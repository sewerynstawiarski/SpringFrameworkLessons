package com.seweryn.RestMvcProject.services;

import com.seweryn.RestMvcProject.Mappers.BeerMapper;
import com.seweryn.RestMvcProject.entities.Beer;
import com.seweryn.RestMvcProject.events.BeerCreatedEvent;
import com.seweryn.RestMvcProject.events.BeerDeletedEvent;
import com.seweryn.RestMvcProject.events.BeerPatchEvent;
import com.seweryn.RestMvcProject.events.BeerUpdatedEvent;
import com.seweryn.RestMvcProject.repositories.BeerRepository;
import com.seweryn.spring_6_restmvc_api.model.BeerDTO;
import com.seweryn.spring_6_restmvc_api.model.BeerStyle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceJPA implements BeerService {
    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;
    private final CacheManager cacheManager;
    private final ApplicationEventPublisher applicationEventPublisher;

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 25;
    @Cacheable(cacheNames = "beerListCache")
    @Override
    public Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer pageNumber, Integer pageSize) {

        Page<Beer> beerPage;

        log.info("listBeers method is working here");

        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

        if (StringUtils.hasText(beerName) && beerStyle == null) {
            beerPage = listBeersByName(beerName, pageRequest);
        } else if (!StringUtils.hasText(beerName) && beerStyle != null) {
            beerPage = listBeersByStyle(beerStyle, pageRequest);
        } else if (StringUtils.hasText(beerName) && beerStyle != null) {
            beerPage = listBeerByNameAndStyle(beerName, beerStyle, pageRequest);
        } else {
            beerPage = beerRepository.findAll(pageRequest);
        }

        if (showInventory != null && !showInventory) {
            beerPage.forEach(beer -> beer.setQuantityOnHand(null));
        }
        return beerPage.map(beerMapper::beerToBeerDto);

//        return beerPage
//                .stream()
//                .map(beerMapper::beerToBeerDto)
//                .toList();
    }
    public static PageRequest buildPageRequest(Integer pageNumber, Integer pageSize) {
        int queryPageNumber;
        int queryPageSize;

        if (pageNumber != null && pageNumber > 0) {
            queryPageNumber = pageNumber -1;
        } else {
            queryPageNumber = DEFAULT_PAGE;
        }
        if (pageSize == null) {
            queryPageSize = DEFAULT_PAGE_SIZE;
        } else {
            if (pageSize > 1000) {
                queryPageSize = 1000;
            } else {
                queryPageSize = pageSize;
            }
        }

        Sort sort = Sort.by(Sort.Order.asc("beerName"));

        return PageRequest.of(queryPageNumber, queryPageSize, sort);

    }

    public Page<Beer> listBeerByNameAndStyle(String beerName, BeerStyle beerStyle, Pageable pageable) {
        return beerRepository.findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle("%" + beerName + "%", beerStyle, pageable);
    }

    public Page<Beer> listBeersByName(String beerName, Pageable pageable) {
        return beerRepository.findAllByBeerNameIsLikeIgnoreCase("%" + beerName + "%", pageable);
    }
    public Page<Beer> listBeersByStyle(BeerStyle beerStyle, Pageable pageable) {
        return beerRepository.findAllByBeerStyle(beerStyle, pageable);
    }
    @Cacheable(cacheNames = "beerCache", key = "#id")
    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        log.info("getBeerById in work here");
        return Optional.ofNullable(beerMapper.beerToBeerDto(beerRepository.findById(id).orElse(null)));
    }

    @Override
    public BeerDTO saveNewBeer(BeerDTO beer) {
        if (cacheManager.getCache("beerListCache") != null) {
            cacheManager.getCache("beerListCache").clear();
        }
        val savedBeer = beerRepository.save(beerMapper.beerDTOToBeer(beer));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        applicationEventPublisher.publishEvent(new BeerCreatedEvent(savedBeer, auth));

        return beerMapper.beerToBeerDto(savedBeer);
    }

    @Override
    public Optional<BeerDTO> updateBeerById(UUID beerId, BeerDTO beer) {
//
//        return Optional.of(beerMapper.beerToBeerDto(
//                beerRepository.save(beerMapper.beerDTOToBeer(beer))
//        ));
        clearCache(beerId);
        AtomicReference<Optional<BeerDTO>> atomicReference = new AtomicReference<>();

        beerRepository.findById(beerId).ifPresentOrElse(foundedBeer -> {
            foundedBeer.setBeerName(beer.getBeerName());
            foundedBeer.setBeerStyle(beer.getBeerStyle());
            foundedBeer.setUpc(beer.getUpc());
            foundedBeer.setPrice(beer.getPrice());
            foundedBeer.setVersion(beer.getVersion());

            val savedBeer = beerRepository.save(foundedBeer);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            applicationEventPublisher.publishEvent(new BeerUpdatedEvent(savedBeer, authentication));

            atomicReference.set(Optional.of(beerMapper.beerToBeerDto(savedBeer)));

        }, () -> {
            atomicReference.set(Optional.empty());
        });
        return atomicReference.get();
    }
//    @Caching(evict = {
//            @CacheEvict(cacheNames = "beerCache", key = "#beerId"),
//            @CacheEvict(cacheNames = "beerListCache")
//    })
    @Override
    public boolean deleteById(UUID beerId) {
        clearCache(beerId);

        if (beerRepository.existsById(beerId)) {
            beerRepository.deleteById(beerId);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            applicationEventPublisher.publishEvent(new BeerDeletedEvent(Beer.builder().id(beerId).build(), authentication));

            return true;
        }
        return false;
    }

    private void clearCache(UUID beerId) {
        Cache beerCache = cacheManager.getCache("beerCache");
        if (beerCache != null) {
        beerCache.evict(beerId);
        }
        Cache beerListCache = cacheManager.getCache("beerListCache");
        if (beerListCache != null) {
            beerListCache.clear();
        }
    }

    @Override
    public boolean patchBeerById(UUID beerId, BeerDTO beer) {

        clearCache(beerId);

        if (beerRepository.existsById(beerId)) {
            beerRepository.findById(beerId).ifPresent(foundedBeer -> {
                if (StringUtils.hasText(beer.getBeerName())) {
                    foundedBeer.setBeerName(beer.getBeerName());
                }
                if (beer.getBeerStyle() != null) {
                    foundedBeer.setBeerStyle(beer.getBeerStyle());
                }
                if (beer.getUpc() !=  null) {
                    foundedBeer.setUpc(beer.getUpc());
                }
                if (beer.getPrice() != null) {
                    foundedBeer.setPrice(beer.getPrice());
                }
                if (beer.getQuantityOnHand() != null) {
                    foundedBeer.setQuantityOnHand(beer.getQuantityOnHand());
                }
                foundedBeer.setUpdateDate(LocalDateTime.now());

                val patchedBeer = beerRepository.save(foundedBeer);

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                applicationEventPublisher.publishEvent(new BeerPatchEvent(patchedBeer, authentication));
            });
            return true;
        }
        return false;
    }
}


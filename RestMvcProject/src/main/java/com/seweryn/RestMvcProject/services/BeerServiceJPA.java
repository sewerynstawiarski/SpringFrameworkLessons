package com.seweryn.RestMvcProject.services;

import com.seweryn.RestMvcProject.Mappers.BeerMapper;
import com.seweryn.RestMvcProject.model.BeerDTO;
import com.seweryn.RestMvcProject.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceJPA implements BeerService {
    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    public List<BeerDTO> listBeers() {
        return beerRepository.findAll().stream().map(beerMapper::beerToBeerDto).toList();
    }

    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        return Optional.ofNullable(beerMapper.beerToBeerDto(beerRepository.findById(id).orElse(null)));
    }

    @Override
    public BeerDTO saveNewBeer(BeerDTO beer) {
        return beerMapper.beerToBeerDto(beerRepository.save(beerMapper.beerDTOToBeer(beer)));
    }

    @Override
    public Optional<BeerDTO> updateBeerById(UUID beerId, BeerDTO beer) {
        AtomicReference<Optional<BeerDTO>> atomicReference = new AtomicReference<>();

        beerRepository.findById(beerId).ifPresentOrElse(foundedBeer -> {
            foundedBeer.setBeerName(beer.getBeerName());
            foundedBeer.setBeerStyle(beer.getBeerStyle());
            foundedBeer.setUpc(beer.getUpc());
            foundedBeer.setPrice(beer.getPrice());
            atomicReference.set(Optional.of(beerMapper.beerToBeerDto(beerRepository.save(foundedBeer))));

        }, () -> {
            atomicReference.set(Optional.empty());
        });
        return atomicReference.get();
    }

    @Override
    public boolean deleteById(UUID beerId) {
        if (beerRepository.existsById(beerId)) {
            beerRepository.deleteById(beerId);
            return true;
        }
        return false;
    }

    @Override
    public boolean patchBeerById(UUID beerId, BeerDTO beer) {
        if (beerRepository.existsById(beerId)) {
            beerRepository.findById(beerId).ifPresent(foundeBeer -> {
                if (StringUtils.hasText(beer.getBeerName())) {
                    foundeBeer.setBeerName(beer.getBeerName());
                }
                if (beer.getBeerStyle() != null) {
                    foundeBeer.setBeerStyle(beer.getBeerStyle());
                }
                if (beer.getUpc() !=  null) {
                    foundeBeer.setUpc(beer.getUpc());
                }
                if (beer.getPrice() != null) {
                    foundeBeer.setPrice(beer.getPrice());
                }
                foundeBeer.setUpdateDate(LocalDateTime.now());
            });
            return true;
        }
        return false;
    }
}


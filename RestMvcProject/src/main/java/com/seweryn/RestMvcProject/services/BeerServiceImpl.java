package com.seweryn.RestMvcProject.services;

import com.seweryn.RestMvcProject.model.Beer;
import com.seweryn.RestMvcProject.model.BeerStyle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {
    private Map<UUID, Beer> beerMap;

    public BeerServiceImpl() {
        this.beerMap = new HashMap<>();

        Beer beer1 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Bojan Forester")
                .beerStyle(BeerStyle.LAGER)
                .upc("23456")
                .price(new BigDecimal("5.99"))
                .quantityOnHand(90)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        Beer beer2 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Bojan Policeman")
                .beerStyle(BeerStyle.PORTER)
                .upc("34567")
                .price(new BigDecimal("6.99"))
                .quantityOnHand(40)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        Beer beer3 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Bojan Doctor")
                .beerStyle(BeerStyle.PILSNER)
                .upc("34567")
                .price(new BigDecimal("6.99"))
                .quantityOnHand(40)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
        log.debug(beerMap.toString());
        beerMap.put(beer1.getId(), beer1);
        beerMap.put(beer2.getId(), beer2);
        beerMap.put(beer3.getId(), beer3);

        log.debug("Beer map from constructor: " + beerMap.values());

    }
    @Override
    public List<Beer> listBeers() {
        return new ArrayList<>(beerMap.values());
    }
    @Override
    public Beer getBeerById(UUID id) {

        log.debug("Get Beer by Id in service. ID: " + id.toString());

        return beerMap.get(id);
    }

    @Override
    public Beer saveNewBeer(Beer beer) {

        Beer savedBeer = Beer.builder()
                .id(UUID.randomUUID())
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .beerName(beer.getBeerName())
                .beerStyle(beer.getBeerStyle())
                .quantityOnHand(beer.getQuantityOnHand())
                .upc(beer.getUpc())
                .price(beer.getPrice())
                .build();

        beerMap.put(savedBeer.getId(), savedBeer);

        return savedBeer;
    }

    @Override
    public void updateBeerById(UUID beerId, Beer beer) {
        Beer existingBeer = beerMap.get(beerId);
        existingBeer.setBeerName(beer.getBeerName());
        existingBeer.setPrice(beer.getPrice());
        existingBeer.setUpc(beer.getUpc());
        existingBeer.setQuantityOnHand(beer.getQuantityOnHand());
        existingBeer.setUpdateDate(LocalDateTime.now());

        beerMap.put(existingBeer.getId(), existingBeer);
    }

    @Override
    public void deleteById(UUID beerId) {

        beerMap.remove(beerId);

    }

    @Override
    public void patchBeerById(UUID beerId, Beer beer) {
        Beer existing = beerMap.get(beerId);

        if (StringUtils.hasText(beer.getBeerName())) {
            existing.setBeerName(beer.getBeerName());
        }
        if (beer.getBeerStyle() != null) {
            existing.setBeerStyle(beer.getBeerStyle());
        }
        if (beer.getPrice() != null) {
            existing.setPrice(beer.getPrice());
        }
        if (beer.getQuantityOnHand() != null) {
            existing.setQuantityOnHand(beer.getQuantityOnHand());
        }
        if (StringUtils.hasText(beer.getUpc())) {
            existing.setUpc(beer.getUpc());
        }


    }
}

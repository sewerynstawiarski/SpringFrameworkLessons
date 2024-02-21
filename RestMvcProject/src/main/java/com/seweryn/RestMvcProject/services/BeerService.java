package com.seweryn.RestMvcProject.services;

import com.seweryn.RestMvcProject.model.BeerDTO;
import com.seweryn.RestMvcProject.model.BeerStyle;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {
    List<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory);

    Optional <BeerDTO> getBeerById(UUID id);

    BeerDTO saveNewBeer(BeerDTO beer);

    Optional<BeerDTO> updateBeerById(UUID beerId, BeerDTO beer);

    boolean deleteById(UUID beerId);

    boolean patchBeerById(UUID beerId, BeerDTO beer);
}

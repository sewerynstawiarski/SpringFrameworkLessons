package com.seweryn.reacitvemongo.services;

import com.seweryn.reacitvemongo.domain.Beer;
import com.seweryn.reacitvemongo.model.BeerDTO;
import com.seweryn.reacitvemongo.model.CustomerDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BeerService {
    Mono<BeerDTO> saveBeer(BeerDTO beerDTO);
    Mono<BeerDTO> getBeerById(String beerId);
    Flux<BeerDTO> listBeers();
    Mono<BeerDTO> updateBeer(String beerId, BeerDTO beerDTO);

    Mono<BeerDTO> patchBeer(String beerId, BeerDTO beerDTO);

    Mono<Void> deleteBeerById(String beerId);
    Mono<BeerDTO> findFirstByBeerName(String beerName);
    Flux<BeerDTO> findByBeerStyle(String beerStyle);
}

package com.seweryn.spring6webclient.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.seweryn.spring6webclient.model.BeerDTO;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Flow;

public interface BeerClient {
    Flux<String> listBeers();
    Flux<Map> listBeerMap();
    Flux<JsonNode> listBeerJsonNode();

    Flux<BeerDTO> listBeerDtos();

    Mono<BeerDTO> getBeerById(String beerId);

    Flux<BeerDTO> getBeerByStyle(String beerStyle);

    Mono<BeerDTO> createBeer(BeerDTO newDTO);

    Mono<BeerDTO> updateBeer(BeerDTO beerDTO);

    Mono<BeerDTO> patchBeer(BeerDTO beerDTO);

    Mono<Void> deleteBeer(String beerId);
}

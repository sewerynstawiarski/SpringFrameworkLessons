package com.seweryn.spring6reactive.controllers;

import com.seweryn.spring6reactive.model.BeerDTO;
import com.seweryn.spring6reactive.services.BeerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class BeerController {

    private final BeerService beerService;

    public static final String BEER_PATH = "/api/v2/beer";
    public static final String BEER_PATH_ID = "/api/v2/beer/{beerId}";
    @GetMapping(BEER_PATH)
    Flux<BeerDTO> listBeers() {
    return beerService.listBeers();
    }
    @GetMapping(BEER_PATH_ID)
    Mono<BeerDTO> getBeerById(@PathVariable("beerId") Integer beerId) {
        return beerService.getBeerById(beerId);
    }
    @PostMapping(BEER_PATH)
    Mono<ResponseEntity<Void>> createNewBeer(@RequestBody BeerDTO beerDTO) {
       return beerService.saveNewBeer(beerDTO).map(savedDTO ->
               ResponseEntity.created(UriComponentsBuilder.fromHttpUrl("http://localhost:8080" + BEER_PATH + "/"
                       + savedDTO.getId())
                       .build().toUri())
                       .build());

    }
    @PutMapping(BEER_PATH_ID)
    Mono<ResponseEntity<Void>> updateExistingBeer(@PathVariable("beerId") Integer beerId,
                                                  @Validated @RequestBody BeerDTO beerDTO) {


        return beerService.updateBeer(beerId, beerDTO)
                .map(savedDTO -> ResponseEntity.ok().build());
    }
    @PatchMapping(BEER_PATH_ID)
    Mono<ResponseEntity<Void>> patchBeer(@PathVariable("beerId") Integer beerId,
                                         @Validated  @RequestBody BeerDTO beerDTO) {
        return beerService.patchBeer(beerId, beerDTO).map(savedDTO -> ResponseEntity.ok().build());
    }
    @DeleteMapping(BEER_PATH_ID)
    Mono<ResponseEntity<Void>> deleteBeerById(@PathVariable Integer beerId) {
        return beerService.deleteBeerById(beerId).map(response -> ResponseEntity.noContent().build());
    }
}

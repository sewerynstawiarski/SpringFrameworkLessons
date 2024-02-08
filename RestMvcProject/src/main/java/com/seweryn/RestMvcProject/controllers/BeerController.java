package com.seweryn.RestMvcProject.controllers;

import com.seweryn.RestMvcProject.model.BeerDTO;
import com.seweryn.RestMvcProject.services.BeerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
//@RequestMapping("/api/v1/beer")
public class BeerController {

    public static final String BEER_PATH = "/api/v1/beer";
    public static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";

    private final BeerService beerService;
    @PatchMapping(BEER_PATH_ID)
    public ResponseEntity updateBeerPatchById(@PathVariable UUID beerId, @RequestBody BeerDTO beer) {

        beerService.patchBeerById(beerId, beer);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(BEER_PATH_ID)
    public ResponseEntity deleteById(@PathVariable("beerId") UUID beerId) {

        beerService.deleteById(beerId);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


    @PutMapping(BEER_PATH_ID)
    public ResponseEntity updateById(@PathVariable UUID beerId, @RequestBody BeerDTO beer) {

        beerService.updateBeerById(beerId, beer);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping(BEER_PATH)
    //@RequestMapping(method = RequestMethod.POST)
    public ResponseEntity handlePost(@RequestBody BeerDTO beer) {
        BeerDTO savedBeer = beerService.saveNewBeer(beer);

        HttpHeaders headers = new HttpHeaders();

        headers.add("Location", "/api/v1/beer/" + savedBeer.getId().toString());


        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

//    @RequestMapping( value = BEER_PATH ,method = RequestMethod.GET)
    @GetMapping(BEER_PATH)
    public List<BeerDTO> listBeers() {
        return beerService.listBeers();
    }
//    @ExceptionHandler(NotFoundException.class)
//    public ResponseEntity handleNotFoundException() {
//        System.out.println("in exception handler");
//        return ResponseEntity.notFound().build();
//    }

//    @RequestMapping(value = BEER_PATH_ID, method = RequestMethod.GET)
    @GetMapping(BEER_PATH_ID)
    public BeerDTO getBeerById(@PathVariable("beerId") UUID beerId) {

        log.debug("Get Beer by ID - in controller -1234");

        return beerService.getBeerById(beerId).orElseThrow(NotFoundException::new);
    }
}

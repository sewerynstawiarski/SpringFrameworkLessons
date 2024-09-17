package com.seweryn.RestMvcProject.controllers;

import com.seweryn.RestMvcProject.entities.BeerOrder;
import com.seweryn.RestMvcProject.model.BeerDTO;
import com.seweryn.RestMvcProject.model.BeerOrderCreateDTO;
import com.seweryn.RestMvcProject.model.BeerOrderDTO;
import com.seweryn.RestMvcProject.model.BeerOrderUpdateDTO;
import com.seweryn.RestMvcProject.services.BeerOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.ServerRequest;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class BeerOrderController {

    private final BeerOrderService beerOrderService;
    public static final String BEER_ORDER = "/api/v1/beer_order";
    public static final String BEER_ORDER_ID = "/api/v1/beer_order/{beerOrderId}";
    @GetMapping(BEER_ORDER_ID)
    public BeerOrderDTO getBeerOrderById(@PathVariable("beerOrderId") UUID beerOrderId) {
        return beerOrderService.getBeerOrderById(beerOrderId).orElseThrow(NotFoundException::new);
    }
    @GetMapping(BEER_ORDER)
    public Page<BeerOrderDTO> listBeerOrders(@RequestParam(required = false) Integer pageSize, @RequestParam(required = false) Integer pageNumber) {
        return beerOrderService.listBeerOrders(pageSize, pageNumber);

    }
    @PostMapping(BEER_ORDER)
    public ResponseEntity<Void> createBeerOrder(@RequestBody BeerOrderCreateDTO beerOrderCreateDTO) {
        BeerOrderDTO beerOrderDTO = beerOrderService.createBeerOrder(beerOrderCreateDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", BEER_ORDER + "/" + beerOrderDTO.getId().toString());
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }
    @PutMapping(BEER_ORDER_ID)
    public ResponseEntity<BeerOrderDTO> updatedBeerOrder(@PathVariable("beerOrderId") UUID beerOrderId,@RequestBody BeerOrderUpdateDTO beerOrderUpdateDTO) {
        return ResponseEntity.ok(beerOrderService.updateBeerOrder(beerOrderId, beerOrderUpdateDTO).orElseThrow(NotFoundException::new));
    }
    @DeleteMapping(BEER_ORDER_ID)
    public ResponseEntity<Void> deleteBeeOrder(@PathVariable("beerOrderId")UUID beerOrderId) {
        beerOrderService.deleteBeerOrderById(beerOrderId);
        return ResponseEntity.noContent().build();
    }
}

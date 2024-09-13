package com.seweryn.RestMvcProject.controllers;

import com.seweryn.RestMvcProject.model.BeerDTO;
import com.seweryn.RestMvcProject.model.BeerOrderDTO;
import com.seweryn.RestMvcProject.services.BeerOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}

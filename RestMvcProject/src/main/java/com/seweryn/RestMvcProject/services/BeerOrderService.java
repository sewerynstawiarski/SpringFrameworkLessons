package com.seweryn.RestMvcProject.services;

import com.seweryn.RestMvcProject.model.BeerOrderCreateDTO;
import com.seweryn.RestMvcProject.model.BeerOrderDTO;
import com.seweryn.RestMvcProject.model.BeerOrderUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface BeerOrderService {
    Page<BeerOrderDTO> listBeerOrders(Integer pageSize, Integer pageNumber);
    Optional<BeerOrderDTO> getBeerOrderById(UUID beerOrderId);

    BeerOrderDTO createBeerOrder(BeerOrderCreateDTO beerOrderCreateDTO);
    Optional<BeerOrderDTO> updateBeerOrder(UUID beerOrderId, BeerOrderUpdateDTO beerOrderUpdateDTO);

    void deleteBeerOrderById(UUID beerOrderId);
}

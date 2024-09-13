package com.seweryn.RestMvcProject.services;

import com.seweryn.RestMvcProject.Mappers.BeerOrderMapper;
import com.seweryn.RestMvcProject.controllers.NotFoundException;
import com.seweryn.RestMvcProject.entities.BeerOrder;
import com.seweryn.RestMvcProject.model.BeerOrderDTO;
import com.seweryn.RestMvcProject.repositories.BeerOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class BeerOrderServiceJPA implements BeerOrderService {
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 25;
    private final BeerOrderRepository beerOrderRepository;
    private final BeerOrderMapper beerOrderMapper;

    @Override
    public Page<BeerOrderDTO> listBeerOrders(Integer pageSize, Integer pageNumber) {
        Page<BeerOrder> beerOrderDTOPage;

        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

        beerOrderDTOPage = beerOrderRepository.findAll(pageRequest);

        return beerOrderDTOPage.map(beerOrderMapper::beerOrderToBeerOrderDto);
    }

    @Override
    public Optional<BeerOrderDTO> getBeerOrderById(UUID beerOrderId) {
        return Optional.ofNullable(beerOrderMapper.beerOrderToBeerOrderDto(beerOrderRepository.findById(beerOrderId).orElse(null)));
    }
    public static PageRequest buildPageRequest(Integer pageNumber, Integer pageSize) {
        int queryPageNumber;
        int queryPageSize;

        if (pageNumber != null && pageNumber > 0) {
            queryPageNumber = pageNumber -1;
        } else {
            queryPageNumber = DEFAULT_PAGE;
        }
        if (pageSize == null) {
            queryPageSize = DEFAULT_PAGE_SIZE;
        } else {
            if (pageSize > 1000) {
                queryPageSize = 1000;
            } else {
                queryPageSize = pageSize;
            }
        }

        Sort sort = Sort.by(Sort.Order.asc("id"));

        return PageRequest.of(queryPageNumber, queryPageSize, sort);

    }
}

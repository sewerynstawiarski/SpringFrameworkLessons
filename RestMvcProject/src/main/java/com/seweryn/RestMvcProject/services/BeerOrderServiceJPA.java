package com.seweryn.RestMvcProject.services;

import com.seweryn.RestMvcProject.Mappers.BeerOrderMapper;
import com.seweryn.RestMvcProject.controllers.NotFoundException;
import com.seweryn.RestMvcProject.entities.BeerOrder;
import com.seweryn.RestMvcProject.entities.BeerOrderLine;
import com.seweryn.RestMvcProject.entities.BeerOrderShipment;
import com.seweryn.RestMvcProject.model.BeerOrderCreateDTO;
import com.seweryn.RestMvcProject.model.BeerOrderDTO;
import com.seweryn.RestMvcProject.model.BeerOrderUpdateDTO;
import com.seweryn.RestMvcProject.repositories.BeerOrderRepository;
import com.seweryn.RestMvcProject.repositories.BeerRepository;
import com.seweryn.RestMvcProject.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class BeerOrderServiceJPA implements BeerOrderService {
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 25;
    private final BeerOrderRepository beerOrderRepository;
    private final BeerOrderMapper beerOrderMapper;
    private final BeerRepository beerRepository;
    private final CustomerRepository customerRepository;

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

    @Override
    public BeerOrderDTO createBeerOrder(BeerOrderCreateDTO beerOrderCreateDTO) {
        BeerOrder createdBeerOrder = BeerOrder.builder()
                .customer(customerRepository.findById(beerOrderCreateDTO.getCustomerId()).orElseThrow(NotFoundException::new))
                .customerRef(beerOrderCreateDTO.getCustomerRef())
                .beerOrderLines(beerOrderCreateDTO.getBeerOrderLines().stream()
                        .map(beerOrderLineCreateDTO -> BeerOrderLine.builder()
                                        .beer(beerRepository.findById(beerOrderLineCreateDTO.getBeerId()).orElseThrow(NotFoundException::new))
                                        .orderQuantity(beerOrderLineCreateDTO.getOrderQuantity())
                                        .build()).collect(Collectors.toSet()))
                .build();
        return beerOrderMapper.beerOrderToBeerOrderDto(beerOrderRepository.save(createdBeerOrder));
    }

    @Override
    public Optional<BeerOrderDTO> updateBeerOrder(UUID beerOrderId, BeerOrderUpdateDTO beerOrderUpdateDTO) {
        var beerOrder = beerOrderRepository.findById(beerOrderId).orElseThrow(NotFoundException::new);

        beerOrder.setCustomerRef(beerOrderUpdateDTO.getCustomerRef());
        beerOrder.setCustomer(customerRepository.findById(beerOrderUpdateDTO.getCustomerId()).orElseThrow(NotFoundException::new));


        beerOrderUpdateDTO.getBeerOrderLinesUpdates().forEach(beerOrderLineUpdateDTO -> {
            if (beerOrderLineUpdateDTO.getBeerId() != null) {
                val foundBeerOrderLine =  beerOrder.getBeerOrderLines().stream()
                        .filter(beerOrderLine1 -> beerOrderLine1.getId().equals(beerOrderLineUpdateDTO.getBeerOrderLineId()))
                        .findFirst().orElseThrow(NotFoundException::new);
                foundBeerOrderLine.setBeer(beerRepository.findById(beerOrderLineUpdateDTO.getBeerId()).orElseThrow(NotFoundException::new));
                foundBeerOrderLine.setOrderQuantity(beerOrderLineUpdateDTO.getOrderQuantity());
                foundBeerOrderLine.setQuantityAllocated(beerOrderLineUpdateDTO.getQuantityAllocated());
        }   else {
                beerOrder.getBeerOrderLines().add(BeerOrderLine.builder()
                        .beer(beerRepository.findById(beerOrderLineUpdateDTO.getBeerId()).orElseThrow(NotFoundException::new))
                        .quantityAllocated(beerOrderLineUpdateDTO.getQuantityAllocated())
                        .orderQuantity(beerOrderLineUpdateDTO.getOrderQuantity())
                        .build());
            }
        });

        if (beerOrderUpdateDTO.getBeerOrderShipmentUpdate() != null && beerOrderUpdateDTO.getBeerOrderShipmentUpdate().getTrackingNumber() != null) {
            if (beerOrder.getBeerOrderShipment() == null) {
                beerOrder.setBeerOrderShipment(BeerOrderShipment.builder()
                        .trackingNumber(beerOrderUpdateDTO.getBeerOrderShipmentUpdate().getTrackingNumber())
                        .build());
            } else {
                beerOrder.getBeerOrderShipment().setTrackingNumber(beerOrderUpdateDTO.getBeerOrderShipmentUpdate().getTrackingNumber());
            }

        }

        var updatedBeerOrder = beerOrderRepository.save(beerOrder);

        return Optional.of(beerOrderMapper.beerOrderToBeerOrderDto(updatedBeerOrder));
    }

    @Override
    public void deleteBeerOrderById(UUID beerOrderId) {
        if (beerOrderRepository.findById(beerOrderId).isPresent()) {
            beerOrderRepository.deleteById(beerOrderId);
        } else {
            throw new NotFoundException();
        }
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

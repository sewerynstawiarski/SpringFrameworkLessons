package com.seweryn.RestMvcProject.Mappers;

import com.seweryn.RestMvcProject.entities.BeerOrder;
import com.seweryn.spring_6_restmvc_api.model.BeerOrderDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BeerOrderMapper {
    BeerOrderDTO beerOrderToBeerOrderDto(BeerOrder beerOrder);
    BeerOrder beerOrderDtoToBeerOrder(BeerOrderDTO beerOrderDTO);
}

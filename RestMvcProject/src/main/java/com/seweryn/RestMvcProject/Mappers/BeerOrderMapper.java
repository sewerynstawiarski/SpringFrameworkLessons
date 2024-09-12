package com.seweryn.RestMvcProject.Mappers;

import com.seweryn.RestMvcProject.entities.BeerOrder;
import com.seweryn.RestMvcProject.model.BeerOrderDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BeerOrderMapper {
    BeerOrderDTO beerOrderToBeerOrderDto(BeerOrder beerOrder);
    BeerOrder beerOrderDtoToBeerOrder(BeerOrderDTO beerOrderDTO);
}

package com.seweryn.RestMvcProject.Mappers;

import com.seweryn.RestMvcProject.entities.BeerOrderShipment;
import com.seweryn.spring_6_restmvc_api.model.BeerOrderShipmentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface BeerOrderShipmentMapper {
    @Mapping(target = "beerOrder", ignore = true)
    BeerOrderShipment beerOrderShipmentDtoToBeerOrderShipment(BeerOrderShipmentDTO beerOrderShipmentDTO);
    BeerOrderShipmentDTO beerOrderShipmentToBeerOrderShipmentDto(BeerOrderShipment beerOrderShipment);
}

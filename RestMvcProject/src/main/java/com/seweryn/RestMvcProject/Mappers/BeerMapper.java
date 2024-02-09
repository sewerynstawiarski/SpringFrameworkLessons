package com.seweryn.RestMvcProject.Mappers;

import com.seweryn.RestMvcProject.entities.Beer;
import com.seweryn.RestMvcProject.model.BeerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {

    Beer beerDTOToBeer(BeerDTO beer);
    BeerDTO beerToBeerDto(Beer beer);

}

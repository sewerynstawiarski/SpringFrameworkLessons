package com.seweryn.reacitvemongo.mappers;

import com.seweryn.reacitvemongo.domain.Beer;
import com.seweryn.reacitvemongo.model.BeerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {
    BeerDTO beerToBeerDto(Beer beer);
    Beer beerDtoToBeer(BeerDTO beerDTO);
}

package com.seweryn.spring6reactive.mapper;

import com.seweryn.spring6reactive.domain.Beer;
import com.seweryn.spring6reactive.model.BeerDTO;
import org.mapstruct.Mapper;
@Mapper
public interface BeerMapper {
    Beer beerDTOToBeer(BeerDTO beerDTO);
    BeerDTO beerToBeerDTO(Beer beer);
}

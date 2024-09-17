package com.seweryn.RestMvcProject.Mappers;

import com.seweryn.RestMvcProject.entities.Beer;
import com.seweryn.RestMvcProject.entities.BeerAudit;
import com.seweryn.RestMvcProject.model.BeerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface BeerMapper {
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "beerOrderLines", ignore = true)
    Beer beerDTOToBeer(BeerDTO beerDTO);
    BeerDTO beerToBeerDto(Beer beer);
    @Mapping(target = "creationDateAudit", ignore = true)
    @Mapping(target = "auditId", ignore = true)
    @Mapping(target = "principalName", ignore = true)
    @Mapping(target = "auditEventType", ignore = true)
    BeerAudit beerToBeerAudit(Beer beer);

}

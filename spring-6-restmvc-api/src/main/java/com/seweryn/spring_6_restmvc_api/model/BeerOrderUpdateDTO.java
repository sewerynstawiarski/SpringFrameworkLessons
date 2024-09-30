package com.seweryn.spring_6_restmvc_api.model;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;
@Data
@Builder
public class BeerOrderUpdateDTO {
    @NotNull
    private UUID customerId;
    private String customerRef;
    private Set<BeerOrderLineUpdateDTO> beerOrderLinesUpdates;
    private BeerOrderShipmentUpdateDTO beerOrderShipmentUpdate;
    private BigDecimal paymentAmount;
}

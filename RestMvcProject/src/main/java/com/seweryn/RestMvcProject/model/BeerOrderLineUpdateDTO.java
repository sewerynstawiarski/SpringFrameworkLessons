package com.seweryn.RestMvcProject.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;
@Data
@Builder
public class BeerOrderLineUpdateDTO {
    private UUID beerOrderLineId;
    private Integer quantityAllocated;
    @Min(value = 1 , message = "Order quantity must be over 0.")
    private Integer orderQuantity;
    @NotNull
    private UUID beerId;

}

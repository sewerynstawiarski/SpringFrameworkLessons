package com.seweryn.RestMvcProject.model;

import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;
@Data
@Builder
public class BeerOrderLineDTO {
    private UUID id;
    private Long version;
    private Timestamp createdDate;
    private Timestamp lastModifiedDate;
    @Min(value = 1 , message = "Order quantity must be over 0.")
    private Integer orderQuantity;
    private Integer quantityAllocated;
    private BeerDTO beer;
}

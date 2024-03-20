package com.seweryn.RestMvcProject.model;

import com.seweryn.RestMvcProject.entities.BeerOrder;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
@Data
@Builder
public class CustomerDTO {
    private UUID id;
    private String customerName;
    private String email;
    private Integer version;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private Set<BeerOrder> beerOrders;
}

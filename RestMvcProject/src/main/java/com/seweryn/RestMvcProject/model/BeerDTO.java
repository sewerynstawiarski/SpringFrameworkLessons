package com.seweryn.RestMvcProject.model;

import com.seweryn.RestMvcProject.entities.BeerOrderLine;
import com.seweryn.RestMvcProject.entities.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
@Builder
@Data
public class BeerDTO {
    private UUID id;
    private Integer version;
    @NotNull
    @NotBlank
    private String beerName;
    @NotNull
    private BeerStyle beerStyle;
    @NotNull
    @NotBlank
    private String upc;
    private Integer quantityOnHand;
    @NotNull
    private BigDecimal price;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
    private Set<Category> categories;
    private Set<BeerOrderLine> beerOrderLines;
}

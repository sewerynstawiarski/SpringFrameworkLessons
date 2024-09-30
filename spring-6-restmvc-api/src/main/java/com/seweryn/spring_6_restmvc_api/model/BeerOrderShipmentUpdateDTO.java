package com.seweryn.spring_6_restmvc_api.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BeerOrderShipmentUpdateDTO {
    @NotBlank
    private String trackingNumber;
}

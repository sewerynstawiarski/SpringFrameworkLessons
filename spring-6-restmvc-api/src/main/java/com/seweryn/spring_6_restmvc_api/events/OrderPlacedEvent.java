package com.seweryn.spring_6_restmvc_api.events;

import com.seweryn.spring_6_restmvc_api.model.BeerOrderDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderPlacedEvent {

    private BeerOrderDTO beerOrderDTO;
}

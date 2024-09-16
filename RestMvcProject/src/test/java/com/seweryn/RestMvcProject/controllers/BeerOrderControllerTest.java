package com.seweryn.RestMvcProject.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seweryn.RestMvcProject.Mappers.BeerOrderMapper;
import com.seweryn.RestMvcProject.entities.Beer;
import com.seweryn.RestMvcProject.entities.BeerOrder;
import com.seweryn.RestMvcProject.entities.BeerOrderLine;
import com.seweryn.RestMvcProject.entities.Customer;
import com.seweryn.RestMvcProject.model.*;
import com.seweryn.RestMvcProject.repositories.BeerOrderRepository;
import com.seweryn.RestMvcProject.repositories.BeerRepository;
import com.seweryn.RestMvcProject.repositories.CustomerRepository;
import jdk.jfr.ContentType;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.seweryn.RestMvcProject.controllers.BeerControllerTest.jwtRequestPostProcessor;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.Is.is;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class BeerOrderControllerTest {
    @Autowired
    BeerOrderRepository beerOrderRepository;
    @Autowired
    BeerOrderMapper beerOrderMapper;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    BeerOrderController beerOrderController;
    @Autowired
    WebApplicationContext webApplicationContext;
    @Autowired
    BeerRepository beerRepository;
    @Autowired
    CustomerRepository customerRepository;
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    void testGetBeerOrderById() throws Exception {
        BeerOrder beerOrder = beerOrderRepository.findAll().getFirst();

        mockMvc.perform(get(BeerOrderController.BEER_ORDER_ID, beerOrder.getId())
                        .with(jwtRequestPostProcessor)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(beerOrder.getId().toString())));

    }

    @Test
    void testListBeerOrder() throws Exception {
        mockMvc.perform(get(BeerOrderController.BEER_ORDER)
                        .with(jwtRequestPostProcessor)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", greaterThan(0)));
    }

    @Test
    void testCreateBeerOrder() throws Exception {
        var beer =  beerRepository.findAll().getFirst();
        var customer = customerRepository.findAll().getFirst();

        BeerOrderCreateDTO beerOrderCreateDTO = BeerOrderCreateDTO.builder()
                .beerOrderLines(Set.of(BeerOrderLineCreateDTO.builder()
                                .beerId(beer.getId())
                        .build()))
                .customerId(customer.getId())
                .build();

        mockMvc.perform(post(BeerOrderController.BEER_ORDER)
                        .with(jwtRequestPostProcessor)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(beerOrderCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    @Transactional
    void testUpdateBeerOrder() throws Exception {
        var beerOrder = beerOrderRepository.findAll().getFirst();
        Set<BeerOrderLineUpdateDTO> lines = new HashSet<>();

       beerOrder.getBeerOrderLines().forEach(beerOrderLine ->
                lines.add(BeerOrderLineUpdateDTO.builder()
                        .beerId(beerOrderLine.getBeer().getId())
                        .beerOrderLineId(beerOrderLine.getId())
                        .orderQuantity(beerOrderLine.getOrderQuantity())
                        .quantityAllocated(beerOrderLine.getQuantityAllocated())
                        .build())
        );



        var beerOrderUpdate = BeerOrderUpdateDTO.builder()
                .customerId(beerOrder.getCustomer().getId())
                .customerRef("UPDATE")
                .beerOrderShipmentUpdate(BeerOrderShipmentUpdateDTO.builder()
                        .trackingNumber(beerOrder.getBeerOrderShipment().toString())
                        .build())
                .beerOrderLinesUpdates(lines).build();

        mockMvc.perform(put(BeerOrderController.BEER_ORDER_ID, beerOrder.getId().toString())
                        .with(jwtRequestPostProcessor)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(beerOrderUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerRef").value("UPDATE"));

    }
}
package com.seweryn.RestMvcProject.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seweryn.RestMvcProject.Mappers.BeerMapper;
import com.seweryn.RestMvcProject.entities.Beer;
import com.seweryn.RestMvcProject.events.BeerCreatedEvent;
import com.seweryn.RestMvcProject.events.BeerDeletedEvent;
import com.seweryn.RestMvcProject.events.BeerPatchEvent;
import com.seweryn.RestMvcProject.events.BeerUpdatedEvent;
import com.seweryn.RestMvcProject.repositories.BeerOrderRepository;
import com.seweryn.RestMvcProject.repositories.BeerRepository;
import com.seweryn.spring_6_restmvc_api.model.BeerDTO;
import com.seweryn.spring_6_restmvc_api.model.BeerStyle;
import lombok.val;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;


import static org.hamcrest.core.Is.is;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RecordApplicationEvents
class BeerControllerIT {
    @Autowired
    ApplicationEvents applicationEvents;
    @Autowired
    BeerController beerController;
    @Autowired
    BeerRepository beerRepository;
    @Autowired
    BeerMapper beerMapper;
    @Autowired
    WebApplicationContext wac;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    BeerOrderRepository beerOrderRepository;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(springSecurity())
                .build();
    }

    @Test
    void testInvalidAuthentication() throws Exception {
        mockMvc.perform(get(BeerController.BEER_PATH))
                .andExpect(status().isUnauthorized());


    }

    @Test
    void testCreateBeerMVC() throws Exception {
        val beerDTO = BeerDTO.builder()
                .beerName("Some New Beer")
                .beerStyle(BeerStyle.IPA)
                .upc("654321")
                .price(BigDecimal.TEN)
                .quantityOnHand(5)
                .build();

        mockMvc.perform(post(BeerController.BEER_PATH)
                        .with(BeerControllerTest.jwtRequestPostProcessor)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerDTO)))
                .andExpect(status().isCreated())
                .andReturn();

        Assertions.assertEquals(1, applicationEvents.stream(BeerCreatedEvent.class).count());
    }

    @Test
    void testUpdateBeerMVC() throws Exception {
        Beer beer = beerRepository.findAll().get(0);

        BeerDTO beerDTO = beerMapper.beerToBeerDto(beer);

        beerDTO.setBeerName("NEW NAME");

        mockMvc.perform(put(BeerController.BEER_PATH_ID, beerDTO.getId())
                        .with(BeerControllerTest.jwtRequestPostProcessor)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerDTO)))
                .andExpect(status().isNoContent())
                .andReturn();

        Assertions.assertEquals(1, applicationEvents.stream(BeerUpdatedEvent.class).count());

    }
    @Test
    void testPatchBeerMVC() throws Exception {
        Beer beer = beerRepository.findAll().get(0);

        BeerDTO beerDTO = beerMapper.beerToBeerDto(beer);

        beerDTO.setBeerName("NEW NAME PATCH");

        mockMvc.perform(patch(BeerController.BEER_PATH_ID, beer.getId())
                        .with(BeerControllerTest.jwtRequestPostProcessor)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerDTO)))
                .andExpect(status().isNoContent())
                .andReturn();

        Assertions.assertEquals(1, applicationEvents.stream(BeerPatchEvent.class).count());

    }
    @Test
    void testDeleteBeerMVC() throws Exception {
        Beer beer = beerRepository.save(Beer.builder()
                        .beerName("TEST")
                        .beerStyle(BeerStyle.IPA)
                        .upc("654321")
                        .price(BigDecimal.TEN)
                .build());

        mockMvc.perform(delete(BeerController.BEER_PATH_ID, beer.getId())
                        .with(BeerControllerTest.jwtRequestPostProcessor)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();

        Assertions.assertEquals(1, applicationEvents.stream(BeerDeletedEvent.class).count());

    }

    @Test
    void testListBeersByName() throws Exception {
        mockMvc.perform(get(BeerController.BEER_PATH)
                        .with(BeerControllerTest.jwtRequestPostProcessor)
                        .queryParam("beerName", "IPA")
                        .queryParam("pageSize", "800"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(336)));
    }

    @Test
    void testListBeersByStyle() throws Exception {
        mockMvc.perform(get(BeerController.BEER_PATH)
                        .with(BeerControllerTest.jwtRequestPostProcessor)
                        .queryParam("beerStyle", BeerStyle.IPA.toString())
                        .queryParam("pageSize", "800"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(547)));

    }

    @Test
    void testListBeersByStyleAndNameShowInventoryFalse() throws Exception {
        mockMvc.perform(get(BeerController.BEER_PATH)
                        .with(BeerControllerTest.jwtRequestPostProcessor)
                        .queryParam("beerName", "IPA")
                        .queryParam("beerStyle", BeerStyle.IPA.name())
                        .queryParam("showInventory", "false")
                        .queryParam("pageSize", "800"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(310)))
                .andExpect(jsonPath("$.content[0].quantityOnHand").value(IsNull.nullValue()));
    }

    @Test
    void testListBeersByStyleAndNameShowInventoryTrue() throws Exception {
        mockMvc.perform(get(BeerController.BEER_PATH)
                        .with(BeerControllerTest.jwtRequestPostProcessor)
                        .queryParam("beerName", "IPA")
                        .queryParam("beerStyle", BeerStyle.IPA.name())
                        .queryParam("showInventory", "true")
                        .queryParam("pageSize", "800"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(310)))
                .andExpect(jsonPath("$.content[0].quantityOnHand").value(IsNull.notNullValue()));
    }

    @Test
    void testListBeersByStyleAndNameShowInventoryTruePage2() throws Exception {
        mockMvc.perform(get(BeerController.BEER_PATH)
                        .with(BeerControllerTest.jwtRequestPostProcessor)
                        .queryParam("beerName", "IPA")
                        .queryParam("beerStyle", BeerStyle.IPA.name())
                        .queryParam("showInventory", "true")
                        .queryParam("pageNumber", "2")
                        .queryParam("pageSize", "50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(50)))
                .andExpect(jsonPath("$.content[0].quantityOnHand").value(IsNull.notNullValue()));
    }

    @Test
    void testListBeersByStyleAndName() throws Exception {
        mockMvc.perform(get(BeerController.BEER_PATH)
                        .with(BeerControllerTest.jwtRequestPostProcessor)
                        .queryParam("beerName", "IPA")
                        .queryParam("beerStyle", BeerStyle.IPA.name())
                        .queryParam("pageSize", "800"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(310)));
    }

    @Test
    void testPatchBeerBadName() throws Exception {
        Beer beer = beerRepository.findAll().get(0);

        Map<String, Object> beerMap = new HashMap<>();
        beerMap.put("beerName", "7564758675645377564758675645377564758675645377564758675645377564758675645377564758675645377564758675645377564758675645377564758675645377564758675645");
        beerMap.put("upc", "9000");
        beerMap.put("beerStyle", BeerStyle.PALE_ALE);
        MvcResult result = mockMvc.perform(patch(BeerController.BEER_PATH_ID, beer.getId())
                        .with(BeerControllerTest.jwtRequestPostProcessor)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerMap)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(1)))
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());

    }

    @Test
    void testBeerIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            beerController.getBeerById(UUID.randomUUID());
        });
    }

    @Test
    void testGetById() {
        Beer beer = beerRepository.findAll().get(0);
        BeerDTO dto = beerController.getBeerById(beer.getId());

        assertThat(dto).isNotNull();

    }

    @Test
    void tesListBeers() {
        Page<BeerDTO> beerDTOS = beerController.listBeers(null, null, false, 1, 2413);

        assertThat(beerDTOS.getContent().size()).isEqualTo(1000);
    }

    @Rollback
    @Transactional
    @Test
    void testEmptyList() {
        beerOrderRepository.deleteAll();
        beerRepository.deleteAll();
        List<BeerDTO> beerDTOS = beerController.listBeers(null, null, false, 1, 25).getContent();

        assertThat(beerDTOS.size()).isEqualTo(0);
    }

    @Rollback
    @Transactional
    @Test
    void tesSaveNewBeer() {
        BeerDTO beerDTO = BeerDTO.builder()
                .beerName("New Beer")
                .build();
        ResponseEntity responseEntity = beerController.handlePost(beerDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID savedUUID = UUID.fromString(locationUUID[4]);

        Beer beer = beerRepository.findById(savedUUID).get();
        assertThat(beer).isNotNull();
    }

    @Transactional
    @Rollback
    @Test
    void testUpdateExistingBeer() {
        Beer beer = beerRepository.findAll().get(0);
        BeerDTO beerDTO = beerMapper.beerToBeerDto(beer);

        beerDTO.setId(null);
        beerDTO.setVersion(null);
        final String beerName = "UPDATED";
        beerDTO.setBeerName(beerName);

        ResponseEntity responseEntity = beerController.updateById(beer.getId(), beerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.valueOf(204));

        Beer updatedBeer = beerRepository.findById(beer.getId()).get();
        assertThat(updatedBeer.getBeerName()).isEqualTo(beerName);
        Assertions.assertEquals(1, applicationEvents.stream(BeerUpdatedEvent.class).count());

    }

    @Test
    void testUpdateNotFound() {
        assertThrows(NotFoundException.class, () -> {
            beerController.updateById(UUID.randomUUID(), BeerDTO.builder().build());
        });
    }

    @Transactional
    @Rollback
    @Test
    void testDeleteByIdFound() {
        Beer beer = beerRepository.findAll().get(0);

        ResponseEntity responseEntity = beerController.deleteById(beer.getId());

        assertThat(beerRepository.findById(beer.getId())).isEmpty();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.valueOf(204));
        Assertions.assertEquals(1, applicationEvents.stream(BeerDeletedEvent.class).count());

//        Beer foundBeer = beerRepository.findById(beer.getId()).get();
//        assertThat(foundBeer).isNull();
    }

    @Test
    void testDeleteByIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            beerController.deleteById(UUID.randomUUID());
        });
    }

    @Transactional
    @Rollback
    @Test
    void testPatchById() {
        Beer beer = beerRepository.findAll().get(0);
        BeerDTO beerDTO = beerMapper.beerToBeerDto(beer);

        final String beerName = "UPDATED";
        beerDTO.setBeerName(beerName);

        ResponseEntity responseEntity = beerController.updateBeerPatchById(beerDTO.getId(), beerDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.valueOf(204));
        assertThat(beerRepository.findById(beerDTO.getId()).get().getBeerName()).isEqualTo(beerName);
        Assertions.assertEquals(1, applicationEvents.stream(BeerPatchEvent.class).count());

    }

    @Test
    void testUpdatePatchByIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            beerController.deleteById(UUID.randomUUID());
        });
    }

    @Test
    void testUpdateBeerBadVersion() throws Exception {
        Beer beer = beerRepository.findAll().get(0);

        BeerDTO beerDTO = beerMapper.beerToBeerDto(beer);

        beerDTO.setBeerName("Updated");

        MvcResult mvcResult = mockMvc.perform(put(BeerController.BEER_PATH_ID, beer.getId())
                        .with(BeerControllerTest.jwtRequestPostProcessor)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerDTO)))
                .andExpect(status().isNoContent())
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());

        beerDTO.setBeerName("Updated - 2");

        MvcResult mvcResult2 = mockMvc.perform(put(BeerController.BEER_PATH_ID, beer.getId())
                        .with(BeerControllerTest.jwtRequestPostProcessor)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerDTO)))
                .andExpect(status().isNoContent())
                .andReturn();

        System.out.println(mvcResult2.getResponse().getContentAsString());
    }
}
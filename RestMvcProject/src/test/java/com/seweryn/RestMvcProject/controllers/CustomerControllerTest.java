package com.seweryn.RestMvcProject.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seweryn.RestMvcProject.model.CustomerDTO;
import com.seweryn.RestMvcProject.services.CustomerService;
import com.seweryn.RestMvcProject.services.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    CustomerService customerService;
    @Autowired
    ObjectMapper objectMapper;
    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;
    @Captor
    ArgumentCaptor<CustomerDTO> customerArgumentCaptor;

    CustomerServiceImpl customerServiceImpl;

    @BeforeEach
    void setUp() {
        customerServiceImpl = new CustomerServiceImpl();
    }

    @Test
    void testCustomerPatch() throws Exception {
        CustomerDTO customerTest = customerServiceImpl.getCustomers().get(0);

        Map<String, Object> customerMap = new HashMap<>();
        customerMap.put("customerName", "Changed Name");

        mockMvc.perform(patch(CustomerController.CUSTOMER_PATH_ID, customerTest.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerMap)))
                .andExpect(status().isNoContent());

        verify(customerService).updateCustomerPatchById(uuidArgumentCaptor.capture(), customerArgumentCaptor.capture());

        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(customerTest.getId());
        assertThat(customerArgumentCaptor.getValue().getCustomerName()).isEqualTo(customerMap.get("customerName"));

    }

    @Test
    void testCustomerDelete() throws Exception {
        CustomerDTO testCustomer =  customerServiceImpl.getCustomers().get(0);

        mockMvc.perform(delete(CustomerController.CUSTOMER_PATH_ID, testCustomer.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());


        verify(customerService).deleteById(uuidArgumentCaptor.capture());

        assertThat(testCustomer.getId()).isEqualTo(uuidArgumentCaptor.getValue());


    }

    @Test
    void testCustomerUpdate() throws Exception {
        CustomerDTO customerTest = customerServiceImpl.getCustomers().get(0);

        mockMvc.perform(put(CustomerController.CUSTOMER_PATH_ID, customerTest.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerTest)))
                .andExpect(status().isNoContent());

        verify(customerService).updateById(uuidArgumentCaptor.capture(), any(CustomerDTO.class));

        assertThat(customerTest.getId()).isEqualTo(uuidArgumentCaptor.getValue());

    }

    @Test
    void testCreateNewBeer() throws Exception {
        CustomerDTO testCustomer = customerServiceImpl.getCustomers().get(0);

        testCustomer.setVersion(null);
        testCustomer.setId(null); // because it should not be implemented in the json that we will create from object mapper


        given(customerService.addCustomer(any(CustomerDTO.class))).willReturn(customerServiceImpl.getCustomers().get(1)); // we want full custmoer object to be returned, witch id and version, that's why we use number 1 and not the one we've changed

        mockMvc.perform(post(CustomerController.CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCustomer)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));

    }

    @Test
    void getCustomersById() throws Exception {

        given(customerService.getCustomers()).willReturn(customerServiceImpl.getCustomers());

        mockMvc.perform(get(CustomerController.CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));

    }

    @Test
    void getCustomerByIdNotFound() throws Exception {
        given(customerService.getCustomerById(any(UUID.class))).willReturn(Optional.empty());

        mockMvc.perform(get(BeerController.BEER_PATH_ID, UUID.randomUUID())).andExpect(status().isNotFound());
    }

    @Test
    void getCustomerById() throws Exception {
        CustomerDTO customerTest = customerServiceImpl.getCustomers().get(0);

        given(customerService.getCustomerById(customerTest.getId())).willReturn(Optional.of(customerTest));

        mockMvc.perform(get(CustomerController.CUSTOMER_PATH_ID, customerTest.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.customerName", is(customerTest.getCustomerName())));

    }
}
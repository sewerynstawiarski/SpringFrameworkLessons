package com.seweryn.RestMvcProject.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seweryn.RestMvcProject.model.Beer;
import com.seweryn.RestMvcProject.model.Customer;
import com.seweryn.RestMvcProject.services.CustomerService;
import com.seweryn.RestMvcProject.services.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;
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

    CustomerServiceImpl customerServiceImpl;

    @BeforeEach
    void setUp() {
        customerServiceImpl = new CustomerServiceImpl();
    }

    @Test
    void testCustomerUpdate() throws Exception {
        Customer customerTest = customerServiceImpl.getCustomers().get(0);

        mockMvc.perform(put("/api/v1/customer/" + customerTest.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerTest)))
                .andExpect(status().isNoContent());

        verify(customerService).updateById(any(UUID.class), any(Customer.class));

    }

    @Test
    void testCreateNewBeer() throws Exception {
        Customer testCustomer = customerServiceImpl.getCustomers().get(0);

        testCustomer.setVersion(null);
        testCustomer.setId(null); // because it should not be implemented in the json that we will create from object mapper


        given(customerService.addCustomer(any(Customer.class))).willReturn(customerServiceImpl.getCustomers().get(1)); // we want full custmoer object to be returned, witch id and version, that's why we use number 1 and not the one we've changed

        mockMvc.perform(post("/api/v1/customer")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCustomer)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));

    }

    @Test
    void getCustomersById() throws Exception {

        given(customerService.getCustomers()).willReturn(customerServiceImpl.getCustomers());

        mockMvc.perform(get("/api/v1/customer")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));

    }

    @Test
    void getCustomerById() throws Exception {
        Customer customerTest = customerServiceImpl.getCustomers().get(0);

        given(customerService.getCustomerById(customerTest.getId())).willReturn(customerTest);

        mockMvc.perform(get("/api/v1/customer/" + customerTest.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.customerName", is(customerTest.getCustomerName())));

    }
}
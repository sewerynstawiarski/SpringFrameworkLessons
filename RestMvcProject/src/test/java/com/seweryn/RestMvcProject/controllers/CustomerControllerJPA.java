package com.seweryn.RestMvcProject.controllers;

import com.seweryn.RestMvcProject.entities.Customer;
import com.seweryn.RestMvcProject.model.CustomerDTO;
import com.seweryn.RestMvcProject.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class CustomerControllerIT {
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    CustomerController customerController;

    @Test
    void testGetCustomerById() {
        Customer customer = customerRepository.findAll().get(0);

        CustomerDTO customerDTO = customerController.getCustomerById(customer.getId());

        assertThat(customerDTO).isNotNull();
    }

    @Test
    void testGetByIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            CustomerDTO customerDTO = customerController.getCustomerById(UUID.randomUUID());
        });

    }

    @Test
    void testGetCustomers() {
        List<CustomerDTO> customerDTOS = customerController.getGustomersList();

        assertThat(customerDTOS.size()).isEqualTo(3);
    }
    @Transactional
    @Rollback
    @Test
    void testGetCustomersNull() {
        customerRepository.deleteAll();
        List<CustomerDTO> customerDTOS = customerController.getGustomersList();

        assertThat(customerDTOS.size()).isEqualTo(0);

    }
}
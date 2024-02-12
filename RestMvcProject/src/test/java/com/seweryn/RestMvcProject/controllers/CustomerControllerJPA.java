package com.seweryn.RestMvcProject.controllers;

import com.seweryn.RestMvcProject.entities.Beer;
import com.seweryn.RestMvcProject.entities.Customer;
import com.seweryn.RestMvcProject.model.BeerDTO;
import com.seweryn.RestMvcProject.model.CustomerDTO;
import com.seweryn.RestMvcProject.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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
    @Transactional
    @Rollback
    @Test
    void testSaveNewCustomer() {
        CustomerDTO customerDTO = CustomerDTO.builder()
                .customerName("NEW CUSTOMER")
                .build();

        ResponseEntity responseEntity = customerController.addCustomer(customerDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String[] location = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID uuid = UUID.fromString(location[4]);

        assertThat(customerRepository.findById(uuid).get()).isNotNull();
    }
    @Transactional
    @Rollback
    @Test
    void testUpdateCustomerById() {
        Customer customer = customerRepository.findAll().get(0);
        CustomerDTO customerDTO = customerController.getGustomersList().get(0);

        final String customerName = "UPDATED";
        customerDTO.setCustomerName(customerName);

        ResponseEntity responseEntity = customerController.updateCustomerByID(customer.getId(), customerDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.valueOf(204));

        Customer updatedCustomer = customerRepository.findById(customer.getId()).get();

        assertThat(updatedCustomer.getCustomerName()).isEqualTo(customerName);
    }

    @Test
    void testUpdateCustomerByIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            customerController.updateCustomerByID(UUID.randomUUID(), CustomerDTO.builder().build());
        });
    }
    @Transactional
    @Rollback
    @Test
    void testDeleteCustomerById() {
        CustomerDTO customerDTO = customerController.getGustomersList().get(0);

        ResponseEntity responseEntity = customerController.deleteCustomerById(customerDTO.getId());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.valueOf(204));

        assertThat(customerRepository.findById(customerDTO.getId())).isEmpty();
    }

    @Test
    void tesDeleteCustomerByIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            customerController.deleteCustomerById(UUID.randomUUID());
        });
    }
    @Transactional
    @Rollback
    @Test
    void testPatchCustomerPatchById() {
        Customer customer = customerRepository.findAll().get(0);
        CustomerDTO customerDTO = customerController.getGustomersList().get(0);

        final String customerName = "UPDATED";
        customerDTO.setCustomerName(customerName);

        ResponseEntity responseEntity = customerController.updateCustomerPatchById(customer.getId(), customerDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.valueOf(204));

        Customer updatedCustomer = customerRepository.findById(customer.getId()).get();

        assertThat(updatedCustomer.getCustomerName()).isEqualTo(customerName);
    }

    @Test
    void testPatchCustomerByIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            customerController.updateCustomerPatchById(UUID.randomUUID(), CustomerDTO.builder().build());
        });
    }
}
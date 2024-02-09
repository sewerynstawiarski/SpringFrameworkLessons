package com.seweryn.RestMvcProject.repositories;

import com.seweryn.RestMvcProject.entities.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class CustomerRepositoryTest {
    @Autowired
    CustomerRepository customerRepository;

    @Test
    void CustomerSavedTest() {
        Customer savedCustomer = customerRepository.save(Customer.builder()
                        .customerName("Tadek")
                .build());

        assertThat(savedCustomer.getId()).isNotNull();

    }
}
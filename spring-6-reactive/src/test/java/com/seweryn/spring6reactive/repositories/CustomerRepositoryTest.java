package com.seweryn.spring6reactive.repositories;

import com.seweryn.spring6reactive.config.DatabaseConfig;
import com.seweryn.spring6reactive.domain.Customer;
import com.seweryn.spring6reactive.mapper.CustomerMapper;
import com.seweryn.spring6reactive.model.CustomerDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import reactor.core.publisher.Flux;

import static org.junit.jupiter.api.Assertions.*;
@DataR2dbcTest
@Import(DatabaseConfig.class)
public class CustomerRepositoryTest {
    @Autowired
    CustomerRepository customerRepository;


    @Test
    void testListBeers() {
        var customers =  customerRepository.findAll().blockFirst();

        System.out.println(customers.getCustomerName());


    }
    public static Customer getCustomer() {
        return Customer.builder()
                .customerName("Sullivan")
                .build();
    }
}
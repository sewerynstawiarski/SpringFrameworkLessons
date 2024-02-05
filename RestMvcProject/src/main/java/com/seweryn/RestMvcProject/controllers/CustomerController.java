package com.seweryn.RestMvcProject.controllers;

import com.seweryn.RestMvcProject.model.Customer;
import com.seweryn.RestMvcProject.services.CustomerService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/customer")
public class CustomerController {
    private final CustomerService customerService;

    @RequestMapping(value = "/{customerId}", method = RequestMethod.GET)
    public Customer getCustomerById(@PathVariable("customerId") UUID customerID) {
        return customerService.getCustomerById(customerID);
    }
    @RequestMapping(method = RequestMethod.GET)
    public List<Customer> getGustomersList() {
        return customerService.getCustomers();
    }
}

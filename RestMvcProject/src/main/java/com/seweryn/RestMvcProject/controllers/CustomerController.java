package com.seweryn.RestMvcProject.controllers;

import com.seweryn.RestMvcProject.model.Customer;
import com.seweryn.RestMvcProject.services.CustomerService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/customer")
public class CustomerController {
    private final CustomerService customerService;

    @PatchMapping("{customerId}")
    public ResponseEntity updateCustomerPatchById(@PathVariable UUID customerId, @RequestBody Customer customer) {

        customerService.updateCustomerPatchById(customerId, customer);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
    @DeleteMapping("{customerId}")
    public ResponseEntity deleteCustomerById(@PathVariable("customerId") UUID id) {

        customerService.deleteById(id);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping("{customerId}")
    public ResponseEntity updateCustomerByID(@PathVariable UUID customerId,@RequestBody Customer customer) {

        customerService.updateById(customerId, customer);

        return new ResponseEntity(HttpStatus.NO_CONTENT);

    }

    @PostMapping
    public ResponseEntity addCustomer(@RequestBody Customer customer) {

        Customer savedCustomer = customerService.addCustomer(customer);

        HttpHeaders headers = new HttpHeaders();

        headers.add("Location", "/api/v1/customer/" + savedCustomer.getId().toString());

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{customerId}", method = RequestMethod.GET)
    public Customer getCustomerById(@PathVariable("customerId") UUID customerID) {
        return customerService.getCustomerById(customerID);
    }
    @RequestMapping(method = RequestMethod.GET)
    public List<Customer> getGustomersList() {
        return customerService.getCustomers();
    }
}

package com.seweryn.RestMvcProject.controllers;

import com.seweryn.RestMvcProject.model.CustomerDTO;
import com.seweryn.RestMvcProject.services.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
//@RequestMapping("/api/v1/customer")
public class CustomerController {

    public static final String CUSTOMER_PATH = "/api/v1/customer";
    public static final String CUSTOMER_PATH_ID = CUSTOMER_PATH + "/{customerId}";

    private final CustomerService customerService;

    @PatchMapping(CUSTOMER_PATH_ID)
    public ResponseEntity updateCustomerPatchById(@PathVariable UUID customerId, @RequestBody CustomerDTO customer) {

        customerService.updateCustomerPatchById(customerId, customer);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
    @DeleteMapping(CUSTOMER_PATH_ID)
    public ResponseEntity deleteCustomerById(@PathVariable("customerId") UUID id) {

        customerService.deleteById(id);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping(CUSTOMER_PATH_ID)
    public ResponseEntity updateCustomerByID(@PathVariable UUID customerId,@RequestBody CustomerDTO customer) {

        customerService.updateById(customerId, customer);

        return new ResponseEntity(HttpStatus.NO_CONTENT);

    }

    @PostMapping(CUSTOMER_PATH)
    public ResponseEntity addCustomer(@RequestBody CustomerDTO customer) {

        CustomerDTO savedCustomer = customerService.addCustomer(customer);

        HttpHeaders headers = new HttpHeaders();

        headers.add("Location", "/api/v1/customer/" + savedCustomer.getId().toString());

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

//    @RequestMapping(value = "/{customerId}", method = RequestMethod.GET)
    @GetMapping(CUSTOMER_PATH_ID)
    public CustomerDTO getCustomerById(@PathVariable("customerId") UUID customerID) {
        return customerService.getCustomerById(customerID).orElseThrow(NotFoundException::new);
    }
//    @RequestMapping(method = RequestMethod.GET)
    @GetMapping(CUSTOMER_PATH)
    public List<CustomerDTO> getGustomersList() {
        return customerService.getCustomers();
    }
}

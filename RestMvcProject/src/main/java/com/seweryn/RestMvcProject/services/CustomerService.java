package com.seweryn.RestMvcProject.services;

import com.seweryn.RestMvcProject.model.Customer;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface CustomerService {

    Customer getCustomerById(UUID customerId);

    List<Customer> getCustomers();

    Customer addCustomer(Customer customer);

    void updateById(UUID customerId, Customer customer);

    void deleteById(UUID id);

    void updateCustomerPatchById(UUID customerId, Customer customer);
}

package com.seweryn.RestMvcProject.services;

import com.seweryn.RestMvcProject.model.Customer;

import java.util.List;
import java.util.UUID;

public interface CustomerService {

    Customer getCustomerById(UUID customerId);

    List<Customer> getCustomers();
}

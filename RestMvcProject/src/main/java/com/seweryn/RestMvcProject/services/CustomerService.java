package com.seweryn.RestMvcProject.services;

import com.seweryn.RestMvcProject.model.CustomerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {

    Optional<CustomerDTO> getCustomerById(UUID customerId);

    List<CustomerDTO> getCustomers();

    CustomerDTO saveCustomer(CustomerDTO customer);

    Optional<CustomerDTO> updateById(UUID customerId, CustomerDTO customer);

    boolean deleteById(UUID id);

    Optional<CustomerDTO> updateCustomerPatchById(UUID customerId, CustomerDTO customer);
}

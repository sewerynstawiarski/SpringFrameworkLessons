package com.seweryn.RestMvcProject.services;

import com.seweryn.RestMvcProject.Mappers.CustomerMapper;
import com.seweryn.RestMvcProject.model.CustomerDTO;
import com.seweryn.RestMvcProject.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Service
@Primary
@RequiredArgsConstructor
public class CustomerServiceJPA implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    @Override
    public Optional<CustomerDTO> getCustomerById(UUID customerId) {
        return Optional.ofNullable(customerMapper.customerToCustomerDto(customerRepository.findById(customerId)
                .orElse(null)));
    }

    @Override
    public List<CustomerDTO> getCustomers() {
        return customerRepository.findAll().stream().map(customerMapper::customerToCustomerDto).toList();
    }

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customer) {
        return customerMapper.customerToCustomerDto(customerRepository.save(customerMapper.customerDtoToCustomer(customer)));
    }

    @Override
    public void updateById(UUID customerId, CustomerDTO customer) {

    }

    @Override
    public void deleteById(UUID id) {

    }

    @Override
    public void updateCustomerPatchById(UUID customerId, CustomerDTO customer) {

    }
}

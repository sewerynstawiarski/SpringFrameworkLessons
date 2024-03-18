package com.seweryn.reacitvemongo.services;

import com.seweryn.reacitvemongo.model.BeerDTO;
import com.seweryn.reacitvemongo.model.CustomerDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {
    Mono<CustomerDTO> saveCustomer(CustomerDTO customerDTO);

    Mono<CustomerDTO> saveCustomer(Mono<CustomerDTO> customerDTOMono);

    Mono<CustomerDTO> getCustomerById(String customerId);
    Flux<CustomerDTO> listCustomers();
    Mono<CustomerDTO> updateCustomer(String customerId, CustomerDTO customerDTO);

    Mono<CustomerDTO> patchCustomer(String customerId, CustomerDTO customerDTO);

    Mono<Void> deleteCustomerById(String customerId);
    Flux<CustomerDTO> findFirstByCustomerName(String customerName);
}

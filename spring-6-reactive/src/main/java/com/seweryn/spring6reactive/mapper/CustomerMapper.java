package com.seweryn.spring6reactive.mapper;

import com.seweryn.spring6reactive.domain.Customer;
import com.seweryn.spring6reactive.model.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {

    CustomerDTO customerToCustomerDTO(Customer customer);
    Customer customerDTOToCustomer(CustomerDTO customerDTO);
}

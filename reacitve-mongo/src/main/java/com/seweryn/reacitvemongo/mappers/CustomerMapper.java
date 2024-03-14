package com.seweryn.reacitvemongo.mappers;

import com.seweryn.reacitvemongo.domain.Customer;
import com.seweryn.reacitvemongo.model.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {
    Customer customerDtoToCustomer(CustomerDTO customerDTO);
    CustomerDTO customerToCustomerDto(Customer customer);
}

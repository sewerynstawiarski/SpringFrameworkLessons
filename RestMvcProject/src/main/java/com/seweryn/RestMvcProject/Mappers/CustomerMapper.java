package com.seweryn.RestMvcProject.Mappers;

import com.seweryn.RestMvcProject.entities.Customer;
import com.seweryn.RestMvcProject.model.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {

    Customer customerDtoToCustomer (CustomerDTO dto);
    CustomerDTO customerToCustomerDto(Customer customer);
}

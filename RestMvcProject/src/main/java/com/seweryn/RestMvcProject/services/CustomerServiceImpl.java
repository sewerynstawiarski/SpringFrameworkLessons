package com.seweryn.RestMvcProject.services;

import com.seweryn.RestMvcProject.model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    Map<UUID, Customer> customersMap;

    public CustomerServiceImpl() {
        this.customersMap = new HashMap<>();

        Customer customer1 = Customer.builder()
                .id(UUID.randomUUID())
                .customerName("Duncan Idaho")
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        Customer customer2 = Customer.builder()
                .id(UUID.randomUUID())
                .customerName("Taraza")
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        Customer customer3 = Customer.builder()
                .id(UUID.randomUUID())
                .customerName("Odrade")
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        customersMap.put(customer1.getId(), customer1);
        customersMap.put(customer2.getId(), customer2);
        customersMap.put(customer3.getId(), customer3);
    }

    @Override
    public Customer getCustomerById(UUID customerId) {
        return customersMap.get(customerId);
    }

    @Override
    public List<Customer> getCustomers() {
        return new ArrayList<>(customersMap.values());
    }

    @Override
    public Customer addCustomer(Customer customer) {
        Customer savedCustomer = Customer.builder()
                .id(UUID.randomUUID())
                .customerName(customer.getCustomerName())
                .version(customer.getVersion())
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        customersMap.put(savedCustomer.getId(), savedCustomer);
        return savedCustomer;
    }

    @Override
    public void updateById(UUID customerId, Customer customer) {
        Customer existingCustomer = customersMap.get(customerId);

        existingCustomer.setCustomerName(customer.getCustomerName());
        existingCustomer.setLastModifiedDate(LocalDateTime.now());

        customersMap.put(existingCustomer.getId(), existingCustomer); // this is not required here

    }

    @Override
    public void deleteById(UUID id) {

        customersMap.remove(id);

    }

    @Override
    public void updateCustomerPatchById(UUID customerId, Customer customer) {

        Customer existingCustomer = customersMap.get(customerId);

        if (StringUtils.hasText(customer.getCustomerName())) {
            existingCustomer.setCustomerName(customer.getCustomerName());
        }
        if (customer.getVersion() != null) {
            existingCustomer.setVersion(customer.getVersion());
        }

    }
}

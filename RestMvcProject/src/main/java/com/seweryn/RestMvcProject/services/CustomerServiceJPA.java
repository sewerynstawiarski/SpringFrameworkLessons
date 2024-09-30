package com.seweryn.RestMvcProject.services;

import com.seweryn.RestMvcProject.Mappers.CustomerMapper;
import com.seweryn.RestMvcProject.repositories.CustomerRepository;
import com.seweryn.spring_6_restmvc_api.model.CustomerDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class CustomerServiceJPA implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final CacheManager cacheManager;
    @Cacheable(cacheNames = "customerCache")
    @Override
    public Optional<CustomerDTO> getCustomerById(UUID customerId) {
        log.info("getCustomerById in work here");
        return Optional.ofNullable(customerMapper.customerToCustomerDto(customerRepository.findById(customerId)
                .orElse(null)));
    }
    @Cacheable(cacheNames = "customerListCache")
    @Override
    public List<CustomerDTO> getCustomers() {
        log.info("getCustomers method in work");
        return customerRepository.findAll().stream().map(customerMapper::customerToCustomerDto).toList();
    }

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customer) {
        cacheManager.getCache("customerListCache").clear();

        return customerMapper.customerToCustomerDto(customerRepository.save(customerMapper.customerDtoToCustomer(customer)));
    }

    @Override
    public Optional<CustomerDTO> updateById(UUID customerId, CustomerDTO customer) {
        clearCache(customerId);
        AtomicReference<Optional<CustomerDTO>> atomicReference = new AtomicReference<>();

        customerRepository.findById(customerId).ifPresentOrElse(foundCustomer -> {
            foundCustomer.setCustomerName(customer.getCustomerName());
            foundCustomer.setLastModifiedDate(customer.getLastModifiedDate());
            atomicReference.set(Optional.of(customerMapper.customerToCustomerDto(foundCustomer)));

        },
                () -> {
                    atomicReference.set(Optional.empty());
                }

        );
        return atomicReference.get();
    }
    @Override
    public boolean deleteById(UUID id) {
        clearCache(id);
        if (customerRepository.existsById(id)){
            customerRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Optional<CustomerDTO> updateCustomerPatchById(UUID customerId, CustomerDTO customer) {
        clearCache(customerId);
        AtomicReference<Optional<CustomerDTO>> atomicReference = new AtomicReference<>();

        customerRepository.findById(customerId).ifPresentOrElse(foundCustomer -> {
            if (StringUtils.hasText(customer.getCustomerName())){
                foundCustomer.setCustomerName(customer.getCustomerName());
            }
            if (customer.getLastModifiedDate() != null) {
                foundCustomer.setLastModifiedDate(customer.getLastModifiedDate());
            }
                    atomicReference.set(Optional.of(customerMapper.customerToCustomerDto(foundCustomer)));

                },
                () -> {
                    atomicReference.set(Optional.empty());
                }
        );
        return atomicReference.get();
    }
    private void clearCache(UUID customerId) {
        Cache customerCache = cacheManager.getCache("customerCache");
        if (customerCache != null) {
            customerCache.evict(customerId);
        }
     Cache customerListCache = cacheManager.getCache("customerListCache");
        if (customerListCache != null) {
            customerListCache.clear();
        }
    }
}

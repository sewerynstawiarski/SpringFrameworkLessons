package com.seweryn.reacitvemongo.services;

import com.seweryn.reacitvemongo.domain.Customer;
import com.seweryn.reacitvemongo.mappers.CustomerMapper;
import com.seweryn.reacitvemongo.model.BeerDTO;
import com.seweryn.reacitvemongo.model.CustomerDTO;
import com.seweryn.reacitvemongo.respositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
public class CustomerServiceImplTest {
    @Autowired
    CustomerService customerService;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    CustomerRepository customerRepository;

    CustomerDTO customerDTO;

    @BeforeEach
    void setUp() {
        customerDTO = customerMapper.customerToCustomerDto(getTestCustomer());
    }

    @Test
    void testSaveCustomerUseSubscriber() {
        AtomicBoolean atomicBoolean = new AtomicBoolean();
        AtomicReference<CustomerDTO>  atomicDTO = new AtomicReference<>();

        var savedCustomer = customerService.saveCustomer(customerDTO);

        savedCustomer.subscribe(savedDTO -> {
            System.out.println(savedDTO.getId());
            atomicBoolean.set(true);
            atomicDTO.set(savedDTO);
        });
        await().untilTrue(atomicBoolean);

        CustomerDTO persistedCustomer = atomicDTO.get();
        assertThat(persistedCustomer).isNotNull();
        assertThat(persistedCustomer.getId()).isNotNull();
    }

    @Test
    void testSaveCustomerUseBlock() {
        CustomerDTO savedCustomer = customerService.saveCustomer(customerMapper.customerToCustomerDto(getTestCustomer())).block();

        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getId()).isNotNull();
    }

    @Test
    void testUpdateCustomerUseBlock() {
        final String newName = "New Name";
        CustomerDTO savedCustomer = getSavedCustomerDto();
        savedCustomer.setCustomerName(newName);

        CustomerDTO updatedCustomer = customerService.updateCustomer(savedCustomer.getId(), savedCustomer).block();

        CustomerDTO fetchedDTO = customerService.getCustomerById(updatedCustomer.getId()).block();
        assertThat(fetchedDTO.getCustomerName()).isEqualTo(newName);
    }

    @Test
    void testUpdateCustomerUsingReactiveStreams() {
        final String newName = "New Name";

        AtomicReference<CustomerDTO> atomicDTO = new AtomicReference<>();

        customerService.saveCustomer(customerMapper.customerToCustomerDto(getTestCustomer()))
                .map(savedDTO -> {
                    savedDTO.setCustomerName(newName);
                    return savedDTO;
                }).flatMap(updatedDTO -> customerService.updateCustomer(updatedDTO.getId(), updatedDTO))
                .flatMap(savedAndUpdatedDTO -> customerService.getCustomerById(savedAndUpdatedDTO.getId()))
                .subscribe(atomicDTO::set);

        await().until(() -> atomicDTO.get() != null);
        assertThat(atomicDTO.get().getCustomerName()).isEqualTo(newName);
    }

    @Test
    void testDeleteCustomer() {
        CustomerDTO savedCustomer = getSavedCustomerDto();

        customerService.deleteCustomerById(savedCustomer.getId()).block();

        var expectEmptyCustomerMono = customerService.getCustomerById(savedCustomer.getId());

        CustomerDTO emptyCustomer = expectEmptyCustomerMono.block();

        assertThat(emptyCustomer).isNull();
    }

    @Test
    void testFindFirstByCustomerName() {

        CustomerDTO savedCustomer = getSavedCustomerDto();

        AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        customerService.findFirstByCustomerName(savedCustomer.getCustomerName())
       .subscribe(dto -> {
            System.out.println(dto.toString());
            atomicBoolean.set(true);
        });

        await().untilTrue(atomicBoolean);
    }

    public static Customer getTestCustomer() {
        return Customer.builder()
                .customerName("TEST_CUSTOMER")
                .build();
    }
    public CustomerDTO getSavedCustomerDto() {
        return customerService.saveCustomer(customerMapper.customerToCustomerDto(getTestCustomer())).block();
    }

}
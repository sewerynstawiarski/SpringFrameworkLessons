package com.seweryn.reacitvemongo.respositories;

import com.seweryn.reacitvemongo.domain.Beer;
import com.seweryn.reacitvemongo.domain.Customer;
import com.seweryn.reacitvemongo.model.CustomerDTO;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerRepository extends ReactiveMongoRepository<Customer, String> {
    Flux<Customer> findFirstByCustomerName(String customerName);
}

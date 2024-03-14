package com.seweryn.reacitvemongo.respositories;

import com.seweryn.reacitvemongo.domain.Customer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CustomerRepository extends ReactiveMongoRepository<Customer, String> {
}

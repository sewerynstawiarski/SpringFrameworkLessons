package com.seweryn.RestMvcProject.bootstrap;

import com.seweryn.RestMvcProject.model.BeerCSVRecord;
import com.seweryn.RestMvcProject.repositories.BeerRepository;
import com.seweryn.RestMvcProject.repositories.CustomerRepository;
import com.seweryn.RestMvcProject.services.BeerCsvService;
import com.seweryn.RestMvcProject.services.BeerCsvServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@Import(BeerCsvServiceImpl.class)
class BootStrapDataTest {
    @Autowired
    BeerRepository beerRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    BeerCsvService csvService;
    BootStrapData bootStrapData;

    @BeforeEach
    void setUp() {
        bootStrapData = new BootStrapData(beerRepository, customerRepository, csvService);
    }

    @Test
    void run() throws Exception {
        bootStrapData.run(null);

        assertThat(beerRepository.count()).isEqualTo(2413);
        assertThat(customerRepository.count()).isEqualTo(3);
    }
}
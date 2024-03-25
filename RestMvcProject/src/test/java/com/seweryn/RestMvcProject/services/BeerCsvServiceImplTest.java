package com.seweryn.RestMvcProject.services;

import com.seweryn.RestMvcProject.model.BeerCSVRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class BeerCsvServiceImplTest {
    @Autowired
    ResourceLoader resourceLoader;
    BeerCsvService beerCsvService = new BeerCsvServiceImpl();

    @Test
    void convertCSV() throws FileNotFoundException {
        try (var file1 = resourceLoader
                .getResource("classpath:beers.csv").getInputStream()) {

            List<BeerCSVRecord> recs = beerCsvService.convertCSV(file1);

            System.out.println(recs.size());

            assertThat(recs.size()).isGreaterThan(0);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }



    }
}
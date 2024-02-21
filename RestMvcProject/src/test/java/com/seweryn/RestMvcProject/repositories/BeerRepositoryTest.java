package com.seweryn.RestMvcProject.repositories;

import com.seweryn.RestMvcProject.bootstrap.BootStrapData;
import com.seweryn.RestMvcProject.entities.Beer;
import com.seweryn.RestMvcProject.model.BeerDTO;
import com.seweryn.RestMvcProject.model.BeerStyle;
import com.seweryn.RestMvcProject.services.BeerCsvServiceImpl;
import com.seweryn.RestMvcProject.services.BeerService;
import com.seweryn.RestMvcProject.services.BeerServiceImpl;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@Import({BootStrapData.class, BeerCsvServiceImpl.class})
class BeerRepositoryTest {
    @Autowired
    BeerRepository beerRepository;

    @Test
    void testGetBeerListByName() {
        List<Beer> list = beerRepository.findAllByBeerNameIsLikeIgnoreCase("%IPA%");

        assertThat(list.size()).isEqualTo(336);
    }

    @Test
    void testGetBeerListByStyle() {

        List<Beer> listByStyle = beerRepository.findAllByBeerStyle(BeerStyle.IPA);

        assertThat(listByStyle.size()).isEqualTo(547);
    }

    @Test
    void testSaveBeer() {
        Beer savedBeer = beerRepository.save(Beer.builder()
                .beerName("My beer")
                .beerStyle(BeerStyle.GOSE)
                .upc("123453454")
                .price(new BigDecimal("12.99"))
                .build());

        beerRepository.flush(); //without that, operation happens to quickly for validation to kick in

        assertThat(savedBeer).isNotNull();
        assertThat(savedBeer.getId()).isNotNull();

        System.out.println(savedBeer.getId());

    }

    @Test
    void testSaveBeerNameTooLong() {

        assertThrows(ConstraintViolationException.class, () -> {
            Beer savedBeer = beerRepository.save(Beer.builder()
                    .beerName("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx")
                    .beerStyle(BeerStyle.GOSE)
                    .upc("123453454")
                    .price(new BigDecimal("12.99"))
                    .build());

            beerRepository.flush();
                });

    }
}
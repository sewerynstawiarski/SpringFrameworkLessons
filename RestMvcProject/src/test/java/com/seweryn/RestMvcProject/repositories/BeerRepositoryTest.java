package com.seweryn.RestMvcProject.repositories;

import com.seweryn.RestMvcProject.bootstrap.BootStrapData;
import com.seweryn.RestMvcProject.entities.Beer;
import com.seweryn.RestMvcProject.model.BeerStyle;
import com.seweryn.RestMvcProject.services.BeerCsvServiceImpl;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
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
        Page<Beer> list = beerRepository.findAllByBeerNameIsLikeIgnoreCase("%IPA%", null);

        assertThat(list.getContent().size()).isEqualTo(336);
    }

    @Test
    void testGetBeerListByStyle() {

        Page<Beer> listByStyle = beerRepository.findAllByBeerStyle(BeerStyle.IPA, null);

        assertThat(listByStyle.getContent().size()).isEqualTo(547);
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
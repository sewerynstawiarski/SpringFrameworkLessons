package com.seweryn.spring6webclient.client;

import com.seweryn.spring6webclient.model.BeerDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class BeerClientImplTest {
    @Autowired
    BeerClient client;

    @Test
    void listBeers() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        client.listBeers().subscribe(response -> {
            System.out.println(response);
            atomicBoolean.set(true);
        });
        await().untilTrue(atomicBoolean);
    }

    @Test
    void testListBeersMap() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        client.listBeerMap().subscribe(response -> {
            System.out.println(response);
            atomicBoolean.set(true);
        });
        await().untilTrue(atomicBoolean);
    }

    @Test
    void testListBeersJsonNode() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        client.listBeerJsonNode().subscribe(response -> {
            System.out.println(response.toPrettyString());
            atomicBoolean.set(true);
        });
        await().untilTrue(atomicBoolean);
    }

    @Test
    void testListBeersDto() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        client.listBeerDtos().subscribe(dto -> {
            System.out.println(dto.getBeerName());
            atomicBoolean.set(true);
        });
        await().untilTrue(atomicBoolean);
    }

    @Test
    void testGetBeerById() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        client.listBeerDtos()
                .flatMap(dto -> client.getBeerById(dto.getId()))
                .subscribe(DTO -> {
                    System.out.println(DTO.getBeerName());
                    atomicBoolean.set(true);
                });
        await().untilTrue(atomicBoolean);
    }

    @Test
    void testGetBeerByStyle() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        client.getBeerByStyle("Pale Ale")
                .subscribe(dto -> {
                    System.out.println(dto.toString());
                    atomicBoolean.set(true);
                });
        await().untilTrue(atomicBoolean);
    }

    @Test
    void testCreateBeer() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        BeerDTO newDTO = BeerDTO.builder()
                .beerName("Space Dust")
                .beerStyle("IPA")
                .price(BigDecimal.TEN)
                .quantityOnHand(12)
                .upc("123456")
                .build();

        client.createBeer(newDTO)
                .subscribe(dto -> {
                    System.out.println(dto.toString());
                    atomicBoolean.set(true);
                });
        await().untilTrue(atomicBoolean);
    }
    @Test
    void testUpdateBeer() {
        final String newName = "New Name";

        AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        client.listBeerDtos()
                        .next()
                                .doOnNext(beerDTO -> beerDTO.setBeerName(newName))
                                        .flatMap(dto -> client.updateBeer(dto))
                                                .subscribe(byIdDto -> {
                                                    System.out.println(byIdDto.toString());
                                                    atomicBoolean.set(true);
                                                });
        await().untilTrue(atomicBoolean);
    }

    @Test
    void testPatchBeer() {
        final String newName = "New Name - PATCH";
        final String newStyle = "Best Style Ever";

        AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        client.listBeerDtos()
                .next()
                .doOnNext(beerDTO -> {
                    beerDTO.setBeerName(newName);
                    beerDTO.setBeerStyle(newStyle);
                })
                .flatMap(dto -> client.patchBeer(dto))
                .subscribe(byIdDto -> {
                    System.out.println(byIdDto.toString());
                    atomicBoolean.set(true);
                });
        await().untilTrue(atomicBoolean);

    }

    @Test
    void testDeleteBeerById() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        client.listBeerDtos()
                .next()
                .flatMap(dto -> client.deleteBeer(dto.getId()))
                .doOnSuccess(dto -> {
                    atomicBoolean.set(true);
                })
                .subscribe();
        await().untilTrue(atomicBoolean);
    }
}
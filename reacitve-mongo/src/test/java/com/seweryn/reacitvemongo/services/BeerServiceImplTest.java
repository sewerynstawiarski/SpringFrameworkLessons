package com.seweryn.reacitvemongo.services;

import com.seweryn.reacitvemongo.domain.Beer;
import com.seweryn.reacitvemongo.mappers.BeerMapper;
import com.seweryn.reacitvemongo.model.BeerDTO;
import com.seweryn.reacitvemongo.respositories.BeerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
 public class BeerServiceImplTest {
    @Autowired
    BeerService beerService;
    @Autowired
    BeerMapper beerMapper;
    @Autowired
    BeerRepository beerRepository;
    BeerDTO beerDTO;

    @BeforeEach
    void setUp() {
        beerDTO = beerMapper.beerToBeerDto(getTestBeer());
    }

    @Test
    @DisplayName("Test Save Beer Using Subscriber")
    void testSaveBeerUseSubscriber() {

        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        AtomicReference<BeerDTO> atomicDTO = new AtomicReference<>();

        var savedBeer = beerService.saveBeer(beerDTO);

        savedBeer.subscribe(savedDTO -> {
            System.out.println(savedDTO.getId());
            atomicBoolean.set(true);
            atomicDTO.set(savedDTO);
        });
        await().untilTrue(atomicBoolean);

        BeerDTO persistedDTO = atomicDTO.get();
        assertThat(persistedDTO).isNotNull();
        assertThat(persistedDTO.getId()).isNotNull();

    }

    @Test
    @DisplayName("Test Save Beer using BLock")
    void testSaveBeerUseBlock() {
        BeerDTO savedDTO = beerService.saveBeer(beerMapper.beerToBeerDto(getTestBeer())).block();

        assertThat(savedDTO).isNotNull();
        assertThat(savedDTO.getId()).isNotNull();

    }

    public static Beer getTestBeer(){
        return Beer.builder()
                .beerName("Space Dust")
                .beerStyle("IPA")
                .price(BigDecimal.TEN)
                .quantityOnHand(12)
                .upc("123456")
                .build();
    }

    @Test
    @DisplayName("Test Update Beer Using Block")
    void testUpdateBlocking() {
        final String newName = "New Beer Name";
        BeerDTO savedBeerDto = getSavedBeerDto();
        savedBeerDto.setBeerName(newName);

        BeerDTO updatedDTO = beerService.updateBeer(savedBeerDto.getId(), savedBeerDto).block(); // why no update method??

        //verify exist in DB
        BeerDTO fetchedDto = beerService.getBeerById(updatedDTO.getId()).block();
        assertThat(fetchedDto.getBeerName()).isEqualTo(newName);
    }

    public  BeerDTO getSavedBeerDto() {
        return beerService.saveBeer(beerMapper.beerToBeerDto(getTestBeer())).block();
    }

    @Test
    @DisplayName("Test Update Using Reactive Streams")
    void testUpdateUsingReactiveStreams() {
        final String newName = "New Beer Name";

        AtomicReference<BeerDTO> atomicDTO = new AtomicReference<>();

        beerService.saveBeer(beerMapper.beerToBeerDto(getTestBeer()))
                .map(savedBeerDto -> {
                    savedBeerDto.setBeerName(newName);
                    return savedBeerDto;
                })
                .flatMap(updatedDTO -> beerService.updateBeer(updatedDTO.getId(), updatedDTO))
                .flatMap(savedUpdatedDTO -> beerService.getBeerById(savedUpdatedDTO.getId()))
                .subscribe(atomicDTO::set);

        await().until(() -> atomicDTO.get() != null);
        assertThat(atomicDTO.get().getBeerName()).isEqualTo(newName);
    }

    @Test
    void testDeleteBeer() {
        BeerDTO beerToDelete = getSavedBeerDto();

        beerService.deleteBeerById(beerToDelete.getId()).block();

        var expectedEmptyBeerMono =  beerService.getBeerById(beerToDelete.getId());

        BeerDTO emptyBeer = expectedEmptyBeerMono.block();

        assertThat(emptyBeer).isNull();
    }

    @Test
    void testFindFirstByBeerName() {
        BeerDTO beerDTO = getSavedBeerDto();

        AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        Mono<BeerDTO> foundDTO = beerService.findFirstByBeerName(beerDTO.getBeerName());

        foundDTO.subscribe(dto -> {
            System.out.println(dto.toString());
            atomicBoolean.set(true);
        });

        await().untilTrue(atomicBoolean);
    }

    @Test
    void testFindBeerByBeerStyle() {
        BeerDTO beerDTO = getSavedBeerDto();

        AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        beerService.findByBeerStyle(beerDTO.getBeerStyle())
                .subscribe(dto -> {
                    System.out.println(dto.toString());
                    atomicBoolean.set(true);
                });

        await().untilTrue(atomicBoolean);
    }
}
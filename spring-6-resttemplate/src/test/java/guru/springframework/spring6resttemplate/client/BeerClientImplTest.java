package guru.springframework.spring6resttemplate.client;

import guru.springframework.spring6resttemplate.model.BeerDTO;
import guru.springframework.spring6resttemplate.model.BeerStyle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class BeerClientImplTest {
    @Autowired
    BeerClientImpl beerClient;


    @Test
    void getBeerById() {
        Page<BeerDTO> beerDTOS =  beerClient.listBeers();

        BeerDTO dto = beerDTOS.getContent().get(0);

        BeerDTO byId = beerClient.getBeerById(dto.getId());

        assertNotNull(byId);
    }

    @Test
    void testCreateBeer() {

        BeerDTO newDto = BeerDTO.builder()
                .price(new BigDecimal("10.99"))
                .beerName("Mango Bobs")
                .beerStyle(BeerStyle.IPA)
                .quantityOnHand(500)
                .upc("123456")
                .build();

        BeerDTO savedDTO = beerClient.createBeer(newDto);
        assertNotNull(savedDTO);

    }
    @Test
    void testUpdateBeer() {

        BeerDTO newDto = BeerDTO.builder()
                .price(new BigDecimal("10.99"))
                .beerName("Mango Bobs")
                .beerStyle(BeerStyle.IPA)
                .quantityOnHand(500)
                .upc("123456")
                .build();

        BeerDTO savedDTO = beerClient.createBeer(newDto);
        final String newName = " Updated Name";
        savedDTO.setBeerName(newName);
        BeerDTO updatedBeer = beerClient.updateBeer(savedDTO);

        assertEquals(newName, updatedBeer.getBeerName());

        assertNotNull(savedDTO);

    }

    @Test
    void tesDeleteBeer() {
        BeerDTO newDto = BeerDTO.builder()
                .price(new BigDecimal("10.99"))
                .beerName("Mango Bobs")
                .beerStyle(BeerStyle.IPA)
                .quantityOnHand(500)
                .upc("123456")
                .build();

        BeerDTO beerDTO = beerClient.createBeer(newDto);

        beerClient.deleteBeer(beerDTO.getId());

        assertThrows(HttpClientErrorException.class, () -> {
            beerClient.getBeerById(beerDTO.getId());
        });
    }

    @Test
    void listBeers() {

        beerClient.listBeers();
    }

    @Test
    void listBeersNoBeeNAME() {

        beerClient.listBeers(null, null, null, null, null);
    }

    @Test
    void listBeersName() {

        beerClient.listBeers("ALE");
    }
    @Test
    void listBeersStyle() {

        beerClient.listBeers(BeerStyle.IPA);
    }
    @Test
    void listBeersShowInventory() {

        beerClient.listBeers(null, BeerStyle.IPA, true);
    }
    @Test
    void listBeersShowInventoryPageTwo() {

        beerClient.listBeers(null, null, null, 2, null);
    }
    @Test
    void listBeersShowInventoryPageTwoSize50() {

        beerClient.listBeers(null, null, null, null , 50);
    }
}
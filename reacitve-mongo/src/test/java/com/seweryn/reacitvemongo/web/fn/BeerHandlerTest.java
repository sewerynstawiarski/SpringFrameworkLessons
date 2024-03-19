package com.seweryn.reacitvemongo.web.fn;

import com.seweryn.reacitvemongo.domain.Beer;
import com.seweryn.reacitvemongo.model.BeerDTO;
import com.seweryn.reacitvemongo.services.BeerServiceImpl;
import com.seweryn.reacitvemongo.services.BeerServiceImplTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.List;

import static net.bytebuddy.implementation.FixedValue.value;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockOAuth2Login;

@SpringBootTest
@AutoConfigureWebTestClient
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BeerHandlerTest {
    @Autowired
    WebTestClient webTestClient;

    @Test
    void testPatchBeerByIdNotFound() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .patch()
                .uri(BeerRouterConfig.BEER_PATH_ID, 999)
                .body(Mono.just(BeerServiceImplTest.getTestBeer()), BeerDTO.class)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testPatchBeerIdFound() {
        BeerDTO beerDTO = getSavedTestBeer();

        webTestClient
                .mutateWith(mockOAuth2Login())
                .patch()
                .uri(BeerRouterConfig.BEER_PATH_ID, beerDTO.getId())
                .body(Mono.just(BeerServiceImplTest.getTestBeer()), BeerDTO.class)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testDeleteNotFound() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .delete()
                .uri(BeerRouterConfig.BEER_PATH_ID, 999)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(999)
    void testDeleteBeer() {
        BeerDTO beerDTO = getSavedTestBeer();
        webTestClient
                .mutateWith(mockOAuth2Login())
                .delete()
                .uri(BeerRouterConfig.BEER_PATH_ID, beerDTO.getId())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @Order(4)
    void testUpdateBeerBadRequest() {
        BeerDTO beerDTO= getSavedTestBeer();
        beerDTO.setBeerStyle("");

        webTestClient
                .mutateWith(mockOAuth2Login())
                .put()
                .uri(BeerRouterConfig.BEER_PATH_ID, beerDTO.getId())
                .body(Mono.just(beerDTO), BeerDTO.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testUpdateBeerNotFound() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .put()
                .uri(BeerRouterConfig.BEER_PATH_ID, 999)
                .body(Mono.just(BeerServiceImplTest.getTestBeer()), BeerDTO.class)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(3)
    void testUpdateBeer() {
        BeerDTO beerDTO = getSavedTestBeer();
        beerDTO.setBeerName("New");

        webTestClient
                .mutateWith(mockOAuth2Login())
                .put()
                .uri(BeerRouterConfig.BEER_PATH_ID, beerDTO.getId())
                .body(Mono.just(beerDTO), BeerDTO.class)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testCreateBeerBadData() {
        Beer testBeer = BeerServiceImplTest.getTestBeer();
        testBeer.setBeerName("");

        webTestClient
                .mutateWith(mockOAuth2Login())
                .post()
                .uri(BeerRouterConfig.BEER_PATH)
                .body(Mono.just(testBeer), BeerDTO.class)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testCreateBeer() {
        BeerDTO testBeer = getSavedTestBeer();
        webTestClient
                .mutateWith(mockOAuth2Login())
                .post()
                .uri(BeerRouterConfig.BEER_PATH)
                .body(Mono.just(testBeer), BeerDTO.class)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists("location");
    }

    @Test
    void testGetBeerByIdNotFound() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .get()
                .uri(BeerRouterConfig.BEER_PATH_ID, 999)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(1)
    void testGetBeerById() {
        BeerDTO beerDTO = getSavedTestBeer();

        webTestClient
                .mutateWith(mockOAuth2Login())
                .get()
                .uri(BeerRouterConfig.BEER_PATH_ID, beerDTO.getId())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Type", "application/json")
                .expectBody(BeerDTO.class);
    }

    @Test
    void testListBeers() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .get().uri(BeerRouterConfig.BEER_PATH)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Type", "application/json")
                .expectBody().jsonPath("$.size()", value(greaterThan(1)));
    }

    @Test
    void testListBeersByStyle() {
        final String BEER_STYLE = "TEST";
        BeerDTO testDto = getSavedTestBeer();
        testDto.setBeerStyle(BEER_STYLE);

        webTestClient
                .mutateWith(mockOAuth2Login())
                .post()
                .uri(BeerRouterConfig.BEER_PATH)
                .body(Mono.just(testDto), BeerDTO.class)
                .exchange();

        webTestClient
                .mutateWith(mockOAuth2Login())
                .get().uri(UriComponentsBuilder.fromPath(BeerRouterConfig.BEER_PATH)
                .queryParam("beerStyle", BEER_STYLE).build().toUri())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Type", "application/json")
                .expectBody().jsonPath("$.size()").value(equalTo(1));
    }

    public BeerDTO getSavedTestBeer(){
        FluxExchangeResult<BeerDTO> beerDTOFluxExchangeResult = webTestClient
                .mutateWith(mockOAuth2Login())
                .post().uri(BeerRouterConfig.BEER_PATH)
                .body(Mono.just(BeerServiceImplTest.getTestBeer()), BeerDTO.class)
                .header("Content-Type", "application/json")
                .exchange()
                .returnResult(BeerDTO.class);

        List<String> location = beerDTOFluxExchangeResult.getResponseHeaders().get("Location");

        return webTestClient.mutateWith(mockOAuth2Login()).get().uri(BeerRouterConfig.BEER_PATH)
                .exchange().returnResult(BeerDTO.class).getResponseBody().blockFirst();
    }
}
package com.seweryn.reacitvemongo.web.fn;

import com.seweryn.reacitvemongo.domain.Customer;
import com.seweryn.reacitvemongo.model.BeerDTO;
import com.seweryn.reacitvemongo.model.CustomerDTO;
import com.seweryn.reacitvemongo.services.BeerServiceImplTest;
import com.seweryn.reacitvemongo.services.CustomerServiceImplTest;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockOAuth2Login;

@SpringBootTest
@AutoConfigureWebTestClient
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CustomerHandlerTest {
    @Autowired
    WebTestClient webTestClient;

    @Test
    @Order(1)
    void testListCustomers() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .get().uri(CustomerRouterConfig.CUSTOMER_PATH)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectBody().jsonPath("$.size()").isEqualTo(3);
    }

    @Test
    @Order(2)
    void testGetCustomerById() {
        CustomerDTO testCustomer = getSavedTestCustomer();

        webTestClient
                .mutateWith(mockOAuth2Login())
                .get().uri(CustomerRouterConfig.CUSTOMER_PATH_ID, testCustomer.getId())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectBody(CustomerDTO.class);
    }

    @Test
    void testGetCustomerByIdNotFound() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .get().uri(CustomerRouterConfig.CUSTOMER_PATH_ID, 51)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testCreateCustomer() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .post()
                .uri(CustomerRouterConfig.CUSTOMER_PATH)
                .body(Mono.just(CustomerServiceImplTest.getTestCustomer()), CustomerDTO.class)
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists("location");
    }

    @Test
    void testCreateCustomerBadData() {

        Customer testCustomer = CustomerServiceImplTest.getTestCustomer();
        testCustomer.setCustomerName("");

        webTestClient
                .mutateWith(mockOAuth2Login())
                .post()
                .uri(CustomerRouterConfig.CUSTOMER_PATH)
                .body(Mono.just(testCustomer), CustomerDTO.class)
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @Order(3)
    void testUpdateExistingCustomer() {
        CustomerDTO testCustomer = getSavedTestCustomer();
        testCustomer.setCustomerName("Updated");


        webTestClient
                .mutateWith(mockOAuth2Login())
                .put().uri(CustomerRouterConfig.CUSTOMER_PATH_ID, testCustomer.getId())
                .body(Mono.just(CustomerServiceImplTest.getTestCustomer()), CustomerDTO.class)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testUpdateExistingCustomerBadData() {
        Customer testCustomer = CustomerServiceImplTest.getTestCustomer();
        testCustomer.setCustomerName("");

        webTestClient
                .mutateWith(mockOAuth2Login())
                .put().uri(CustomerRouterConfig.CUSTOMER_PATH_ID, 1)
                .body(Mono.just(testCustomer), CustomerDTO.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testUpdateExistingCustomerNotFound() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .put().uri(CustomerRouterConfig.CUSTOMER_PATH_ID, 51)
                .body(Mono.just(CustomerServiceImplTest.getTestCustomer()), CustomerDTO.class)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(99)
    void testDeleteCustomer() {
        CustomerDTO testCustomer = getSavedTestCustomer();

        webTestClient
                .mutateWith(mockOAuth2Login())
                .delete().uri(CustomerRouterConfig.CUSTOMER_PATH_ID, testCustomer.getId())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testDeleteCustomerNotFound() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .delete().uri(CustomerRouterConfig.CUSTOMER_PATH_ID, 51)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(4)
    void testPatchCustomer() {
        CustomerDTO testCustomer = getSavedTestCustomer();

        webTestClient
                .mutateWith(mockOAuth2Login())
                .patch().uri(CustomerRouterConfig.CUSTOMER_PATH_ID, testCustomer.getId())
                .body(Mono.just(CustomerServiceImplTest.getTestCustomer()), CustomerDTO.class)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testPatchCustomerBadData() {
        Customer testCustomer = CustomerServiceImplTest.getTestCustomer();
        testCustomer.setCustomerName("");

        webTestClient
                .mutateWith(mockOAuth2Login())
                .patch().uri(CustomerRouterConfig.CUSTOMER_PATH_ID, 1)
                .body(Mono.just(testCustomer), CustomerDTO.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testPatchCustomerNotFound() {
        Customer testCustomer = CustomerServiceImplTest.getTestCustomer();
        testCustomer.setCustomerName("Villan");

        webTestClient
                .mutateWith(mockOAuth2Login())
                .patch().uri(CustomerRouterConfig.CUSTOMER_PATH_ID, 51)
                .body(Mono.just(testCustomer), CustomerDTO.class)
                .exchange()
                .expectStatus().isNotFound();

    }

    public CustomerDTO getSavedTestCustomer(){
        FluxExchangeResult<CustomerDTO> customerDTOFluxExchangeResult = webTestClient
                .mutateWith(mockOAuth2Login()).post().uri(CustomerRouterConfig.CUSTOMER_PATH)
                .body(Mono.just(CustomerServiceImplTest.getTestCustomer()), CustomerDTO.class)
                .header("Content-Type", "application/json")
                .exchange()
                .returnResult(CustomerDTO.class);

        List<String> location = customerDTOFluxExchangeResult.getResponseHeaders().get("Location");

        return webTestClient.mutateWith(mockOAuth2Login()).get().uri(CustomerRouterConfig.CUSTOMER_PATH)
                .exchange().returnResult(CustomerDTO.class).getResponseBody().blockFirst();
    }

    @Test
    void testGetCustomersByName() {
        final String name = "TESTNAME";
        CustomerDTO testCustomer =getSavedTestCustomer();
        testCustomer.setCustomerName(name);

        webTestClient
                .mutateWith(mockOAuth2Login())
                .post()
                .uri(CustomerRouterConfig.CUSTOMER_PATH)
                .body(Mono.just(testCustomer), CustomerDTO.class)
                .exchange();


        webTestClient
                .mutateWith(mockOAuth2Login())
                .get()
                .uri(UriComponentsBuilder.fromPath(CustomerRouterConfig.CUSTOMER_PATH)
                        .queryParam("customerName", name).build().toUri())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Type", "application/json")
                .expectBody().jsonPath("$.size()").isEqualTo(1);

    }
}
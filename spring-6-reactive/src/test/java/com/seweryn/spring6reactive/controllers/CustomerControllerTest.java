package com.seweryn.spring6reactive.controllers;

import com.seweryn.spring6reactive.domain.Customer;
import com.seweryn.spring6reactive.model.CustomerDTO;
import com.seweryn.spring6reactive.repositories.CustomerRepository;
import com.seweryn.spring6reactive.repositories.CustomerRepositoryTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockOAuth2Login;

@SpringBootTest
@AutoConfigureWebTestClient
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CustomerControllerTest {
    @Autowired
    WebTestClient webTestClient;

    @Test
    @Order(2)
    void testListCustomers() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .get().uri(CustomerController.CUSTOMER_PATH)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectBody().jsonPath("$.size()").isEqualTo(3);
    }

    @Test
    @Order(1)
    void testGetCustomerById() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .get().uri(CustomerController.CUSTOMER_PATH_ID, 1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectBody(CustomerDTO.class);
    }
    @Test
    void testGetCustomerByIdNotFound() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .get().uri(CustomerController.CUSTOMER_PATH_ID, 51)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testCreateCustomer() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .post()
                .uri(CustomerController.CUSTOMER_PATH)
                .body(Mono.just(CustomerRepositoryTest.getCustomer()), CustomerDTO.class)
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().location("http://localhost:8080/api/v2/customer/4");
    }
    @Test
    void testCreateCustomerBadData() {

        Customer testCustomer =  CustomerRepositoryTest.getCustomer();
        testCustomer.setCustomerName("");

        webTestClient
                .mutateWith(mockOAuth2Login())
                .post()
                .uri(CustomerController.CUSTOMER_PATH)
                .body(Mono.just(testCustomer), CustomerDTO.class)
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @Order(3)
    void testUpdateExistingCustomer() {


        webTestClient
                .mutateWith(mockOAuth2Login())
                .put().uri(CustomerController.CUSTOMER_PATH_ID, 1)
                .body(Mono.just(CustomerRepositoryTest.getCustomer()), CustomerDTO.class)
                .exchange()
                .expectStatus().isNoContent();
    }
    @Test
    void testUpdateExistingCustomerBadData() {
        Customer testCustomer =  CustomerRepositoryTest.getCustomer();
        testCustomer.setCustomerName("");

        webTestClient
                .mutateWith(mockOAuth2Login())
                .put().uri(CustomerController.CUSTOMER_PATH_ID, 1)
                .body(Mono.just(testCustomer), CustomerDTO.class)
                .exchange()
                .expectStatus().isBadRequest();
    }
    @Test
    void testUpdateExistingCustomerNotFound() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .put().uri(CustomerController.CUSTOMER_PATH_ID, 51)
                .body(Mono.just(CustomerRepositoryTest.getCustomer()), CustomerDTO.class)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(99)
    void testDeleteCustomer() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .delete().uri(CustomerController.CUSTOMER_PATH_ID, 1)
                .exchange()
                .expectStatus().isNoContent();
    }
    @Test
    void testDeleteCustomerNotFound() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .delete().uri(CustomerController.CUSTOMER_PATH_ID, 51)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(4)
    void testPatchCustomer() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .patch().uri(CustomerController.CUSTOMER_PATH_ID, 1)
                .body(Mono.just(CustomerRepositoryTest.getCustomer()), CustomerDTO.class)
                .exchange()
                .expectStatus().isOk();
    }
    @Test
    void testPatchCustomerBadData() {
        Customer testCustomer =  CustomerRepositoryTest.getCustomer();
        testCustomer.setCustomerName("");

        webTestClient
                .mutateWith(mockOAuth2Login())
                .patch().uri(CustomerController.CUSTOMER_PATH_ID, 1)
                .body(Mono.just(testCustomer), CustomerDTO.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testPatchCustomerNotFound() {
        Customer testCustomer =  CustomerRepositoryTest.getCustomer();
        testCustomer.setCustomerName("Villan");

        webTestClient
                .mutateWith(mockOAuth2Login())
                .patch().uri(CustomerController.CUSTOMER_PATH_ID, 51)
                .body(Mono.just(testCustomer), CustomerDTO.class)
                .exchange()
                .expectStatus().isNotFound();
    }
}
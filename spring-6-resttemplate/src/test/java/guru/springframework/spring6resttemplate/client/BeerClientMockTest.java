package guru.springframework.spring6resttemplate.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.spring6resttemplate.config.RestTemplateBuilderConfig;
import guru.springframework.spring6resttemplate.model.BeerDTO;
import guru.springframework.spring6resttemplate.model.BeerDTOPageImpl;
import guru.springframework.spring6resttemplate.model.BeerStyle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.web.client.MockServerRestTemplateCustomizer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Arrays;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

@RestClientTest
@Import(RestTemplateBuilderConfig.class)
public class BeerClientMockTest {

    static final String URL = "http://localhost:8080";
    BeerClient beerClient;
    MockRestServiceServer server;
    @Autowired
    RestTemplateBuilder restTemplateBuilderConfigured;
    @Autowired
    ObjectMapper objectMapper;
    @Mock
    RestTemplateBuilder mockRestTemplateBuilder =
            new RestTemplateBuilder(new MockServerRestTemplateCustomizer());

    BeerDTO testBeer;
    String testBeerJson;

    @BeforeEach
    void setUp() throws JsonProcessingException {

        RestTemplate restTemplate = restTemplateBuilderConfigured.build();
        server = MockRestServiceServer.bindTo(restTemplate).build();
        when(mockRestTemplateBuilder.build()).thenReturn(restTemplate);
        beerClient = new BeerClientImpl(mockRestTemplateBuilder);

        testBeer = getBeerDto();
        testBeerJson = objectMapper.writeValueAsString(testBeer);
    }

    @Test
    void testListBeers() throws JsonProcessingException {
        String payload = objectMapper.writeValueAsString(getPage());

        server.expect(method(HttpMethod.GET))
                .andExpect(requestTo(URL + BeerClientImpl.GET_BEER_PATH))
                .andRespond(withSuccess(payload, MediaType.APPLICATION_JSON));

        Page<BeerDTO> dtos = beerClient.listBeers();
        assertThat(dtos.getContent().size()).isGreaterThan(0);
    }

    BeerDTO getBeerDto() {
        return   BeerDTO.builder()
                .price(new BigDecimal("10.99"))
                .beerName("Mango Bobs")
                .beerStyle(BeerStyle.IPA)
                .quantityOnHand(500)
                .upc("123456")
                .id(UUID.randomUUID())
                .build();
    }
    BeerDTOPageImpl getPage() {
        return new BeerDTOPageImpl(Arrays.asList(getBeerDto()), 1, 25, 1);
    }

    @Test
    void testGetBeerById() {

        mockGetOperation();

        BeerDTO result = beerClient.getBeerById(testBeer.getId());
        assertThat(result.getId()).isEqualTo(testBeer.getId());
    }

    private void mockGetOperation() {
        server.expect(method(HttpMethod.GET))
                .andExpect(requestToUriTemplate(URL + BeerClientImpl.GET_BEER_BY_ID_PATH, testBeer.getId()))
                .andRespond(withSuccess(testBeerJson, MediaType.APPLICATION_JSON));
    }

    @Test
    void testCreateBeer() {

        URI uri = UriComponentsBuilder.fromPath(BeerClientImpl.GET_BEER_BY_ID_PATH).build(testBeer.getId());

        server.expect(method(HttpMethod.POST))
                        .andExpect(requestTo(URL + BeerClientImpl.GET_BEER_PATH))
                                .andRespond(withAccepted().location(uri));

        mockGetOperation();

        BeerDTO result = beerClient.createBeer(testBeer);
        assertThat(result.getId()).isEqualTo(testBeer.getId());
    }

    @Test
    void testUpdateBeer() {
        server.expect(method(HttpMethod.PUT))
                .andExpect(requestToUriTemplate(URL + BeerClientImpl.GET_BEER_BY_ID_PATH, testBeer.getId()))
                .andRespond(withNoContent());

        mockGetOperation();

        BeerDTO result = beerClient.updateBeer(testBeer);
        assertThat(result.getId()).isEqualTo(testBeer.getId());
    }

    @Test
    void testDeleteBeer() {
        server.expect(method(HttpMethod.DELETE))
                .andExpect(requestToUriTemplate(URL + BeerClientImpl.GET_BEER_BY_ID_PATH, testBeer.getId()))
                .andRespond(withNoContent());

        beerClient.deleteBeer(testBeer.getId());

        server.verify();
    }

    @Test
    void testDeleteNotFound() {
        server.expect(method(HttpMethod.DELETE))
                .andExpect(requestToUriTemplate(URL + BeerClientImpl.GET_BEER_BY_ID_PATH, testBeer.getId()))
                .andRespond(withResourceNotFound());

        assertThrows(HttpClientErrorException.class, () -> {
            beerClient.deleteBeer(testBeer.getId());
        });
        server.verify();
    }

    @Test
    void testListBeersWithQueryParameter() throws JsonProcessingException {
        String response = objectMapper.writeValueAsString(getPage());

        URI uri = UriComponentsBuilder.fromHttpUrl(URL + BeerClientImpl.GET_BEER_PATH)
                .queryParam("beerName", "ALE")
                .build().toUri();

        server.expect(method(HttpMethod.GET))
                .andExpect(requestTo(uri))
                .andExpect(queryParam("beerName", "ALE"))
                .andRespond(withSuccess(response, MediaType.APPLICATION_JSON));

        Page<BeerDTO> responsePage = beerClient.listBeers("ALE");
        assertThat(responsePage.getContent().size()).isEqualTo(1);

    }
}

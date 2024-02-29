package guru.springframework.spring6resttemplate.client;

import guru.springframework.spring6resttemplate.model.BeerDTO;
import guru.springframework.spring6resttemplate.model.BeerDTOPageImpl;
import guru.springframework.spring6resttemplate.model.BeerStyle;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BeerClientImpl implements BeerClient {

//    public static final String BASE_URL = "http://localhost:8080";
    public static final String GET_BEER_PATH = "/api/v1/beer";
    public static final String GET_BEER_BY_ID_PATH = "/api/v1/beer/{beerId}";
    private final RestTemplateBuilder restTemplateBuilder;

    @Override
    public BeerDTO getBeerById(UUID beerId) {

        RestTemplate restTemplate = restTemplateBuilder.build();


        return restTemplate.getForObject(GET_BEER_BY_ID_PATH, BeerDTO.class, beerId);
    }

    @Override
    public BeerDTO createBeer(BeerDTO newDto) {
        RestTemplate restTemplate = restTemplateBuilder.build();

        ResponseEntity<BeerDTO> response = restTemplate.postForEntity(GET_BEER_PATH, newDto, BeerDTO.class);
        URI uri = restTemplate.postForLocation(GET_BEER_PATH, newDto);

        return restTemplate.getForObject(uri.getPath(), BeerDTO.class);
    }
    @Override
    public BeerDTO updateBeer(BeerDTO savedDTO) {

        RestTemplate restTemplate = restTemplateBuilder.build();

        restTemplate.put(GET_BEER_BY_ID_PATH, savedDTO, savedDTO.getId());
        return getBeerById(savedDTO.getId());
    }

    @Override
    public void deleteBeer(UUID beerId) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        restTemplate.delete(GET_BEER_BY_ID_PATH, beerId);
    }


    @Override
    public Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer pageNumber, Integer pageSize) {
        RestTemplate restTemplate = restTemplateBuilder.build();

        // the source API was RestMVC project

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromPath(GET_BEER_PATH);

        if (beerName != null) {
            uriComponentsBuilder.queryParam("beerName", beerName);
        }
        if (beerStyle != null) {
            uriComponentsBuilder.queryParam("beerStyle", beerStyle);
        }
        if (showInventory != null) {
                uriComponentsBuilder.queryParam("showInventory", showInventory);
        }
        if (showInventory ==null) {
            uriComponentsBuilder.queryParam("showInventory", false);
        }
        if (pageNumber != null) {
            uriComponentsBuilder.queryParam("pageNumber", pageNumber);
        }
        if (pageSize != null) {
            uriComponentsBuilder.queryParam("pageSize", pageSize);
        }


        ResponseEntity<BeerDTOPageImpl> jsonResponse = restTemplate
                .getForEntity(uriComponentsBuilder.toUriString(), BeerDTOPageImpl.class);

//        ResponseEntity<Map> mapResponse = restTemplate.getForEntity(BASE_URL + GET_BEER_PATH, Map.class);
//
//        ResponseEntity<JsonNode> jsonResponse = restTemplate.getForEntity(BASE_URL + GET_BEER_PATH, JsonNode.class);
//
//        jsonResponse.getBody().findPath("content").elements()
//                .forEachRemaining(node -> {
//                    System.out.println(node.get("beerName").asText());
//                });
//
//        System.out.println(stringResponse.getBody());

        return jsonResponse.getBody();
    }
    @Override
    public Page<BeerDTO> listBeers() {

        return this.listBeers(null, null, null, null, null);
    }

    @Override
    public Page<BeerDTO> listBeers(String beerName) {
        return this.listBeers(beerName, null, null, null, null);
    }

    @Override
    public Page<BeerDTO> listBeers(BeerStyle beerStyle) {
        return this.listBeers(null, beerStyle, null, null, null);
    }

    @Override
    public Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle) {
        return this.listBeers(beerName, beerStyle, null, null, null);
    }

    @Override
    public Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory) {
        return this.listBeers(beerName, beerStyle, showInventory, null, null);
    }


}

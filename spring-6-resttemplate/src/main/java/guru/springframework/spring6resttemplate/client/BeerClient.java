package guru.springframework.spring6resttemplate.client;

import guru.springframework.spring6resttemplate.model.BeerDTO;
import guru.springframework.spring6resttemplate.model.BeerStyle;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface BeerClient {

    BeerDTO getBeerById(UUID beerId);
    BeerDTO createBeer(BeerDTO newDto);
    Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer pageNumber, Integer pageSize);
    Page<BeerDTO> listBeers();
    Page<BeerDTO> listBeers(String beerName);
    Page<BeerDTO> listBeers(BeerStyle beerStyle);
    Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle);
    Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory);

    BeerDTO updateBeer(BeerDTO savedDTO);

    void deleteBeer(UUID beerId);
}

package com.seweryn.spring6reactive;

import com.seweryn.spring6reactive.domain.Beer;
import com.seweryn.spring6reactive.mapper.BeerMapper;
import com.seweryn.spring6reactive.model.BeerDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class Spring6ReactiveApplicationTests {
	@Autowired
	BeerMapper beerMapper;

	@Test
	void contextLoads() {
	}

	@Test
	void testMapStruct() {

		Beer beer = Beer.builder().id(1).build();

		BeerDTO beerDTO =  beerMapper.beerToBeerDTO(beer);

        assertNotNull(beerDTO);
	}
}

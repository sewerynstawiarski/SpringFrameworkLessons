package com.seweryn.RestMvcProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class RestMvcProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestMvcProjectApplication.class, args);
	}

}

package com.seweryn.spring6reactive.boostrap;

import com.seweryn.spring6reactive.domain.Beer;
import com.seweryn.spring6reactive.domain.Customer;
import com.seweryn.spring6reactive.repositories.BeerRepository;
import com.seweryn.spring6reactive.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.swing.plaf.IconUIResource;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class BootStrapData implements CommandLineRunner {

    private final BeerRepository beerRepository;
    private final CustomerRepository customerRepository;

    @Override
    public void run(String... args) throws Exception {
    loadBeerData();
    loadCustomerData();

    beerRepository.count().subscribe(count -> {
        System.out.println("Count is: " + count);
    });
    customerRepository.count().subscribe(count -> {
        System.out.println("Customer count is: " + count);
    });
    }
    private void loadBeerData() {
        beerRepository.count().subscribe(count -> {
            if (count == 0) {
                Beer beer1 = Beer.builder()
                        .beerName("Bojan Forester")
                        .beerStyle("Pale Ale")
                        .upc("23456")
                        .price(new BigDecimal("5.99"))
                        .quantityOnHand(90)
                        .createdDate(LocalDateTime.now())
                        .lastModifiedDate(LocalDateTime.now())
                        .build();

                Beer beer2 = Beer.builder()
                        .beerName("Bojan Policeman")
                        .beerStyle("Porter")
                        .upc("34567")
                        .price(new BigDecimal("6.99"))
                        .quantityOnHand(40)
                        .createdDate(LocalDateTime.now())
                        .lastModifiedDate(LocalDateTime.now())
                        .build();

                Beer beer3 = Beer.builder()
                        .beerName("Bojan Doctor")
                        .beerStyle("Pilsner")
                        .upc("34567")
                        .price(new BigDecimal("6.99"))
                        .quantityOnHand(40)
                        .createdDate(LocalDateTime.now())
                        .lastModifiedDate(LocalDateTime.now())
                        .build();

                beerRepository.save(beer1).subscribe();
                beerRepository.save(beer2).subscribe();
                beerRepository.save(beer3).subscribe();
            }
        });
    }
    private void loadCustomerData() {
        customerRepository.count().subscribe(count -> {
            if (count == 0) {
                Customer customer1 = Customer.builder()
                        .customerName("Duncan Idaho")
                        .createdDate(LocalDateTime.now())
                        .lastModifiedDate(LocalDateTime.now())
                        .build();

                Customer customer2 = Customer.builder()
                        .customerName("Taraza")
                        .createdDate(LocalDateTime.now())
                        .lastModifiedDate(LocalDateTime.now())
                        .build();

                Customer customer3 = Customer.builder()
                        .customerName("Odrade")
                        .createdDate(LocalDateTime.now())
                        .lastModifiedDate(LocalDateTime.now())
                        .build();

                customerRepository.save(customer1).subscribe();
                customerRepository.save(customer2).subscribe();
                customerRepository.save(customer3).subscribe();
            }
        });
    }
}

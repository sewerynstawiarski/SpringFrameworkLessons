package com.seweryn.spring6reactiveexamples.repositories;

import com.seweryn.spring6reactiveexamples.domain.Person;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PersonRepositoryImplTest {

    PersonRepository personRepository = new PersonRepositoryImpl();

    @Test
    void testMonoByIdBlock() {

        Mono<Person> personMono = personRepository.getById(1);

        Person person = personMono.block();

        System.out.println(person.toString());
    }

    @Test
    void testGetByIDSubscriber() {
        Mono<Person> personMono = personRepository.getById(1);

        personMono.subscribe(person -> {
            System.out.println(person.toString());
        });
    }

    @Test
    void testMapOperation() {
        Mono<Person> personMono = personRepository.getById(1);

        personMono.map(Person::getFirstName).subscribe(System.out::println);
        //subscriber makes back pressure, it's needed
    }

    @Test
    void testFluxBlock() {
        Flux<Person> personFlux = personRepository.findAll();

        Person person = personFlux.blockFirst();

        System.out.println(person.toString());
    }

    @Test
    void testFluxSubscriber() {
        Flux<Person> personFlux = personRepository.findAll();

        personFlux.subscribe(person -> {
            System.out.println(person.toString());
        });
    }

    @Test
    void testFluxMap() {
        Flux<Person> personFlux = personRepository.findAll();

        personFlux.map(Person::getFirstName).subscribe(System.out::println);
    }

    @Test
    void testFluxToList() {
        Flux<Person> personFlux = personRepository.findAll();
        var listMono = personFlux.collectList();

        listMono.subscribe(list -> {
            list.forEach(person -> {
                System.out.println(person.getFirstName());
            });
        });
    }

    @Test
    void testFilterOnName() {
        personRepository.findAll()
                .filter(person -> person.getFirstName().equals("Fiona"))
                .subscribe(person -> System.out.println(person.getFirstName()));
    }

    @Test
    void testGetById() {
        Mono<Person> fionaMono = personRepository.findAll()
                .filter(person -> person.getFirstName().equals("Fiona")).next();


        fionaMono.subscribe(person -> System.out.println(person.getFirstName()));
    }

    @Test
    void testFindPersonByIdNotFound() {
        Flux<Person> personFlux = personRepository.findAll();

        final Integer id = 8;

        Mono<Person> personMono = personFlux.filter(person -> person.getId() == id).single()
                .doOnError(throwable -> {
                    System.out.println("Error occurred in Flux");
                    System.out.println(throwable.toString());
                });

        personMono.subscribe(person -> {
            System.out.println(person.toString());
        }, throwable -> {
            System.out.println("Error occurred in the Mono");
            System.out.println(throwable.toString());
        });
    }

    @Test
    void testPersonById() {
        Flux<Person> personFlux = personRepository.findAll();
        final Integer id = 4;


        Mono<Person> personMonoGood = personRepository.findAll().filter(person -> person.getId() == id).next();

        Mono<Person> personMonoTest = personRepository.getById(id);

        personMonoGood.subscribe(person -> {
            System.out.println(person.getFirstName() + " " + person.getId());
        });
        personMonoTest.subscribe(person -> {
            System.out.println(person.getFirstName() + " " + person.getId());
        });
    }

    @Test
    void testGetByIdFound() {
        Mono<Person> personMonoTest = personRepository.getById(3);

        assertTrue(personMonoTest.hasElement().block());
    }
    @Test
    void testGetByIdNotFound() {
        Mono<Person> personMonoTest = personRepository.getById(6);

        assertFalse(personMonoTest.hasElement().block());
    }
    @Test
    void testGetByIdFoundStepVerifier() {
        Mono<Person> personMonoTest = personRepository.getById(3);

        StepVerifier.create(personMonoTest).expectNextCount(1).verifyComplete();

        personMonoTest.subscribe(person -> {
            System.out.println(person.getFirstName());
        });
    }
    @Test
    void testGetByIdNotFoundStepVerifier() {
        Mono<Person> personMonoTest = personRepository.getById(6);

        StepVerifier.create(personMonoTest).expectNextCount(0).verifyComplete();

        personMonoTest.subscribe(person -> {
            System.out.println(person.getFirstName());
        });
    }
}
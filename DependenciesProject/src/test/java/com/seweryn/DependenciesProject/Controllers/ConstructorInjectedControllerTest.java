package com.seweryn.DependenciesProject.Controllers;

import com.seweryn.DependenciesProject.Services.GreetingService;
import com.seweryn.DependenciesProject.Services.GreetingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ConstructorInjectedControllerTest {
    @Autowired
    ConstructorInjectedController constructorInjectedController;

//    @BeforeEach
//    void setUp() {
//        constructorInjectedController  =  new ConstructorInjectedController(new GreetingServiceImpl());
//
//    }

    @Test
    void sayHello() {
        System.out.println(constructorInjectedController.sayHello());
    }
}
package com.seweryn.DependenciesProject.Controllers;

import com.seweryn.DependenciesProject.Services.GreetingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class PropertyInjectionControllerTest {
    @Autowired
    PropertyInjectionController propertyInjectionController;

//    @BeforeEach
//    void setUp() {
//        propertyInjectionController = new PropertyInjectionController();
//        propertyInjectionController.greetingService = new GreetingServiceImpl();
//    }

    @Test
    void sayHell() {
        System.out.println(propertyInjectionController.sayHello());
    }
}
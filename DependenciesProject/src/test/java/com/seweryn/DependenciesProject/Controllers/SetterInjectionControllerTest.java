package com.seweryn.DependenciesProject.Controllers;

import com.seweryn.DependenciesProject.Services.GreetingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class SetterInjectionControllerTest {
    @Autowired
    SetterInjectionController setterInjectionController;

//    @BeforeEach
//    void setUp() {
//        setterInjectionController = new SetterInjectionController();
//        setterInjectionController.setGreetingService(new GreetingServiceImpl()); // without that injection = NullPointerEWxception
//    }

    @Test
    void sayHello() {
        System.out.println(setterInjectionController.sayHello());
    }
}
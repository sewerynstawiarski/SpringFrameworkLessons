package com.seweryn.DependenciesProject.Controllers;

import com.seweryn.DependenciesProject.Services.GreetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

@Controller
public class SetterInjectionController {

    private GreetingService greetingService;

    @Qualifier("setterGreetingBean")
    @Autowired
    public void setGreetingService( GreetingService greetingService) {
        System.out.println("SetterInjectionController.setGreetingService");
        this.greetingService = greetingService;
    }
    public String sayHello() {
       return greetingService.sayGreeting();
    }
}

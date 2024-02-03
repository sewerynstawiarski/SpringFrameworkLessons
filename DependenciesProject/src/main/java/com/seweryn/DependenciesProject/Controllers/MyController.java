package com.seweryn.DependenciesProject.Controllers;

import com.seweryn.DependenciesProject.Services.GreetingService;
import com.seweryn.DependenciesProject.Services.GreetingServiceImpl;
import org.springframework.stereotype.Controller;

@Controller
public class MyController {
    private final GreetingService greetingService;

    public MyController() {
        this.greetingService = new GreetingServiceImpl();
    }

    public String sayHello(){
        System.out.println("Controller! Here!");
        return greetingService.sayGreeting();
    }
    public void beforeInit(){
        System.out.println("## - Before Init - Called by Bean Post Processor");
    }

    public void afterInit(){
        System.out.println("## - After init called by Bean Post Processor");
    }
}

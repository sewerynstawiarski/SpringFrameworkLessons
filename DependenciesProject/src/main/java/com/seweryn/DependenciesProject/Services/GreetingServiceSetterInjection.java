package com.seweryn.DependenciesProject.Services;

import org.springframework.stereotype.Service;

@Service("setterGreetingBean")
public class GreetingServiceSetterInjection implements GreetingService {
    @Override
    public String sayGreeting() {
        return "Hello! Setting a Greeting!";
    }
}

package com.seweryn.DependenciesProject.Services;

import org.springframework.stereotype.Service;

@Service("propertyGreetingService")
public class GreetingServiceProperty implements GreetingService{
    @Override
    public String sayGreeting() {
        return "Friends don't let friends to property injection! Really?";
    }
}

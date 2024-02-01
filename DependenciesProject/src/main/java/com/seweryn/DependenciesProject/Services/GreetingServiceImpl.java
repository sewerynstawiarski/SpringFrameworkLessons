package com.seweryn.DependenciesProject.Services;

public class GreetingServiceImpl implements GreetingService {

    @Override
    public String sayGreeting() {
        return "Hello! From Base Service!";
    }
}

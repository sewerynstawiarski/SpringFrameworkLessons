package com.seweryn.DependenciesProject.Services.i18n;

import com.seweryn.DependenciesProject.Services.GreetingService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
@Profile("ES")
@Service("i18nService")
public class SpanishGreetingServices implements GreetingService {
    @Override
    public String sayGreeting() {
        return "Hola Mundo - ES";
    }
}

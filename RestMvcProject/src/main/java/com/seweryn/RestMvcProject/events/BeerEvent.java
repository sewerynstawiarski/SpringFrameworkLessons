package com.seweryn.RestMvcProject.events;

import com.seweryn.RestMvcProject.entities.Beer;
import org.springframework.security.core.Authentication;

public interface BeerEvent {
    Beer getBeer();
    Authentication getAuthentication();
}

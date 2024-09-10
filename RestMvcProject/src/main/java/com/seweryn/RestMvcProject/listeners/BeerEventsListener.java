package com.seweryn.RestMvcProject.listeners;

import com.seweryn.RestMvcProject.Mappers.BeerMapper;
import com.seweryn.RestMvcProject.events.*;
import com.seweryn.RestMvcProject.repositories.BeerAuditRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
@RequiredArgsConstructor
@Component
@Slf4j
public class BeerEventsListener {
    private final BeerMapper beerMapper;
    private final BeerAuditRepository beerAuditRepository;
    @Async
    @EventListener
    public void  listen(BeerEvent event) {
        val beerAudit = beerMapper.beerToBeerAudit(event.getBeer());

        String eventType = null;

        switch (event) {
            case BeerCreatedEvent beerCreatedEvent -> eventType = "BEER_CREATED";
            case BeerPatchEvent beerPatchEvent -> eventType = "BEER_PATCHED";
            case BeerUpdatedEvent beerUpdatedEvent -> eventType = "BEER_UPDATED";
            case BeerDeletedEvent beerDeletedEvent -> eventType = "BEER_DELETED";
            default -> eventType = "UNKNOWN";
        }

        if (event.getAuthentication() != null && event.getAuthentication().getName() != null) {
            beerAudit.setPrincipalName(event.getAuthentication().getName());
        }

        val savedBeerAudit = beerAuditRepository.save(beerAudit);
        log.debug("Beer Audit Saved: " + eventType + " " + beerAudit.getAuditId());
    }
}

package com.seweryn.RestMvcProject.listeners;

import com.seweryn.spring_6_restmvc_api.events.OrderPlacedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class OrderPlacedListener {
    @Async
    @EventListener
    public void listen(OrderPlacedEvent event) {
        // will have to add send to Kafka later on
        System.out.println("Order Placed Event Received");
    }
}

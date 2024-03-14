package com.seweryn.reacitvemongo.config;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;

@Configuration
public class MongoConfig extends AbstractReactiveMongoConfiguration {
    public MongoClient mongoClient() {
        return MongoClients.create();
    }
    @Override
    protected String getDatabaseName() {
        return "sfg";
    }
}

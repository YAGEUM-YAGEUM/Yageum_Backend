package com.yageum.fintech.global.config.datasource;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/** 동적 쿼리 문제를 해결 위해 MongoRepository 상속 대신 MongoTemplate 사용 **/

@Configuration
@RequiredArgsConstructor
@EnableMongoRepositories(basePackages = "com.yageum.fintech.domain.chat.infrastructure.repository")
public class MongoConfig {

    private final MongoProperties mongoProperties;

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create(mongoProperties.getClient());
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), mongoProperties.getName());
    }
}

package com.yageum.fintech.domain.chat.config.mongodb;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "mongodb")
public class MongoProperties {
    String client;
    String name;
}

package com.yageum.fintech.domain.tenant.service;

import java.time.Duration;
import java.util.Map;

public interface RedisService {

    void setValues(String key, String data);

    void setValues(String key, String data, Duration duration);

    String getValues(String key);

    void deleteValues(String key);

    void expireValues(String key, int timeout);

    void setHashOps(String key, Map<String, String> data);

    String getHashOps(String key, String hashKey);

    void deleteHashOps(String key, String hashKey);

    boolean checkExistsValue(String value);
}


package com.zsf.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RedisDao {

    @Autowired
    private RedisTemplate redisTemplate;

    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public String getGroup(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    public void saveGroupedElement(String key, String data) {
        redisTemplate.opsForValue().set(key,
                data);
    }

    public String getValue(String key) {
        return (String)redisTemplate.opsForValue().get(key);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }
}


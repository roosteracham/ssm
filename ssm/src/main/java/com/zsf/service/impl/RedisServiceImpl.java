package com.zsf.service.impl;

import com.zsf.domain.*;
import com.zsf.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void saveGroupedElement(GroupedElement groupedElement) {
        redisTemplate.opsForValue().set(groupedElement.getGroupName(),
                groupedElement.getData());
    }

    public String getValue(String key) {
        return (String)redisTemplate.opsForValue().get(key);
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }
}

package com.zsf.dao;

import com.zsf.domain.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.TimeUnit;

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

    public void setExpired(String key, String value, long time, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, time, unit);
    }

    public long lpush(String key, String value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }

    public List<UserInfo> rpop(String key, int timeout, TimeUnit unit) {
        return (List<UserInfo>)redisTemplate.opsForList().rightPop(key, timeout, unit);
    }
}


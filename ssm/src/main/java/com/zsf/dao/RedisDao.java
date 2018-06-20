package com.zsf.dao;

import com.zsf.domain.SVGDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RedisDao {

    @Autowired
    private RedisTemplate redisTemplate;

    public void insert(SVGDto svgDto) {
        String key = svgDto.getProjectName() + "_" + svgDto.getSvgName();
        redisTemplate.opsForValue().set(key, svgDto.getSvg());
    }

    public String getVlaue(SVGDto svgDto) {
        String key = svgDto.getProjectName() + "_" + svgDto.getSvgName();
        return (String)redisTemplate.opsForValue().get(key);
    }
}

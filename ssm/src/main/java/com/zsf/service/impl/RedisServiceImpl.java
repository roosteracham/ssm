package com.zsf.service.impl;

import com.zsf.dao.RedisDao;
import com.zsf.domain.SVGDto;
import com.zsf.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisDao redisDao;

    public void set(SVGDto svgDto) {
        redisDao.insert(svgDto);
    }

    public String getValue(SVGDto svgDto) {
        return redisDao.getVlaue(svgDto);
    }

    public void delete(SVGDto svgDto) {
        redisDao.delete(svgDto);
    }
}

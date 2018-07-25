package com.zsf.service.impl;

import com.zsf.dao.RedisDao;
import com.zsf.domain.*;
import com.zsf.service.RedisService;
import com.zsf.util.errorcode.ErrorCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisDao redisDao;

    public ResBody set(SVGDto svgDto) {
        String key = svgDto.getProjectName() + "_" +
                svgDto.getSvgName() + "_" + svgDto.getIndex();
        String value = svgDto.getSvg();
        redisDao.set(key, value);
        // 设置返回参数
        ResBody body = new ResBody();
        body.setSuccess(true);
        body.setErrorCode(ErrorCodeEnum.SUC.getIndex());
        body.setData("");
        return body;
    }

    public ResBody getValue(SVGDto svgDto) {

        String key = svgDto.getProjectName() + "_" + svgDto.getSvgName()
                + "_" + svgDto.getIndex();
        String value =  redisDao.getValue(key);

        ResBody body = new ResBody();
        body.setSuccess(true);
        body.setErrorCode(ErrorCodeEnum.SUC.getIndex());
        body.setData(value);
        return body;
    }

    public ResBody delete(SVGDto svgDto) {

        String key = svgDto.getProjectName() + "_" + svgDto.getSvgName()
                + "_" + svgDto.getIndex();
        redisDao.delete(key);

        ResBody body = new ResBody();
        body.setSuccess(true);
        body.setErrorCode(ErrorCodeEnum.SUC.getIndex());
        body.setData("");
        return body;
    }

    public ResBody getGroupedElement(GroupedElement group) {
        String key = "group_" + group.getGroupName();
        String data = redisDao.getGroup(key);
        ResBody body = new ResBody();
        body.setSuccess(true);
        body.setErrorCode(ErrorCodeEnum.SUC.getIndex());
        body.setData(data);
        return body;
    }
}

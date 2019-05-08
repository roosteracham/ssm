package com.zsf.service.impl;

import com.zsf.dao.RedisDao;
import com.zsf.domain.*;
import com.zsf.service.RedisService;
import com.zsf.util.errorcode.ErrorCodeEnum;
import com.zsf.util.mapper.ProjectInfoMapper;
import com.zsf.util.mapper.UserSvgsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisDao redisDao;

    @Autowired
    private UserSvgsMapper userSvgsMapper;

    @Autowired
    private ProjectInfoMapper projectInfoMapper;

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

    @Override
    public void delete(UserInfo userInfo) {
        redisDao.delete(userInfo.getId() + "");
    }

    @Override
    public String getValue(String key) {
        return redisDao.getValue(key);
    }

    public ResBody getValue(SVGDto svgDto) {

        String key = svgDto.getProjectName() + "_" + svgDto.getSvgName()
                + "_" + svgDto.getIndex();
        String value =  getValue(key);

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

    @Override
    public void setExpired(String key, String value, long time, TimeUnit unit) {
        redisDao.setExpired(key, value, time, unit);
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

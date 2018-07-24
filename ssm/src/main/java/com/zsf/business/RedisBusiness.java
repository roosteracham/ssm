package com.zsf.business;

import com.zsf.domain.*;
import com.zsf.service.ISvgService;
import com.zsf.service.RedisService;
import com.zsf.util.errorcode.ErrorCodeEnum;
import com.zsf.util.errorcode.ProjectEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional
public class RedisBusiness {

    @Autowired
    private RedisService redisService;

    public ResBody set(SVGDto svgDto) {
        String key = svgDto.getProjectName() + "_" +
                svgDto.getSvgName() + "_" + svgDto.getIndex();
        String value = svgDto.getSvg();
        redisService.set(key, value);
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
        String value =  redisService.getValue(key);

        ResBody body = new ResBody();
        body.setSuccess(true);
        body.setErrorCode(ErrorCodeEnum.SUC.getIndex());
        body.setData(value);
        return body;
    }

    public ResBody delete(SVGDto svgDto) {

        String key = svgDto.getProjectName() + "_" + svgDto.getSvgName()
                + "_" + svgDto.getIndex();
        redisService.delete(key);

        ResBody body = new ResBody();
        body.setSuccess(true);
        body.setErrorCode(ErrorCodeEnum.SUC.getIndex());
        body.setData("");
        return body;
    }

    public ResBody getGroupedElement(GroupedElement group) {
        String key = "group_" + group.getGroupName();
        String data = redisService.getGroup(key);
        ResBody body = new ResBody();
        body.setSuccess(true);
        body.setErrorCode(ErrorCodeEnum.SUC.getIndex());
        body.setData(data);
        return body;
    }
}

package com.zsf.business;

import com.zsf.domain.*;
import com.zsf.service.ISvgService;
import com.zsf.service.RedisService;
import com.zsf.util.errorcode.ErrorCodeEnum;
import com.zsf.util.errorcode.ProjectEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RedisBusiness {

    @Autowired
    private RedisService redisService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ISvgService svgService;

    public ResBody getProjectsColletion(ProjectTypeDto types) {

        // 从redis获得集合数据
        String projects = getAllColletion(types);

        // 设置返回参数
        ResBody body = new ResBody();
        body.setSuccess(true);
        body.setErrorCode(ErrorCodeEnum.SUC.getIndex());
        body.setData(projects);
        return body;
    }

    private String getAllColletion(ProjectTypeDto types) {

        // 拼接成json格式字符串
        StringBuilder result = new StringBuilder("{");
        for (int i = 0; i < types.getTypes().length; i++) {
            String key = ProjectEnum.getProject(types.getTypes()[i]);
            List<String> projects =  redisTemplate.opsForList().range(key,
                    0, -1);
            if (projects == null || projects.size() == 0)
                continue;
            result.append("\"")
                    .append(types.getTypes()[i])
                    .append("\":[");
            for (int j = projects.size() - 1; j >= 0; j--) {
                result.append(projects.get(j));
                if (j != 0) {
                    result.append(",");
                }
            }
            result.append("]");
            if (i != types.getTypes().length - 1)
                result.append(",");
        }
        result.append("}");
        return result.toString();
    }

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

        String key = svgDto.getProjectName() + "_" + svgDto.getSvgName() + "_" + svgDto.getIndex();
        String value =  redisService.getValue(key);

        ResBody body = new ResBody();
        body.setSuccess(true);
        body.setErrorCode(ErrorCodeEnum.SUC.getIndex());
        body.setData(value);
        return body;
    }

    public ResBody delete(SVGDto svgDto) {

        String key = svgDto.getProjectName() + "_" + svgDto.getSvgName() + "_" + svgDto.getIndex();
        redisService.delete(key);

        ResBody body = new ResBody();
        body.setSuccess(true);
        body.setErrorCode(ErrorCodeEnum.SUC.getIndex());
        body.setData("");
        return body;
    }

    public ResBody saveGroupedElement(GroupedElement groupedElement) {
        redisService.saveGroupedElement(groupedElement);
        ResBody body = new ResBody();
        body.setSuccess(true);
        body.setErrorCode(ErrorCodeEnum.SUC.getIndex());
        body.setData("");
        return body;
    }
}

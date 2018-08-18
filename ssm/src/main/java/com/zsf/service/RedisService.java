package com.zsf.service;

import com.zsf.domain.*;

import java.util.concurrent.TimeUnit;

public interface RedisService {
    ResBody set(SVGDto svgDto);

    ResBody getValue(SVGDto svgDto);

    ResBody delete(SVGDto svgDto);

    ResBody getGroupedElement(GroupedElement group);

    void setExpired(String key, String value, long time, TimeUnit unit);

    String getValue(String token);
}

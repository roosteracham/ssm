package com.zsf.service;

import com.zsf.domain.SVGDto;

public interface RedisService {
    void set(SVGDto svgDto);

    String getValue(SVGDto svgDto);
}

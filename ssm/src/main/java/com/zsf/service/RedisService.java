package com.zsf.service;

import com.zsf.domain.*;

public interface RedisService {
    ResBody set(SVGDto svgDto);

    ResBody getValue(SVGDto svgDto);

    ResBody delete(SVGDto svgDto);

    ResBody getGroupedElement(GroupedElement group);
}

package com.zsf.service;

import com.zsf.domain.*;

public interface RedisService {
    void set(String key, String value);

    String getValue(String svgDto);

    void delete(String svgDto);

    void saveGroupedElement(GroupedElement groupedElement);
}

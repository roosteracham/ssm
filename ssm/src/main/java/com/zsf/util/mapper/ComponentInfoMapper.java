package com.zsf.util.mapper;

import com.zsf.domain.ComponentInfo;

import java.util.List;

public interface ComponentInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ComponentInfo record);

    int insertSelective(ComponentInfo record);

    ComponentInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ComponentInfo record);

    int updateByPrimaryKey(ComponentInfo record);

    List<ComponentInfo> getAllComponents();

    ComponentInfo getComponentByName(String name);
}
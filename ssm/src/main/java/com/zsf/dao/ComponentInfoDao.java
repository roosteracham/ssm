package com.zsf.dao;

import com.zsf.domain.ComponentInfo;
import com.zsf.util.mapper.ComponentInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ComponentInfoDao {

    @Autowired
    private ComponentInfoMapper componentInfoMapper;

    public void insert(ComponentInfo componentInfo) {
        componentInfoMapper.insert(componentInfo);
    }

    public void updateByPrimaryId(ComponentInfo componentInfo) {
        componentInfoMapper.updateByPrimaryKey(componentInfo);
    }

    public List<ComponentInfo> getAllComponents() {
        return componentInfoMapper.getAllComponents();
    }

    public ComponentInfo getComponentByName(String name) {
        return componentInfoMapper.getComponentByName(name);
    }
}


package com.zsf.service.impl;

import com.zsf.domain.ComponentInfo;
import com.zsf.mapper.ComponentInfoMapper;
import com.zsf.service.IComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComponentServiceImpl implements IComponentService {

    @Autowired
    private ComponentInfoMapper componentInfoMapper;

    @Override
    public void insert(ComponentInfo componentInfo) {
        componentInfoMapper.insert(componentInfo);
    }

    @Override
    public void updateByPrimaryId(ComponentInfo componentInfo) {
        componentInfoMapper.updateByPrimaryKey(componentInfo);
    }

    @Override
    public List<ComponentInfo> getAllComponents() {
        return componentInfoMapper.getAllComponents();
    }

    @Override
    public ComponentInfo getComponentByName(String name) {
        return componentInfoMapper.getComponentByName(name);
    }
}

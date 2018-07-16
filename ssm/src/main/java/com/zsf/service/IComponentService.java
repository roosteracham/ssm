package com.zsf.service;

import com.zsf.domain.ComponentInfo;

import java.util.List;

public interface IComponentService {

    ComponentInfo getComponentByName(String name);

    List<ComponentInfo> getAllComponents();

    void updateByPrimaryId(ComponentInfo componentInfo);

    void insert(ComponentInfo componentInfo);
}

package com.zsf.util.mapper;

import com.zsf.domain.Menu;

import java.util.List;

public interface MenuMapper {
    int deleteByPrimaryKey(String id);

    int insert(Menu record);

    int insertSelective(Menu record);

    Menu selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Menu record);

    int updateByPrimaryKey(Menu record);

    List<Menu> queryAll();

    List<Menu> queryByRoleId(String roleid);
}
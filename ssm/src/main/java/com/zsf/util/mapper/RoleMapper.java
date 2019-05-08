package com.zsf.util.mapper;

import com.zsf.domain.Role;

import java.util.List;

public interface RoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Role record);

    int insertSelective(Role record);

    Role selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);

    List<Role> queryAll();

    List<Role> queryByUserId(int userId);

    int add(Role role);

    int addRoleMenu(Integer id, String[] menuIds);

    int delete(int i);

    Role selectByName(String name);
}
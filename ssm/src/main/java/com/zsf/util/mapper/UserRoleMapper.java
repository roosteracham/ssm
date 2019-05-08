package com.zsf.util.mapper;

import com.zsf.domain.UserInfo;
import com.zsf.domain.UserRole;

public interface UserRoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserRole record);

    int insertSelective(UserRole record);

    UserRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserRole record);

    int updateByPrimaryKey(UserRole record);

    UserRole selectByUserId(UserInfo userInfo);
}
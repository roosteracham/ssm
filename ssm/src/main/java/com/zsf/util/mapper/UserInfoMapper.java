package com.zsf.util.mapper;

import com.zsf.domain.UserInfo;

import java.util.List;

public interface UserInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserInfo record);

    int insertSelective(UserInfo record);

    UserInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserInfo record);

    int updateByPrimaryKey(UserInfo record);

    UserInfo selectByName(UserInfo userInfo);

    UserInfo selectByEmail(UserInfo userInfo);

    void updateByName(UserInfo userInfo);

    List<UserInfo> selectByChecked(int i);
}
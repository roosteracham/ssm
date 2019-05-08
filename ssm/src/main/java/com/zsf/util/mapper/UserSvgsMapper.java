package com.zsf.util.mapper;

import com.zsf.domain.RoleProjectSvg;
import com.zsf.domain.UserSvgs;

import java.util.List;

public interface UserSvgsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserSvgs record);

    int insertSelective(UserSvgs record);

    UserSvgs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserSvgs record);

    int updateByPrimaryKey(UserSvgs record);

    List<Integer> selectSvgsByUserId(Integer userId);

    List<UserSvgs> selectAll();

    List<Integer> selectSvgsById(Integer id);

    int deleteByPSId(UserSvgs userSvgs);

    UserSvgs querySelective(UserSvgs userSvgs);
}
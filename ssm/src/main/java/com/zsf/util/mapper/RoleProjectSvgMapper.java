package com.zsf.util.mapper;

import com.zsf.domain.RoleProjectSvg;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleProjectSvgMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RoleProjectSvg record);

    int insertSelective(RoleProjectSvg record);

    RoleProjectSvg selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RoleProjectSvg record);

    int updateByPrimaryKey(RoleProjectSvg record);

    List<RoleProjectSvg> queryByRoleId(@Param("id") String roleId);

    int deleteByRoleId(String id);

    String queryProjectName(RoleProjectSvg roleProjectSvg);

    int deleteByPSId(RoleProjectSvg roleProjectSvg);
}
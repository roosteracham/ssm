package com.zsf.util.mapper;

import com.zsf.domain.ProjectInfo;
import com.zsf.domain.RoleProjectSvg;

import java.util.List;
import java.util.Set;

public interface ProjectInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ProjectInfo record);

    int insertSelective(ProjectInfo record);

    ProjectInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ProjectInfo record);

    int updateByPrimaryKey(ProjectInfo record);

    ProjectInfo selectByProjectId(int projectId);

    List<ProjectInfo> selectAllProjects();

    ProjectInfo selectByProjectName(String name);

    int deleteByPId(int projectId);

    List<ProjectInfo> selectByProjectId(Set<String> projectIds);
}
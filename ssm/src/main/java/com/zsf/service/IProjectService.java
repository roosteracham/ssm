package com.zsf.service;

import com.zsf.domain.ProjectInfo;

import java.util.List;

public interface IProjectService {

    int insert(ProjectInfo projectInfo);

    int update(ProjectInfo projectInfo);

    int delete(int id);

    ProjectInfo select(int id);

    List<ProjectInfo> selectAllProjects();

    ProjectInfo selectByProjectId(int id);
}

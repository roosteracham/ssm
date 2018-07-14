package com.zsf.service.impl;

import com.zsf.domain.ProjectInfo;
import com.zsf.mapper.ProjectInfoMapper;
import com.zsf.service.IProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl implements IProjectService {

    @Autowired
    private ProjectInfoMapper projectInfoMapper;

    @Override
    public int insert(ProjectInfo projectInfo) {
        return projectInfoMapper.insert(projectInfo);
    }

    @Override
    public int update(ProjectInfo projectInfo) {
        return projectInfoMapper.updateByPrimaryKey(projectInfo);
    }

    @Override
    public int delete(int id) {
        return projectInfoMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<ProjectInfo> selectAllProjects() {
        return projectInfoMapper.selectAllProjects();
    }

    @Override
    public ProjectInfo select(int id) {

        return projectInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public ProjectInfo selectByProjectId(int index) {
        return projectInfoMapper.selectByProjectId(index);
    }
}

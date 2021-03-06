package com.zsf.dao;

import com.zsf.domain.ProjectInfo;
import com.zsf.util.mapper.ProjectInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public class ProjectInfoDao {

    @Autowired
    private ProjectInfoMapper projectInfoMapper;

    public int insert(ProjectInfo projectInfo) {
        return projectInfoMapper.insert(projectInfo);
    }

    public int update(ProjectInfo projectInfo) {
        return projectInfoMapper.updateByPrimaryKey(projectInfo);
    }

    public int delete(int id) {
        return projectInfoMapper.deleteByPrimaryKey(id);
    }

    public List<ProjectInfo> selectAllProjects() {
        return projectInfoMapper.selectAllProjects();
    }

    public ProjectInfo select(int id) {

        return projectInfoMapper.selectByPrimaryKey(id);
    }

    public ProjectInfo selectByProjectId(int index) {
        return projectInfoMapper.selectByProjectId(index);
    }

    public ProjectInfo selectByProjectName(String name) {
        return projectInfoMapper.selectByProjectName(name);
    }

    public List<ProjectInfo> selectByProjectId(Set<String> projectIds) {
        return projectInfoMapper.selectByProjectId(projectIds);
    }
}

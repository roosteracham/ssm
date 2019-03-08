package com.zsf.service.impl;

import com.alibaba.fastjson.JSON;
import com.zsf.dao.ComponentInfoDao;
import com.zsf.dao.ProjectInfoDao;
import com.zsf.dao.RedisDao;
import com.zsf.dao.SvgInfoDao;
import com.zsf.domain.*;
import com.zsf.service.IProjectService;
import com.zsf.util.errorcode.ErrorCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectServiceImpl implements IProjectService {

    @Autowired
    private ProjectInfoDao projectInfoDao;

    @Autowired
    private SvgInfoDao svgInfoDao;

    @Autowired
    private ComponentInfoDao componentInfoDao;

    @Autowired
    private RedisDao redisDao;

    @Override
    public ResBody deleteSvg(ProjectDto projectDto) {

        ResBody body = new ResBody();
        body.setSuccess(true);
        body.setErrorCode(ErrorCodeEnum.SUC.getIndex());
        try {
            ProjectInfo projectInfo = projectInfoDao.
                    selectByProjectName(projectDto.getProjectName());
            if (null != projectInfo) {
                SvgInfo svgInfo = new SvgInfo();
                svgInfo.setName(projectDto.getName());
                svgInfo.setProjectId(projectInfo.getProjectId());
                svgInfoDao.deleteSvg(svgInfo);
            } else {
                throw new NullPointerException();
            }

            String key = projectInfo.getProjectName() + "_" +
                    projectDto.getName() + "_" + projectDto.getSvgIndex();
            redisDao.delete(key);
        } catch (NullPointerException e) {
            body.setSuccess(false);
            body.setErrorCode(ErrorCodeEnum.FAIL.getIndex());
        }
        body.setData("");
        return body;
    }

    @Override
    public ResBody addProjectToCollection(ProjectDto projectDto) {

        // 更新工程
        ProjectInfo projectInfo = new ProjectInfo();
        projectInfo.setProjectName(projectDto.getProjectName());
        projectInfo.setProjectId(projectDto.getProjectIndex());

        ResBody body = new ResBody();
        try {
            ProjectInfo project = projectInfoDao.
                    selectByProjectName(projectInfo.getProjectName());

            if (project == null) {
                projectInfoDao.insert(projectInfo);
            }

            // 更新画面
            SvgInfo svgInfo = new SvgInfo();
            svgInfo.setSvgId(projectDto.getSvgIndex());
            svgInfo.setProjectId(projectInfo.getProjectId());
            svgInfo.setName(projectDto.getName());

            SvgInfo svg = svgInfoDao.selectBySvgId(svgInfo.getSvgId());

            if (svg == null) {
                svgInfoDao.insert(svgInfo);
            }
            body.setSuccess(true);
            body.setErrorCode(ErrorCodeEnum.SUC.getIndex());
        } catch (Exception e) {
            body.setSuccess(false);
            body.setErrorCode(ErrorCodeEnum.FAIL.getIndex());
        }

        body.setData("");
        return body;
    }

    @Override
    public ResBody getProjectsCollection() {

        List<ProjectInfo> projectInfos = projectInfoDao.selectAllProjects();

        List<SvgInfo> svgInfos = svgInfoDao.selectAllSvgs();

        List<ComponentInfo> componentInfos = componentInfoDao.getAllComponents();

        StringBuilder stringBuilder = new StringBuilder("");
        stringBuilder.append("{");
        if (svgInfos.size() != 0 || projectInfos.size() != 0
                || componentInfos.size() != 0) {
            svgsToString(svgInfos, stringBuilder,
                    projectInfos.size() != 0 || componentInfos.size() != 0);
            projectsToString(projectInfos, componentInfos.size(), stringBuilder);
            componentsToString(componentInfos, stringBuilder);
        }
        stringBuilder.append("}");

        ResBody body = new ResBody();
        body.setSuccess(true);
        body.setErrorCode(ErrorCodeEnum.SUC.getIndex());
        body.setData(stringBuilder.toString());
        return body;
    }

    private void componentsToString(List<ComponentInfo> componentInfos,
                                    StringBuilder stringBuilder) {
        if (componentInfos == null || componentInfos.size() <= 0)
            return;
        List<String> list = new ArrayList<>();
        for (int i = 0; i < componentInfos.size(); i++) {
            list.add(componentInfos.get(i).getName());
        }
        stringBuilder.append("\"4\":")
                .append(JSON.toJSON(list));
    }

    private void svgsToString(List<SvgInfo> svgInfos, StringBuilder stringBuilder, boolean append) {

        if (svgInfos == null || svgInfos.size() <= 0)
            return;

        List<SvgInfoDto> list = new ArrayList<>();
        for (int i = 0; i < svgInfos.size(); i++) {

            SvgInfo svg = svgInfos.get(i);
            ProjectInfo projectInfo = projectInfoDao.selectByProjectId(svg.getProjectId());
            SvgInfoDto svgInfoDto = new SvgInfoDto();
            svgInfoDto.setIndex(svg.getSvgId());
            svgInfoDto.setName(svg.getName());
            svgInfoDto.setProjectName(projectInfo.getProjectName());
            svgInfoDto.setId("svg-" + projectInfo.getProjectName()
                    + "-" + svg.getName() + "-" + svg.getSvgId());
            list.add(svgInfoDto);
        }
        stringBuilder.append("\"1\":")
                .append(JSON.toJSON(list));
        if (append) {
            stringBuilder.append(",");
        }
    }

    private void projectsToString(List<ProjectInfo> projectInfos, int size,
                                  StringBuilder stringBuilder) {

        if (projectInfos == null || projectInfos.size() <= 0)
            return;
        stringBuilder.append("\"0\":")
                .append(JSON.toJSON(projectInfos));
        if (size > 0) {
            stringBuilder.append(",");
        }
    }

    @Override
    public ResBody saveGroupedElement(GroupedElement group) {

        ComponentInfo componentInfo =
                componentInfoDao.getComponentByName(group.getGroupName());

        // 更新数据库记录
        if (componentInfo == null) {
            componentInfo = new ComponentInfo();
            componentInfo.setName(group.getGroupName());
            componentInfoDao.insert(componentInfo);
        }

        // 画面存入redis
        String key = "group_" + group.getGroupName();
        redisDao.saveGroupedElement(key, group.getData());

        ResBody body = new ResBody();
        body.setSuccess(true);
        body.setErrorCode(ErrorCodeEnum.SUC.getIndex());
        body.setData("");
        return body;
    }
}

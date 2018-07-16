package com.zsf.business;

import com.zsf.domain.*;
import com.zsf.service.IComponentService;
import com.zsf.service.IProjectService;
import com.zsf.service.ISvgService;
import com.zsf.service.RedisService;
import com.zsf.util.errorcode.ErrorCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProjectBusiness {

    @Autowired
    private IProjectService projectService;

    @Autowired
    private ISvgService svgService;

    @Autowired
    private IComponentService componentService;

    @Autowired
    private RedisService redisService;

    public int addProject(ProjectDto projectDto) {
        ProjectInfo project = projectService.select(projectDto.getId());
        if (project == null){
            ProjectInfo projectInfo = new ProjectInfo();
            projectInfo.setId(projectDto.getProjectIndex());
            projectInfo.setProjectName(projectDto.getProjectName());
            return projectService.insert(projectInfo);
        }
        return -1;
    }

    public int addSvg(ProjectDto projectDto) {
        SvgInfo svg = svgService.select(projectDto.getSvgIndex());
        if (svg == null){
            SvgInfo svgInfo = new SvgInfo();
            svgInfo.setProjectId(projectDto.getProjectIndex());
            svgInfo.setName(projectDto.getProjectName());
            return svgService.insert(svgInfo);
        }
        return -1;
    }

    public int updateProject(ProjectDto projectDto) {
        ProjectInfo projectInfo = new ProjectInfo();
        projectInfo.setId(projectDto.getProjectIndex());
        projectInfo.setProjectName(projectDto.getProjectName());
        return projectService.update(projectInfo);
    }

    public int deleteProject(ProjectDto projectDto) {
        return projectService.delete(projectDto.getProjectIndex());
    }

    public ProjectInfo selectProject(int id) {
        return projectService.select(id);
    }

    public List<ProjectInfo> selectAllProjects() {
        return projectService.selectAllProjects();
    }

    public ResBody addProjectToColletion(ProjectDto projectDto) {

        // 更新工程
        ProjectInfo projectInfo = new ProjectInfo();
        projectInfo.setProjectId(projectDto.getProjectIndex());
        projectInfo.setProjectName(projectDto.getProjectName());

        ProjectInfo project = projectService.selectByProjectId(projectInfo.getProjectId());

        if (project == null) {
            projectService.insert(projectInfo);
        }

        // 更新画面
        SvgInfo svgInfo = new SvgInfo();
        svgInfo.setSvgId(projectDto.getSvgIndex());
        svgInfo.setProjectId(projectDto.getProjectIndex());
        svgInfo.setName(projectDto.getName());

        SvgInfo svg = svgService.selectBySvgId(svgInfo.getSvgId());

        if (svg == null) {
            svgService.insert(svgInfo);
        }

        ResBody body = new ResBody();
        body.setSuccess(true);
        body.setErrorCode(ErrorCodeEnum.SUC.getIndex());
        body.setData("");
        return body;
    }

    public ResBody getProjectsColletion() {

        List<ProjectInfo> projectInfos = projectService.selectAllProjects();

        List<SvgInfo> svgInfos = svgService.selectAllSvgs();

        List<ComponentInfo> componentInfos = componentService.getAllComponents();

        StringBuilder stringBuilder = new StringBuilder("");
        stringBuilder.append("{");
        if (svgInfos.size() != 0 || projectInfos.size() != 0
                || componentInfos.size() != 0) {
            svgsToString(svgInfos, stringBuilder,
                    projectInfos.size() != 0 && componentInfos.size() != 0);
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
        stringBuilder.append("\"4\":[");
        for (int i = 0; i < componentInfos.size(); i++) {
            ComponentInfo componentInfo = componentInfos.get(i);
            stringBuilder.append("\"")
                    .append(componentInfo.getName())
                    .append("\"");
            if (i != componentInfos.size() - 1) {
                stringBuilder.append(",");
            }
        }
        stringBuilder.append("]");
    }

    private void svgsToString(List<SvgInfo> svgInfos, StringBuilder stringBuilder, boolean append) {

        if (svgInfos == null || svgInfos.size() <= 0)
            return;
        stringBuilder.append("\"1\":[");
        for (int i = 0; i < svgInfos.size(); i++) {
            SvgInfo svg = svgInfos.get(i);
            ProjectInfo projectInfo = projectService.selectByProjectId(svg.getProjectId());
            stringBuilder.append("{\"projectName\":\"")
                    .append(projectInfo.getProjectName())
                    .append("\",\"name\":\"")
                    .append(svg.getName())
                    .append("\",\"id\":\"")
                    .append("svg-" + projectInfo.getProjectName()
                            + "-" + svg.getName() + "-" + svg.getSvgId())
                    .append("\",\"index\":\"")
                    .append(svg.getSvgId())
                    .append("\"}");
            if (i != svgInfos.size() - 1) {
                stringBuilder.append(",");
            }
        }
        stringBuilder.append("]");
        if (append) {
            stringBuilder.append(",");
        }
    }

    private void projectsToString(List<ProjectInfo> projectInfos, int size,
                                  StringBuilder stringBuilder) {

        if (projectInfos == null || projectInfos.size() <= 0)
            return;
        stringBuilder.append("\"0\":[");
        for (int i = 0; i < projectInfos.size(); i++) {
            ProjectInfo project = projectInfos.get(i);
            stringBuilder.append("{\"index\":\"")
                    .append(project.getProjectId())
                    .append("\",\"projectName\":\"")
                    .append(project.getProjectName())
                    .append("\"}");
            if (i != projectInfos.size() - 1) {
                stringBuilder.append(",");
            }
        }
        stringBuilder.append("]");
        if (size > 0) {
            stringBuilder.append(",");
        }
    }

    public ResBody saveGroupedElement(GroupedElement group) {

        ComponentInfo componentInfo =
                componentService.getComponentByName(group.getGroupName());

        // 更新数据库记录
        if (componentInfo == null) {
            componentInfo = new ComponentInfo();
            componentInfo.setName(group.getGroupName());
            componentService.insert(componentInfo);
        }

        // 画面存入redis
        String key = "group_" + group.getGroupName();
        redisService.saveGroupedElement(key, group.getData());

        ResBody body = new ResBody();
        body.setSuccess(true);
        body.setErrorCode(ErrorCodeEnum.SUC.getIndex());
        body.setData("");
        return body;
    }
}

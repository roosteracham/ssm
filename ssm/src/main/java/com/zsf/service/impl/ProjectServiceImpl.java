package com.zsf.service.impl;

import com.alibaba.fastjson.JSON;
import com.zsf.dao.*;
import com.zsf.domain.*;
import com.zsf.service.IProjectService;
import com.zsf.service.RedisService;
import com.zsf.util.errorcode.ErrorCodeEnum;
import com.zsf.util.errorcode.RedirectEnum;
import com.zsf.util.mapper.ProjectInfoMapper;
import com.zsf.util.mapper.RoleProjectSvgMapper;
import com.zsf.util.mapper.UserRoleMapper;
import com.zsf.util.mapper.UserSvgsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

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

    @Autowired
    private UserSvgsMapper userSvgsMapper;

    @Autowired
    private ProjectInfoMapper projectInfoMapper;

    @Autowired
    private UserInfoDao userInfoDao;

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RoleProjectSvgMapper roleProjectSvgMapper;

    @Override
    public ResBody deleteSvg(ProjectDto projectDto) {

        ResBody body = new ResBody();
        body.setSuccess(true);
        body.setErrorCode(ErrorCodeEnum.SUC.getIndex());
        try {
            ProjectInfo projectInfo = projectInfoDao.
                    selectByProjectName(projectDto.getProjectName());
            SvgInfo svgInfo = new SvgInfo();
            svgInfo.setSvgId(projectDto.getSvgIndex());
            svgInfo.setName(projectDto.getName());
            if (null != projectInfo) {
                svgInfo.setName(projectDto.getName());
                svgInfo.setProjectId(projectInfo.getProjectId());
                svgInfoDao.deleteSvg(svgInfo);
            } else {
                throw new NullPointerException();
            }

            String key = projectInfo.getProjectName() + "_" +
                    projectDto.getName() + "_" + projectDto.getSvgIndex();
            redisDao.delete(key);
            // 从user_svgs表删除

            UserSvgs userSvgs = buildUserSvgs(projectInfo, svgInfo);
            int n1 = userSvgsMapper.deleteByPSId(userSvgs);
            // 从role_project_svg表删除
            RoleProjectSvg roleProjectSvg = new RoleProjectSvg();
            roleProjectSvg.setProjectId(projectInfo.getProjectId() + "");
            roleProjectSvg.setSvgId(svgInfo.getSvgId() + "");
            int n2 = roleProjectSvgMapper.deleteByPSId(roleProjectSvg);
        } catch (NullPointerException e) {
            body.setSuccess(false);
            body.setErrorCode(ErrorCodeEnum.FAIL.getIndex());
        }
        body.setData("");
        return body;
    }

    private UserSvgs buildUserSvgs(ProjectInfo projectInfo, SvgInfo svgInfo) {
        UserSvgs userSvgs = new UserSvgs();
        userSvgs.setProjectId(svgInfo.getProjectId());
        userSvgs.setProjectName(projectInfo.getProjectName());
        userSvgs.setSvgId(svgInfo.getSvgId());
        userSvgs.setSvgName(svgInfo.getName());
        return userSvgs;
    }

    @Override
    public ResBody configHtml(Integer nep, HttpServletRequest request) {

        String token = request.getHeader("Authorization").split(" ")[1];
        int id = Integer.parseInt(token);
        UserInfo userInfo = userInfoDao.selectById(id);
        ResBody body = null;
        if (null != userInfo) {

            TokenLocation tokenLocation = new TokenLocation();
            tokenLocation.setToken(token);
            if (null == nep || 0 != nep) {

                if (userInfo.getRole() == 1) {
                    tokenLocation.setLocation(RedirectEnum.CONFIG);
                } else {
                    tokenLocation.setLocation(RedirectEnum.MANAGER);
                }
            } else {
                tokenLocation.setLocation(RedirectEnum.RUN);
            }
            body = new ResBody();
            body.setSuccess(true);
            body.setData(JSON.toJSONString(tokenLocation));
        }
        return body;
    }

    @Override
    public ResBody runHtml(HttpServletRequest request) {
        return null;
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

            SvgInfo svg = svgInfoDao.selectByPSId(svgInfo);

            if (svg == null) {
                svgInfoDao.insert(svgInfo);
            }
            // 再user_svgs插入记录
            UserSvgs userSvgs = new UserSvgs();
            userSvgs.setSvgName(svg.getName());
            userSvgs.setSvgId(svg.getSvgId());
            userSvgs.setProjectName(projectInfo.getProjectName());
            userSvgs.setProjectId(projectInfo.getProjectId());
            UserSvgs us = userSvgsMapper.querySelective(userSvgs);
            if (null == us) {
                int n = userSvgsMapper.insertSelective(userSvgs);
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
    public ResBody getProjects(HttpServletRequest request) {

        ResBody resBody = null;
        // 获得关联的role
        String token = request.getHeader("Authorization");
        UserInfo userInfo;
        UserRole userRole = null;
        //boolean failed = true;
        if (token != null) {
            token = token.split(" ")[1];
            String value = redisService.getValue(token);

            // 如果value不空，通过身份验证
            if (value != null) {
                Integer id = Integer.parseInt(token);
                userInfo = new UserInfo();
                userInfo.setId(id);
                userInfo = userInfoDao.selectById(id);
                if (1 == userInfo.getRole()) {
                    resBody = getProjectsCollection();
                } else {

                    userRole = userRoleMapper.selectByUserId(userInfo);
                    resBody = getProjectByRole(userRole);
                }
            }
        }


        return resBody;
    }

    private ResBody getProjectByRole(UserRole userRole) {
        StringBuilder stringBuilder = new StringBuilder("{");
        // 加project和svg
        if (null != userRole) {

            // 先加自定义组件
            List<ComponentInfo> componentInfos = componentInfoDao.getAllComponents();
            componentsToString(componentInfos, stringBuilder);

            List<RoleProjectSvg> roleProjectSvgs =
                    roleProjectSvgMapper.queryByRoleId(userRole.getRoleId());
            if (roleProjectSvgs.size() > 0) {

                Set<String> projectIds = new HashSet<>();
                List<SvgInfoDto> list = new ArrayList<>();
                stringBuilder.append(",");
                for (RoleProjectSvg rps : roleProjectSvgs) {
                    projectIds.add(rps.getProjectId());
                    SvgInfoDto svgInfoDto = new SvgInfoDto();
                    svgInfoDto.setIndex(Integer.parseInt(rps.getSvgId()));
                    svgInfoDto.setName(rps.getSvgName());
                    svgInfoDto.setProjectName(rps.getProjectName());
                    svgInfoDto.setId("svg-" + rps.getProjectName()
                            + "-" + rps.getSvgName() + "-" + rps.getSvgId());
                    list.add(svgInfoDto);
                }
                stringBuilder.append("\"1\":")
                        .append(JSON.toJSON(list));

                List<ProjectInfo> projectInfos = new ArrayList<>();

                for (String id : projectIds) {
                    ProjectInfo projectInfo =
                            projectInfoDao.selectByProjectId(Integer.parseInt(id));
                    projectInfos.add(projectInfo);
                }
                if (0 < projectInfos.size()) {
                    stringBuilder.append(",");
                    projectsToString(projectInfos, 0, stringBuilder);
                }

            }
        }
        stringBuilder.append("}");
        ResBody body = new ResBody();
        body.setSuccess(true);
        body.setErrorCode(ErrorCodeEnum.SUC.getIndex());
        body.setData(stringBuilder.toString());
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

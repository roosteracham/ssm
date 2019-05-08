package com.zsf.service.impl;

import com.zsf.dao.RedisDao;
import com.zsf.domain.*;
import com.zsf.service.IMenuService;
import com.zsf.util.MenuUtil;
import com.zsf.util.mapper.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MenuServiceImpl implements IMenuService {

    @Autowired
    public MenuMapper menuMapper;

    @Autowired
    public RoleProjectSvgMapper roleProjectSvgMapper;

    @Autowired
    public ProjectInfoMapper projectInfoMapper;

    @Autowired
    public SvgInfoMapper svgInfoMapper;

    @Autowired
    public UserSvgsMapper userSvgsMapper;

    @Autowired
    public RedisDao redisDao;

    @Override
    public List<Menu> queryByUserId(UserInfo loginUser) {
        return null;
    }

    @Override
    public List<Menu> queryAll() {
        return menuMapper.queryAll();
    }

    @Override
    public int addOrUpdateMenu(Menu menu) {
        return 0;
    }

    @Override
    public int delete(String id) {
        return 0;
    }

    @Override
    public List<Menu> queryAll(UserInfo loginUser) {
        return null;
    }

    @Override
    public int delete(String id, RoleProjectSvg roleProjectSvg) {

        try {
            String pName = roleProjectSvgMapper.queryProjectName(roleProjectSvg);
            roleProjectSvg.setProjectName(pName);
            roleProjectSvgMapper.deleteByPSId(roleProjectSvg);
            int pId = Integer.parseInt(roleProjectSvg.getProjectId());
            int sId = Integer.parseInt(roleProjectSvg.getSvgId());
            //projectInfoMapper.deleteByPId(pId);
            SvgInfo svgInfo = new SvgInfo();
            svgInfo.setProjectId(pId);
            svgInfo.setSvgId(sId);
            svgInfoMapper.deleteByPSId(svgInfo);

            UserSvgs userSvgs = new UserSvgs();
            userSvgs.setProjectId(pId);
            userSvgs.setSvgId(sId);
            userSvgsMapper.deleteByPSId(userSvgs);

            // 从redis中删除
            //deleteFromRedis(svgInfo);
        } catch (Exception e) {
            return 0;
        }

        return 1;
    }

    private void deleteFromRedis(SvgInfo svgInfo) {

        ProjectInfo projectInfo = projectInfoMapper.selectByProjectId(svgInfo.getProjectId());
        String key = projectInfo.getProjectName() + "_" +
                svgInfo.getName() + "_" + svgInfo.getSvgId();
        redisDao.delete(key);
    }

    @Override
    public Menu queryById(String id) {
        return null;
    }

    @Override
    public void setMenuInSession(HttpSession session) {
        List<Menu> rootMenu = menuMapper.queryAll();
        List<Menu> menuList = new ArrayList<Menu>();
        // 先找到所有的一级菜单
        for (int i = 0; i < rootMenu.size(); i++) {
            // 一级菜单没有parentId
            if (StringUtils.isBlank(rootMenu.get(i).getParentId())) {
                menuList.add(rootMenu.get(i));
            }
        }
        // 为一级菜单设置子菜单，getChild是递归调用的
        for (Menu menu : menuList) {
            menu.setChildren(MenuUtil.getChild(menu.getId(), rootMenu));
        }

        // 将权限列表保存到session中
        session.setAttribute("userMenuList", menuList);
    }
}

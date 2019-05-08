package com.zsf.service.impl;

import com.zsf.domain.*;
import com.zsf.service.IZtreeService;
import com.zsf.util.encode.SessionParam;
import com.zsf.util.errorcode.BaseReturn;
import com.zsf.util.errorcode.ErrorCode;
import com.zsf.util.mapper.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Service
public class ZtreeServiceImpl implements IZtreeService {
	@Autowired
	private RoleProjectSvgMapper roleProjectSvgMapper;

	@Autowired
	private MenuMapper menuDao;

	@Autowired
	private RoleMapper roleDao;

	@Autowired
	private UserSvgsMapper userSvgsMapper;

	@Autowired
	private ProjectInfoMapper projectInfoMapper;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 查询登录用户所拥有的菜单树
	 *
	 * @param loginUser
	 * @return
	 */
	public List<Ztree> getMenuZtree(UserInfo loginUser) {
		List<Menu> menuList = menuDao.queryAll();
		List<Ztree> ztreeList = new ArrayList<>();
		for (Menu menu : menuList) {
			Ztree ztree = new Ztree();
			ztree.setId(menu.getId());
			ztree.setpId(menu.getParentId());
			ztree.setName(menu.getName());
			ztree.setIcon(menu.getIcon());
			if (menu.getName().equals("个人设置") || menu.getName().equals("后台首页")) {
				ztree.setChecked(true);
				ztree.setChkDisabled(true);
			}
			if (StringUtils.isNotBlank(menu.getIcon())) {
				ztree.setOpen(true);
			}
			ztreeList.add(ztree);

		}
		return ztreeList;

	}

	/**
	 * 根据角色id查询角色所拥有的菜单
	 *
	 * @param loginUser
	 * @param roleId
	 * @return
	 */
	public List<Ztree> getMenuZtreeByRoleId(UserInfo loginUser, String roleId) {
		// 1 查询出用户所拥有的权限
		List<Menu> menuList = menuDao.queryAll();
		// 2 查询出角色所拥有的权限
		List<Menu> roleMenuList = menuDao.queryByRoleId(roleId);
		List<Ztree> ztreeList = new ArrayList<>();
		for (Menu menu : menuList) {
			Ztree ztree = new Ztree();
			ztree.setId(menu.getId());
			ztree.setpId(menu.getParentId());
			ztree.setName(menu.getName());
			ztree.setIcon(menu.getIcon());
			if (menu.getName().equals("个人设置") || menu.getName().equals("后台首页")) {
				ztree.setChecked(true);
				ztree.setChkDisabled(true);
			}
			for (Menu roleMenu : roleMenuList) {
				if (menu.equals(roleMenu)) {
					ztree.setChecked(true);
					continue;
				}
			}
			if (StringUtils.isNotBlank(menu.getIcon())) {
				ztree.setOpen(true);
			}
			ztreeList.add(ztree);

		}
		return ztreeList;
	}

	/**
	 * 查询登录用户所拥有的角色树
	 *
	 * @param loginUser
	 * @return
	 */
	public List<Ztree> getRoleZtree(UserInfo loginUser) {
		List<Role> roleList = roleDao.queryAll();
		List<Ztree> ztreeList = new ArrayList<>();
		for (Role role : roleList) {
			Ztree ztree = new Ztree();
			ztree.setId(role.getId() + "");
			ztree.setName(role.getName());
			ztreeList.add(ztree);
		}
		return ztreeList;
	}

	public List<Ztree> getRoleZtree(UserInfo loginUser, String userId) {
		List<Role> roleList = roleDao.queryAll();
		List<Role> userRoleList = roleDao.queryByUserId(Integer.parseInt(userId));
		List<Ztree> ztreeList = new ArrayList<>();
		for (Role role : roleList) {
			Ztree ztree = new Ztree();
			ztree.setId(role.getId() + "");
			ztree.setName(role.getName());
			for (Role userRole : userRoleList) {
				if (role.equals(userRole)) {
					ztree.setChecked(true);
					continue;
				}
			}
			ztreeList.add(ztree);
		}
		return ztreeList;

	}

	@Override
	public List<Ztree> roleZtree(UserInfo loginUser, String userId) {
		return null;
	}

	@Override
	public List<Ztree> roleZtree(UserInfo loginUser) {
		return null;
	}

	@Override
	public String menuZtree(Model model, HttpSession session, String roleId) {
		String res;

		UserInfo loginUser = (UserInfo) session.getAttribute(SessionParam.LOGIN_USER);
		if (null != loginUser) {
			List<Ztree> ztree = new ArrayList<>();
			if (StringUtils.isNoneBlank(roleId)) {
				ztree = this.getMenuZtreeByRoleId(roleId);

			} else {
				//ztree = this.getMenuZtree(loginUser);
				ztree = this.getZtreeByUserId(loginUser);
			}
			logger.info("-----返回的ztree:{}-----", BaseReturn.response(ztree));
			res = BaseReturn.response(ztree);
		} else {
			res = BaseReturn.response(ErrorCode.NOT_LOGGIN);
		}

		return res;
	}

	private List<Ztree> getZtreeByUserId(UserInfo userInfo) {
		String res = null;
		List<Ztree> ztreeList = new ArrayList<>();

		if (null != userInfo) {
			List<UserSvgs> userSvgs =
					userSvgsMapper.selectAll();

			//Set<ProjectInfo> projectInfos = new TreeSet<>();

			List<ProjectInfo> projectInfos = projectInfoMapper.selectAllProjects();
			for (UserSvgs svgs : userSvgs) {
				/*ProjectInfo projectInfo = new ProjectInfo();
				projectInfo.setProjectId(svgs.getProjectId() + 133);
				projectInfo.setProjectName(svgs.getProjectName());
				projectInfos.add(projectInfo);*/
				Ztree ztree = new Ztree();
				svgs.setProjectId(svgs.getProjectId() + 133);
				ztree.setId(svgs.getSvgId() + "");
				ztree.setpId(svgs.getProjectId() + "");
				ztree.setName(svgs.getSvgName());
				ztreeList.add(ztree);
			}
			addProjectToTree(ztreeList, projectInfos);
		}
		return ztreeList;
	}

	private List<Ztree> getMenuZtreeByRoleId(String roleId) {
		// 1 查询出用户所拥有的权限
		List<UserSvgs> userSvgs =
				userSvgsMapper.selectAll();
		// 2 查询出角色所拥有的权限
		List<RoleProjectSvg> roleMenuList =
				roleProjectSvgMapper.queryByRoleId(roleId);

		List<Ztree> ztreeList = new ArrayList<>();

		//Set<ProjectInfo> projectInfos = new TreeSet<>();
		List<ProjectInfo> projectInfos = projectInfoMapper.selectAllProjects();
		for (UserSvgs svgs : userSvgs) {
			svgs.setProjectId(svgs.getProjectId() + 133);
			Ztree ztree = new Ztree();
			ztree.setId(svgs.getSvgId() + "");
			ztree.setpId(svgs.getProjectId() + "");
			ztree.setName(svgs.getSvgName());
			svgs.setProjectId(svgs.getProjectId() - 133);
			for (RoleProjectSvg roleMenu : roleMenuList) {

				if (roleMenu.getProjectId().equals(svgs.getProjectId() + "") &&
						roleMenu.getSvgId().equals(svgs.getSvgId() + "") &&
						roleMenu.getProjectName().equals(svgs.getProjectName()) &&
						roleMenu.getSvgName().equals(svgs.getSvgName())) {
					ztree.setChecked(true);
					continue;
				}
			}
			ztreeList.add(ztree);

		}
		addProjectToTree(ztreeList, projectInfos);
		return ztreeList;
	}

	private void addProjectToTree(List<Ztree> ztreeList, List<ProjectInfo> projectInfos) {
		for (ProjectInfo project :
				projectInfos) {
			Ztree ztree = new Ztree();
			project.setProjectId(project.getProjectId() + 133);
			ztree.setId(project.getProjectId() + "");
			ztree.setpId("");
			ztree.setName(project.getProjectName());
			ztree.setIcon(ErrorCode.PROJECT_ICON.getMessage());
			ztree.setOpen(true);
			ztreeList.add(ztree);
		}
	}
}
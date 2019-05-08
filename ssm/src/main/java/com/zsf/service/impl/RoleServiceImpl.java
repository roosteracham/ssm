package com.zsf.service.impl;

import com.zsf.dao.ProjectInfoDao;
import com.zsf.domain.*;
import com.zsf.service.RoleService;
import com.zsf.util.UUIDGenerator;
import com.zsf.util.mapper.RoleMapper;
import com.zsf.util.mapper.RoleProjectSvgMapper;
import com.zsf.util.mapper.UserInfoMapper;
import com.zsf.util.mapper.UserRoleMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private ProjectInfoDao projectInfoDao;

	@Autowired
	private RoleMapper roleMapper;

	@Autowired
	private UserInfoMapper userInfoMapper;

	@Autowired
	private UserRoleMapper userRoleMapper;

	@Autowired
	private RoleProjectSvgMapper roleProjectSvgMapper;

	@Override
	public List<Role> queryAll() {
		return roleMapper.queryAll();
	}

	@Transactional
	@Override
	public int addOrUpdateRole(Role role, String[] menuIds) {
		int updateCount = 0;
		int deleteRoleMenu = 0;
		int updateRoleMenu = 0;
		if (StringUtils.isBlank(role.getId() + "")) {// 新增
			//role.setId(UUIDGenerator.getUUID());
			updateCount = roleMapper.insertSelective(role);
			// 插入角色权限
			updateRoleMenu = roleMapper.addRoleMenu(role.getId(), menuIds);
			if (updateCount == 0 || updateRoleMenu == 0) {
				return 0;
			} else {
				return 1;
			}
		} else {// 修改
				// 修改角色名称，修改权限列表
			/*updateCount = roleMapper.update(role);
			deleteRoleMenu = roleMapper.deleteRoleMenuByRoleId(role.getId());
			updateRoleMenu = roleMapper.addRoleMenu(role.getId(), menuIds);*/
			if (updateCount == 0 || deleteRoleMenu == 0 || updateRoleMenu == 0) {
				return 0;
			} else {
				return 1;
			}
		}

	}

	@Override
	public int delete(String id) {
		return roleMapper.delete(Integer.parseInt(id));
	}

	@Override
	public int addOrUpdate(UserInfo loginUser, String[] split) {
		String roleId = split[split.length - 1];
		if (null != loginUser.getId()) {// 更新

			UserInfo userInfo = userInfoMapper.selectByPrimaryKey(loginUser.getId());
			if (null != userInfo) {

				UserRole userRole = userRoleMapper.selectByUserId(userInfo);
				if (null == userRole) {
					userRole = new UserRole();
					userRole.setRoleId(roleId);
					userRole.setUserId(userInfo.getId() + "");
					userRoleMapper.insert(userRole);
				} else if (!roleId.equals(userRole.getRoleId())){
					userRole.setRoleId(roleId);
					userRoleMapper.updateByPrimaryKey(userRole);
				}
				return 1;
			}else {
				return 0;
			}
		} else { //新增

			loginUser.setChecked(3);
			loginUser.setRole(0);
			userInfoMapper.insertSelective(loginUser);
			UserInfo userInfo = userInfoMapper.selectByName(loginUser);
			if (null != userInfo) {
				UserRole userRole = new UserRole();
				userRole.setRoleId(roleId);
				userRole.setUserId(userInfo.getId() + "");
				userRoleMapper.insertSelective(userRole);
			}
		}
		return 0;
	}

	@Override
	public int deleteRole(String id) {

		roleProjectSvgMapper.deleteByRoleId(id);
		return roleMapper.deleteByPrimaryKey(Integer.parseInt(id));
	}

	@Override
	public int updateRole(Role role, List<RoleProjectSvg> roleProjectSvgs) {

		try {
			if (null == role.getId()) {// 新增
				//role.setId(UUIDGenerator.getUUID());
				int count = roleMapper.insertSelective(role);
				Role role1 = roleMapper.selectByName(role.getName());
				if (null != role1) {
					for (RoleProjectSvg rps : roleProjectSvgs) {
						rps.setRoleId(role1.getId() + "");
						roleProjectSvgMapper.insertSelective(rps);
					}
				} else {
					return 0;
				}
			} else {// 已经有了权限
				//先删除再添加
				roleProjectSvgMapper.deleteByRoleId(role.getId() + "");
				List<RoleProjectSvg> rpses =
						roleProjectSvgMapper.queryByRoleId(role.getId() + "");
				List<RoleProjectSvg> nrps = new ArrayList<>();
				for (RoleProjectSvg rps : roleProjectSvgs) {

					rps.setRoleId(role.getId() + "");
					if (null == rps.getProjectName()) {
						ProjectInfo projectInfo =
								projectInfoDao.selectByProjectId(
										Integer.parseInt(rps.getProjectId()));
						rps.setProjectName(projectInfo.getProjectName());
					}
					nrps.add(rps);
					roleProjectSvgMapper.insert(rps);
				}
			}
		} catch (Exception e) {
			return 0;
		}
		return 1;
	}
}

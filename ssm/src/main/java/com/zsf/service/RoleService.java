package com.zsf.service;

import com.zsf.domain.Role;
import com.zsf.domain.RoleProjectSvg;
import com.zsf.domain.UserInfo;

import java.util.List;

/**
 * @author 程高伟
 *
 * @date 2016年6月16日 下午9:27:47
 */
public interface RoleService {

	/**
	 * 查询用户所能看到的角色列表
	 * 
	 * @return
	 */
	public List<Role> queryAll();

	/**
	 * 添加菜单或修改菜单
	 * 
	 * @param
	 * @return
	 */
	public int addOrUpdateRole(Role role, String[] menuIds);

	/**
	 * 删除角色
	 * 
	 * @param id
	 * @return
	 */
	public int delete(String id);

	int updateRole(Role role, List<RoleProjectSvg> roleProjectSvgs);

	int deleteRole(String id);

    int addOrUpdate(UserInfo loginUser, String[] split);
}

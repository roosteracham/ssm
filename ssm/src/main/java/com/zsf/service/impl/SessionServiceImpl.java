package com.zsf.service.impl;

import com.zsf.domain.Menu;
import com.zsf.domain.UserInfo;
import com.zsf.service.IMenuService;
import com.zsf.service.ISessionService;
import com.zsf.util.MenuUtil;
import com.zsf.util.encode.Param;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * 用来更新session中的信息
 *
 */
@Service
public class SessionServiceImpl implements ISessionService {
	@Autowired
	private IMenuService menuService;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 更新session中的菜单
	 * 
	 * @param session
	 * @param loginUser
	 */
	public void setMenuInSession(HttpSession session, UserInfo loginUser) {
		// 查询用户拥有的权限列表
		// 所有菜单
		List<Menu> rootMenu = menuService.queryAll(loginUser);
		// 要返回的菜单
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
		logger.info("-----用户的菜单:{}-----", menuList);
		// 将权限列表保存到session中
		session.setAttribute(Param.SESSION_USER_MENU, menuList);
	}

}

package com.zsf.controller;

import com.zsf.domain.Menu;
import com.zsf.domain.RoleProjectSvg;
import com.zsf.domain.UserInfo;
import com.zsf.service.IMenuService;
import com.zsf.service.ISessionService;
import com.zsf.util.encode.Param;
import com.zsf.util.encode.SessionParam;
import com.zsf.util.errorcode.BaseReturn;
import com.zsf.util.errorcode.ErrorCode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping(value = "/admin/menu")
public class MenuController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private IMenuService menuService;

	@Autowired
	private ISessionService sessionService;

	/**
	 * 菜单管理
	 * 
	 * @param model
	 * @param session
	 * @return
	 */
	@RequestMapping("/index.html")
	public String index(Model model, HttpSession session) {
		String title = "菜单管理";
		UserInfo loginUser = (UserInfo) session.getAttribute(SessionParam.LOGIN_USER);
		if (null != loginUser) {
			List<Menu> menuList = menuService.queryAll();
			model.addAttribute(Param.MENU_LIST, menuList);
		}
		model.addAttribute("title", title);
		return "/admin/menu/index";
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public String add(Menu menu, HttpSession session) {
		UserInfo loginUser = (UserInfo) session.getAttribute(SessionParam.LOGIN_USER);
		logger.info("-----正在更新菜单,传过来的数据：{}-----", menu.toString());
		int count = menuService.addOrUpdateMenu(menu);
		logger.info("----------更新数据：{}----------", count);
		if (count > 0) {
			sessionService.setMenuInSession(session, loginUser);
			return BaseReturn.response(ErrorCode.SUCCESS);
		} else {
			return BaseReturn.response(ErrorCode.FAILURE);
		}

	}

	@RequestMapping(value = "/detial/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String detial(@PathVariable String id) {
		logger.info("-----正在查询菜单详情：{}-----", id);
		if (StringUtils.isBlank(id)) {
			return BaseReturn.response(ErrorCode.PARAM_ERROR, "id不能为空");
		}
		Menu menu = menuService.queryById(id);
		logger.info("-----查找到的菜单：{}-----", menu);
		if (null == menu) {
			return BaseReturn.response(ErrorCode.RECORD_NULL);
		} else {
			return BaseReturn.response(ErrorCode.SUCCESS, menu);
		}
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public String delete(@RequestBody RoleProjectSvg roleProjectSvg,
						 HttpSession session) {
		String id = roleProjectSvg.getProjectId();
		UserInfo loginUser = (UserInfo) session.getAttribute(SessionParam.LOGIN_USER);
		logger.debug("-----删除菜单-----");
		logger.info("-----传过来的id:{}-----", id);
		if (StringUtils.isBlank(id)) {
			return BaseReturn.response(ErrorCode.PARAM_ERROR, "id不能为空");
		} else {
			int deleteCount = menuService.delete(id, roleProjectSvg);
			if (deleteCount > 0) {
				menuService.setMenuInSession(session);
				return BaseReturn.response(ErrorCode.SUCCESS, deleteCount);
			} else {
				return BaseReturn.response(ErrorCode.FAILURE);
			}
		}

	}

}

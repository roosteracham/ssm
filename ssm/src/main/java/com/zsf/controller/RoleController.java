package com.zsf.controller;

import com.alibaba.fastjson.JSON;
import com.zsf.domain.*;
import com.zsf.service.IUserService;
import com.zsf.service.IZtreeService;
import com.zsf.service.RedisService;
import com.zsf.service.RoleService;
import com.zsf.util.encode.Param;
import com.zsf.util.encode.SessionParam;
import com.zsf.util.errorcode.BaseReturn;
import com.zsf.util.errorcode.ErrorCode;
import com.zsf.util.errorcode.RedirectEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@Controller
@RequestMapping(value = "/admin/role")
public class RoleController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private RoleService roleService;

	@Autowired
	private IZtreeService ztreeService;

	/**
	 * 角色管理
	 * 
	 * @param model
	 * @param session
	 * @return
	 */
	@RequestMapping("/index.html")
	public String index(Model model, HttpSession session) {
		String title = "角色管理";
		UserInfo loginUser = (UserInfo) session.getAttribute(SessionParam.LOGIN_USER);
		if (null != loginUser) {
			List<Role> roleList = roleService.queryAll();
			model.addAttribute(Param.Role_LIST, roleList);
		}

		model.addAttribute("title", title);
		return "/admin/role/index";
	}

	/**
	 * 分配权限的时候加载的ztree
	 * 
	 * @param model
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/menuZtree", method = RequestMethod.GET)
	@ResponseBody
	public String menuZtree(Model model, HttpSession session, String roleId) {
		return ztreeService.menuZtree(model, session, roleId);
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public String add(Role role, String menuIds) {
		logger.info("-----正在更新角色,传过来的数据：{}-----", role.toString());
		logger.info("----------菜单id：{}----------", menuIds);
		List<RoleProjectSvg> roleProjectSvgs = JSON.parseArray(menuIds, RoleProjectSvg.class);
		int count = roleService.updateRole(role, roleProjectSvgs);
		logger.info("----------更新数据：{}----------", count);
		if (count > 0) {
			return BaseReturn.response(ErrorCode.SUCCESS);
		} else {
			return BaseReturn.response(ErrorCode.FAILURE);
		}

	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	@ResponseBody
	public String delete(@PathVariable String id, HttpServletRequest request) {

		logger.debug("-----删除角色-----");
		logger.info("-----传过来的id:{}-----", id);
		if (StringUtils.isBlank(id)) {
			return BaseReturn.response(ErrorCode.PARAM_ERROR, "id不能为空");
		} else {
			int deleteCount = 0;
			deleteCount = roleService.deleteRole(id);
			if (deleteCount > 0) {
				return BaseReturn.response(ErrorCode.SUCCESS, deleteCount);
			} else {
				return BaseReturn.response(ErrorCode.FAILURE, "删除数据失败");
			}
		}

	}

	/**
	 * 分配权限的时候加载的ztree
	 *
	 * @param model
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/roleZtree", method = RequestMethod.GET)
	@ResponseBody
	public String roleZtree(Model model, HttpSession session, String userId) {
		UserInfo loginUser = (UserInfo) session.getAttribute(SessionParam.LOGIN_USER);
		if (null != loginUser) {
			List<Ztree> ztree = new ArrayList<>();
			if (StringUtils.isNoneBlank(userId)) {
				ztree = ztreeService.getRoleZtree(loginUser, userId);
			} else {
				ztree = ztreeService.getRoleZtree(loginUser);
			}
			logger.info("-----返回的ztree:{}-----", BaseReturn.response(ztree));
			return BaseReturn.response(ztree);
		} else {
			return BaseReturn.response(ErrorCode.NOT_LOGGIN);
		}
	}

	/**
	 * 更新用户管理角色
	 * @param loginUser
	 * @param roleIds
	 * @return
	 */
	@RequestMapping(value = "/bind", method = RequestMethod.POST)
	@ResponseBody
	public String bind(UserInfo loginUser, String roleIds) {
		logger.info("-----正在更新用户,传过来的数据：{}-----", loginUser.toString());
		logger.info("----------角色id：{}----------", roleIds);
		int count = roleService.addOrUpdate(loginUser, roleIds.split(","));
		logger.info("----------更新数据：{}条----------", count);
		if (count > 0) {
			return BaseReturn.response(ErrorCode.SUCCESS);
		} else {
			return BaseReturn.response(ErrorCode.FAILURE);
		}

	}
}

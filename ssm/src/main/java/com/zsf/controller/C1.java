package com.zsf.controller;

import com.alibaba.fastjson.JSON;
import com.zsf.dao.UserInfoDao;
import com.zsf.domain.*;
import com.zsf.service.IMenuService;
import com.zsf.service.IUserService;
import com.zsf.service.RedisService;
import com.zsf.service.RoleService;
import com.zsf.util.encode.Param;
import com.zsf.util.errorcode.BaseReturn;
import com.zsf.util.errorcode.ErrorCode;
import com.zsf.util.errorcode.RedirectEnum;
import com.zsf.util.mapper.MenuMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.List;


@Controller
@RequestMapping("/c1")
public class C1 {

    @Autowired
    private IUserService userService;

    @Autowired
    private UserInfoDao userInfoDao;

    @Autowired
    private IMenuService menuService;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private IUserService userInfoService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RoleService roleService;

    @RequestMapping("/user/{id}")
    public String c(@PathVariable("id") int id) {
        //User user = userService.selectByPrimaryKey(id);
        //System.out.println(user.getName() + " : " + user.getPass());
        return  null;
    }

    @RequestMapping(value = "/a", method = RequestMethod.GET)
    public String a(int id, HttpSession session) {

        UserInfo userInfo = userInfoDao.selectById(id);
        if (null != userInfo) {
            menuService.setMenuInSession(session);
            session.setAttribute("userInfo", userInfo);
            return "admin/home/index";
        }
        return null;

    }

    /**
     * 个人设置
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/setting.html", method = RequestMethod.GET)
    public String setting(Model model) {
        String title = "个人设置";
        model.addAttribute("title", title);
        return "/admin/user/setting";
    }
    /**
     * 用户管理
     *
     * @param model
     * @param session
     * @return
     */
    @RequestMapping("/index.html")
    public String index(Model model, HttpSession session) {
        String title = "用户管理";
        UserInfo loginUser = (UserInfo) session.getAttribute("userInfo");
        if (null != loginUser) {
            List<UserInfo> menuList = userService.queryAllByRole(loginUser);
            model.addAttribute("userList", menuList);
        }

        model.addAttribute("title", title);
        return "/admin/user/index";
    }

    /**
     * 菜单管理
     *
     * @param model
     * @param session
     * @return
     */
    @RequestMapping("/menuManager.html")
    public String menuManager(Model model, HttpSession session) {
        String title = "菜单管理";
        UserInfo loginUser = (UserInfo) session.getAttribute("userInfo");
        if (null != loginUser) {
            List<Menu> menuList = menuMapper.queryAll();
            model.addAttribute("menuList", menuList);

        }
        model.addAttribute("title", title);
        return "/admin/menu/index";
    }

    /**
     * 角色管理
     *
     * @param model
     * @param session
     * @return
     */
    @RequestMapping("/roleManager.html")
    public String roleManager(Model model, HttpSession session) {
        String title = "角色管理";
        UserInfo loginUser = (UserInfo) session.getAttribute("userInfo");
        if (null != loginUser) {
            List<Role> roleList = roleService.queryAll();
            model.addAttribute(Param.Role_LIST, roleList);
        }

        model.addAttribute("title", title);
        return "/admin/role/index";
    }

    /**
     * 检查用户名是否可用
     *
     * @param name
     * @return
     */
    @RequestMapping(value = "/checkUsername", method = RequestMethod.GET)
    @ResponseBody
    public String checkUsername(String name) {
        if (StringUtils.isBlank(name)) {
            return BaseReturn.response(ErrorCode.PARAM_ERROR, "username不能为空");
        } else {
            return BaseReturn.response(userService.checkUsername(name));
        }

    }

    /**
     * 更新登录密码
     *
     * @param userId
     * @param password
     * @return
     */
    @RequestMapping(value = "/updatepassword", method = RequestMethod.POST)
    @ResponseBody
    public String updatepassword(String userId, String password) {
        if (StringUtils.isBlank(userId)) {
            return BaseReturn.response(ErrorCode.PARAM_ERROR, "userId不能为空");
        }
        int count = userService.updatePassword(userId, password);
        //logger.info("----------更新数据：{}条----------", count);
        if (count > 0) {
            return BaseReturn.response(ErrorCode.SUCCESS);
        } else {
            return BaseReturn.response(ErrorCode.FAILURE);
        }

    }

    /**
     * 退出登录
     *
     * @param
     * @param session
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public @ResponseBody
    ResBody logout(Object o, HttpSession session,
                   RedirectAttributes redirectAttributes) {
        ResBody resBody = new ResBody();
        UserInfo loginUser = (UserInfo) session.getAttribute(Param.SESSION_LOGIN_USER);
        try {
            if (null != loginUser) {
                //logger.info("-----用户{}退出登录-----", loginUser.getName());
                UserInfo userInfo = userInfoService.selectByName(loginUser);

                //redisService.delete(userInfo);
                Enumeration<?> e = session.getAttributeNames();
                while (e.hasMoreElements()) {
                    String sessionName = (String) e.nextElement();
                    session.removeAttribute(sessionName);
                }
            }
            resBody.setSuccess(true);LogoutVO logoutVO = new LogoutVO();
            logoutVO.setId(loginUser.getId());
            logoutVO.setLocation(RedirectEnum.HOST + RedirectEnum.LOGIN);
            resBody.setData(JSON.toJSONString(logoutVO));
        } catch (Exception e) {
            resBody.setData("server occurs exception.");
        }

        //redirectAttributes.addFlashAttribute("msg", BaseReturn.response("您已安全退出"));
        return resBody;
    }

    @RequestMapping(value = "/menuZtree", method = RequestMethod.GET)
    @ResponseBody
    public ResBody menuZtree(Model model, HttpSession session, String roleId) {
        ResBody resBody = new ResBody();
        UserInfo loginUser = (UserInfo) session.getAttribute(Param.SESSION_LOGIN_USER);
        try {
            if (null != loginUser) {
                //logger.info("-----用户{}退出登录-----", loginUser.getName());
                UserInfo userInfo = userInfoService.selectByName(loginUser);

                //redisService.delete(userInfo);
                Enumeration<?> e = session.getAttributeNames();
                while (e.hasMoreElements()) {
                    String sessionName = (String) e.nextElement();
                    session.removeAttribute(sessionName);
                }
            }
            resBody.setSuccess(true);LogoutVO logoutVO = new LogoutVO();
            logoutVO.setId(loginUser.getId());
            logoutVO.setLocation(RedirectEnum.HOST + RedirectEnum.LOGIN);
            resBody.setData(JSON.toJSONString(logoutVO));
        } catch (Exception e) {
            resBody.setData("server occurs exception.");
        }

        //redirectAttributes.addFlashAttribute("msg", BaseReturn.response("您已安全退出"));
        return resBody;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/test-cross", method = RequestMethod.POST)
    public @ResponseBody ResultBean test(HttpServletResponse response) {
        System.out.println("request received.");
        //response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        return new ResultBean("returned meaasge id : " + (int)(Math.random() * 100));
    }

    @RequestMapping(value = "/test1", method = RequestMethod.GET)
    public ResultBean test1() {
        System.out.println("request received.");
        return new ResultBean("returned meaasge id : " + (int)(Math.random() * 100));
    }
}

package com.zsf.service;

import com.zsf.domain.Menu;
import com.zsf.domain.RoleProjectSvg;
import com.zsf.domain.UserInfo;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface IMenuService {

    void setMenuInSession(HttpSession session);

    List<Menu> queryByUserId(UserInfo loginUser);

    List<Menu> queryAll();

    /**
     * 添加菜单或修改菜单
     *
     * @param menu
     * @return
     */
    public int addOrUpdateMenu(Menu menu);

    /**
     * 根据id批量删除菜单
     *
     * @param id
     * @return
     */
    public int delete(String id);

    /**
     * 查询为用户所分配的菜单
     *
     * @param loginUser
     * @return
     */
    public List<Menu> queryAll(UserInfo loginUser);

    /**
     * 根据id查询一个菜单
     *
     * @param id
     * @return
     */
    public Menu queryById(String id);

    int delete(String id, RoleProjectSvg roleProjectSvg);
}

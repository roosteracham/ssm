package com.zsf.service;

import com.zsf.domain.UserInfo;
import com.zsf.domain.Ztree;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface IZtreeService {

    List<Ztree> getMenuZtree(UserInfo loginUser);

    List<Ztree> getMenuZtreeByRoleId(UserInfo loginUser, String roleid);

    List<Ztree> getRoleZtree(UserInfo loginUser);

    List<Ztree> getRoleZtree(UserInfo loginUser,String userid);

    String menuZtree(Model model, HttpSession session, String roleId);

    List<Ztree> roleZtree(UserInfo loginUser, String userId);

    List<Ztree> roleZtree(UserInfo loginUser);
}

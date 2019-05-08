package com.zsf.service;

import com.zsf.domain.ResBody;
import com.zsf.domain.UserInfo;
import com.zsf.domain.UserSvgsDto;
import com.zsf.util.errorcode.ErrorCode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public interface IUserService {

    ResBody register(UserInfo userInfo);

    String confirm(String param, HttpServletRequest request);

    ResBody login(UserInfo user, HttpServletResponse response);

    ResBody roleManage(UserSvgsDto user);

    String userManager(UserInfo user, HttpServletRequest request, HttpSession session);

    List<UserInfo> queryAllByRole(UserInfo loginUser);

    boolean checkUsername(String username);

    int updatePassword(String userId, String password);

    UserInfo selectByName(UserInfo userInfo);
}

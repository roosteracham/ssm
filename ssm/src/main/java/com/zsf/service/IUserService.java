package com.zsf.service;

import com.zsf.domain.ResBody;
import com.zsf.domain.UserInfo;
import com.zsf.domain.UserSvgsDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IUserService {

    ResBody register(UserInfo userInfo);

    String confirm(String param, HttpServletRequest request);

    ResBody login(UserInfo user, HttpServletResponse response);

    ResBody roleManage(UserSvgsDto user);
}

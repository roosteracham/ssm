package com.zsf.service;

import com.zsf.domain.ResBody;
import com.zsf.domain.UserInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IUserService {

    ResBody register(UserInfo userInfo);

    String confirm(String param, HttpServletRequest request);

    ResBody login(UserInfo user, HttpServletResponse response);

    void sendEmail(UserInfo userinfo);
}

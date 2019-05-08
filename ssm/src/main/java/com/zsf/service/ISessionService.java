package com.zsf.service;

import com.zsf.domain.UserInfo;
import javax.servlet.http.HttpSession;

/**
 * 用来更新session中的信息
 */
public interface ISessionService {

	/**
	 * 更新session中的菜单
	 * 
	 * @param session
	 * @param loginUser
	 */
	void setMenuInSession(HttpSession session, UserInfo loginUser);

}

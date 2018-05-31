package com.zsf.service;

import com.zsf.domain.User;

public interface IUserService {
    User selectByPrimaryKey(Integer id);
    void insert(User user);
}

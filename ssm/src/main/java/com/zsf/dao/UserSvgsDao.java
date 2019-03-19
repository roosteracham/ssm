package com.zsf.dao;

import com.zsf.domain.UserSvgs;
import com.zsf.util.mapper.UserSvgsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserSvgsDao {

    @Autowired
    private UserSvgsMapper userSvgsMapper;

    public List<Integer> selectSvgsByUserId(Integer id) {
        return userSvgsMapper.selectSvgsByUserId(id);
    }

    public void insert(UserSvgs userSvgs) {
        userSvgsMapper.insertSelective(userSvgs);
    }
}

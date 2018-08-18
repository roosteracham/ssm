package com.zsf.dao;

import com.zsf.domain.UserInfo;
import com.zsf.util.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserInfoDao {

    @Autowired
    private UserInfoMapper userInfoMapper;

    public int insertSelective(UserInfo userInfo) {
        return userInfoMapper.insertSelective(userInfo);
    }

    public int insert(UserInfo userInfo) {
        return userInfoMapper.insert(userInfo);
    }

    public UserInfo selectByName(UserInfo userInfo) {
        return userInfoMapper.selectByName(userInfo);
    }

    public UserInfo selectByEmail(UserInfo userInfo) {
        return userInfoMapper.selectByEmail(userInfo);
    }

    public UserInfo selectById(int id) {
        return userInfoMapper.selectByPrimaryKey(id);
    }

    public void updataById(UserInfo userInfo) {
        userInfoMapper.updateByPrimaryKey(userInfo);
    }

    public void updataByName(UserInfo userInfo) {
        userInfoMapper.updateByName(userInfo);
    }

    public List<UserInfo> selectByChecked(int i) {
        return userInfoMapper.selectByChecked(i);
    }
}

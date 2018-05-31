package test.junit.impl;

import com.zsf.domain.User;
import com.zsf.service.IUserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import test.junit.BaseJunitTest;

public class SecondJunitImpl extends BaseJunitTest{ // 继承BaseJunitTest

    @Autowired
    private IUserService iUserService;

    @Test
    public void test() {
        System.out.println();
        User user = iUserService.selectByPrimaryKey(1);
        System.out.println(user.getName() + " " + user.getPass());
    }

    @Test
    @Rollback(false)
    public void insert() {
        User user = new User();
        user.setName("qd");
        user.setPass("qsp");
        iUserService.insert(user);
        int a = 1/0;
    }
}

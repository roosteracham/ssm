package com.zsf.util.job;

import com.zsf.business.MailBusiness;
import com.zsf.dao.RedisDao;
import com.zsf.dao.UserInfoDao;
import com.zsf.domain.UserInfo;
import com.zsf.service.IUserService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component     //开启计划任务
public class MailJob {

    @Autowired
    private UserInfoDao userInfoDao;

    @Autowired
    private MailBusiness mailBusiness;

    /**
     *  重新发送邮件
     *  五分钟一次
     */
    @Scheduled(cron = "${quartz.job.resendEmail}")
    public void resendEmail() {

        List<UserInfo> list = userInfoDao.selectByChecked(1);

        for (UserInfo userinfo :
                list) {
            mailBusiness.sendEmail(userinfo);
        }
    }

    /**
     * 过期的邮件重新发送
     * 8 14 20 点各执行一次
     */
    @Scheduled(cron = "${quartz.job.resendExpiredEmail}")
    public void resendExpiredEmail() {

        List<UserInfo> list = userInfoDao.selectByChecked(2);
        Date date = Calendar.getInstance().getTime();
        for (UserInfo user :
                list) {
            if (date.after(user.getExpireTime())) {
                Date expiredTime = DateUtils.addDays(date, 1);
                user.setExpireTime(expiredTime);
                userInfoDao.updataById(user);
                mailBusiness.sendEmail(user);
            }
        }
    }
}

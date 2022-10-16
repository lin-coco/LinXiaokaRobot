package com.lincoco.xiaokarobot.common;

import com.lincoco.xiaokarobot.model.UserIdentity;
import com.lincoco.xiaokarobot.service.DriverService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author ：xys
 * @description：TODO
 * @date ：2022/10/16
 */
@Slf4j
public class RobotTask implements Runnable{

    private DriverService driverService;

    private UserIdentity userIdentity;

    public RobotTask(DriverService driverService) {
        this.driverService = driverService;
    }

    @Override
    public void run() {
        try {
            driverService.driverDeclare(userIdentity);
        } catch (Exception e) {
            log.info(e.getMessage());
            e.printStackTrace();
            RetryTask.ROBOT_TASKS.add(this);
        }finally {
            TaskCounter.countDown();
        }
    }

    public UserIdentity getUserIdentity() {
        return userIdentity;
    }

    public void setUserIdentity(UserIdentity userIdentity) {
        this.userIdentity = userIdentity;
    }
}

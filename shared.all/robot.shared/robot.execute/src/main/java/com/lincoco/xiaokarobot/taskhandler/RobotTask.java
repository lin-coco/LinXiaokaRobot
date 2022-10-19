package com.lincoco.xiaokarobot.taskhandler;

import com.lincoco.xiaokarobot.model.UserIdentity;
import com.lincoco.xiaokarobot.service.DriverService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ：xys
 * @description：TODO
 * @date ：2022/10/16
 */
@Slf4j
public class RobotTask implements Runnable{

    private final DriverService driverService;

    private UserIdentity userIdentity;

    public RobotTask(DriverService driverService) {
        this.driverService = driverService;
    }

    @Override
    public void run() {
        try {
            long start = System.currentTimeMillis();
            driverService.driverDeclare(userIdentity);
            log.info(userIdentity.getId() + " 执行花了" + (System.currentTimeMillis() - start)/1000 + "秒");
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

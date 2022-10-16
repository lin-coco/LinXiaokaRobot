package com.lincoco.xiaokarobot.service;

import com.lincoco.xiaokarobot.model.UserIdentity;

/**
 * @author ：xys
 * @description：TODO
 * @date ：2022/10/16
 */
public interface DriverService {

    /**
     * 自动化驱动程序
     */
    void driverDeclare(UserIdentity identity) throws InterruptedException;
}

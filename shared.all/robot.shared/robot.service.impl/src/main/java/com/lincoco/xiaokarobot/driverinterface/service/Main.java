package com.lincoco.xiaokarobot.driverinterface.service;

import com.lincoco.xiaokarobot.driverinterface.service.impl.JinKeJSDriverServiceImpl;
import com.lincoco.xiaokarobot.model.UserIdentity;
import com.lincoco.xiaokarobot.service.DriverService;

/**
 * @author ：xys
 * @description：TODO
 * @date ：2022/10/30
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {

        System.setProperty("webdriver.chrome.driver","chromedriver_mac64");

        DriverService driverService = new JinKeJSDriverServiceImpl();
        UserIdentity userIdentity = new UserIdentity();
        userIdentity.setId("2012011084");
        userIdentity.setPassword("223755");
        userIdentity.setPhoneNumber("18952333672");
        driverService.driverDeclare(userIdentity);
    }
}

package com.lincoco.xiaokarobot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;

//定时任务配置
@EnableScheduling
@SpringBootApplication
@MapperScan(basePackages = {"com.lincoco.xiaokarobot.dao"})
public class LinXiaokaRobotApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext app = SpringApplication.run(LinXiaokaRobotApplication.class, args);
    }

    @PostConstruct
    public void init(){
        //设置浏览器驱动
//        System.setProperty("webdriver.edge.driver","./edgedriver_linux64/msedgedriver");
//        System.setProperty("webdriver.edge.driver","./edgedriver_mac64/msedgedriver");
        System.setProperty("webdriver.chrome.driver","/usr/local/bin/chromedriver");
//        System.setProperty("webdriver.chrome.driver","chromedriver_mac64");

    }

}

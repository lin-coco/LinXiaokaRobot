package com.lincoco.xiaokarobot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
@MapperScan(basePackages = {"com.lincoco.xiaokarobot.dao"})
public class LinXiaokaRobotApplication {

    public static void main(String[] args) {
        SpringApplication.run(LinXiaokaRobotApplication.class, args);
    }

    @PostConstruct
    public void init(){
        //设置浏览器驱动
        System.setProperty("webdriver.edge.driver","./edgedriver_mac64/msedgedriver");
    }

}

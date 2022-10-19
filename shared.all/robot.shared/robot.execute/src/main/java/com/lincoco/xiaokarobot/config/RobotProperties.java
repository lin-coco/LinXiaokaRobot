package com.lincoco.xiaokarobot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.MapPropertySource;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

/**
 * @author ：xys
 * @description：TODO
 * @date ：2022/10/16
 */
@Component
@Data
@PropertySource(value = "file:robotConfig.properties",encoding = "UTF-8")
@ConfigurationProperties(prefix = "robot")
public class RobotProperties {

    //robot拥有者id
    private String id;

    //robot每天的执行时间
    private String time;

    //robot开启线程数
    private Integer thread;

    //最大任务数
    private Integer queue;

    //robot任务异常重试次数
    private Integer retry;

    //robot通知方式 (mail,phone)
    private List<String> notices;
}

package com.lincoco.xiaokarobot.schedule;

import com.lincoco.xiaokarobot.config.RobotProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Properties;

/**
 * @author ：xys
 * @description：TODO
 * @date ：2022/10/17
 * Cron表达式参数分别表示：
 *
 * （Cron具体意思和用法请看这篇文章：https://blog.csdn.net/u013987258/article/details/106690859）
 *
 * 秒（0~59） 例如0/5表示每5秒
 * 分（0~59）
 * 时（0~23）
 * 日（0~31）的某天，需计算
 * 月（0~11）
 * 周几（ 可填1-7 或 SUN/MON/TUE/WED/THU/FRI/SAT）
 *
 * ? 表示不关心值 ， * 表示所有值
 */
@Slf4j
@Component
public class ScheduleProperties {

    @Autowired
    private RobotProperties robotProperties;

    /**
     * 配置文件定时 0点重新配置值
     */
    @Scheduled(cron = "0 0 0 * * ?")
//    @Scheduled(fixedRate = 5000)
    public void robotConfigJob() throws FileNotFoundException {
        log.info("执行静态定时任务: 配置检查" + LocalDateTime.now().toLocalTime());
        Properties properties = new Properties();
        // 使用InPutStream流读取properties文件
        BufferedReader bufferedReader = new BufferedReader(new FileReader("./robotConfig.properties"));
        //File file = new File("./robotConfig.properties");
        //InputStream stream = ScheduleProperties.class.getResourceAsStream("/Users/apple/Desktop/2022-8研发开始/lin-xiaoka-robot/robotConfig.properties");
        try {
            properties.load(bufferedReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String id = properties.getProperty("robot.id");
        String time = properties.getProperty("robot.time");
        String thread = properties.getProperty("robot.thread");
        String queue = properties.getProperty("robot.queue");
        String retry = properties.getProperty("robot.retry");
        String notices = properties.getProperty("robot.notices");
        String[] split = notices.split(",");
        robotProperties.setId(id);
        robotProperties.setTime(time);
        robotProperties.setThread(Integer.parseInt(thread));
        robotProperties.setQueue(Integer.parseInt(queue));
        robotProperties.setRetry(Integer.parseInt(retry));
        robotProperties.setNotices(Arrays.asList(split));
        log.info("配置检查结束：" + robotProperties.toString());
        log.info("scheduleTask 重新设置定时时间");
    }

}

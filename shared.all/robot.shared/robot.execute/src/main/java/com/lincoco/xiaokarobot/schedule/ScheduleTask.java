package com.lincoco.xiaokarobot.schedule;

import com.lincoco.xiaokarobot.config.RobotProperties;
import com.lincoco.xiaokarobot.taskhandler.TaskExecute;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.config.TriggerTask;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ：xys
 * @description：TODO
 * @date ：2022/10/17
 */
@Slf4j
@Component
public class ScheduleTask implements SchedulingConfigurer {

    @Autowired
    private RobotProperties robotProperties;

    @Autowired
    private TaskExecute taskExecute;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {

        taskRegistrar.addTriggerTask(
                //1.添加任务内容(Runnable)
                () -> {
                    log.info("执行动态定时任务: 自动打卡" + LocalDateTime.now().toLocalTime());
                    taskExecute.execute();
                },
                //2.设置执行周期(Trigger)
                triggerContext -> {
                    //2.1 从数据库获取执行周期
//                    String cron = robotProperties.getTime();
                    //2.2 合法性校验.
//                    if (StringUtils.isEmpty(cron)) {
//                        // Omitted Code ..
//                        cron = "0 10 0 * * ?";
//                    }
                    //CronTrigger cronTrigger = new CronTrigger(robotProperties.getTime());
                    //2.3 返回执行周期(Date)
                    // 使用CronTrigger触发器，可动态修改cron表达式来操作循环规则
                    CronTrigger cronTrigger = new CronTrigger(robotProperties.getTime());
                    log.info("使用CronTrigger触发器 : " + robotProperties.getTime());
                    return cronTrigger.nextExecutionTime(triggerContext);
                }
        );

    }
}
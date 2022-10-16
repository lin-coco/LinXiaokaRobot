package com.lincoco.xiaokarobot.common;

import com.lincoco.xiaokarobot.config.RobotTaskConfiguration;
import com.lincoco.xiaokarobot.config.RobotProperties;
import com.lincoco.xiaokarobot.model.UserIdentity;
import com.lincoco.xiaokarobot.service.DriverService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author ：xys
 * @description：TODO
 * @date ：2022/10/16
 */
@Slf4j
@Component
public class TaskExecute {

    /**
     * 配置
     */
    @Autowired
    private RobotProperties robotProperties;

    @Autowired
    private RobotTaskConfiguration robotTaskConfiguration;

    @Autowired
    private DriverService jinKeDriverServiceImpl;

    /**
     * 收集任务
     */
    @Autowired
    private CollectTask collectTask;

    public void execute(){
        Integer retry = robotProperties.getRetry();

        ThreadPoolExecutor executor = threadPoolCreater();
        List<RobotTask> tasks = getTasks();
        //初始化计数器
        TaskCounter.resetCounter(tasks.size());
        //执行
        tasks.forEach(executor::execute);
        //等待执行任务完毕
        TaskCounter.await();

        //重试retry次
        while (RetryTask.ROBOT_TASKS.size() != 0 && retry != 0){
            //先缓存下来，设置RetryTask为空
            List<RobotTask> tempTasks = new ArrayList<>(RetryTask.ROBOT_TASKS);
            RetryTask.ROBOT_TASKS.clear();
            //初始化计数器
            TaskCounter.resetCounter(tasks.size());
            tempTasks.forEach(executor::execute);
            //等待执行任务完毕
            TaskCounter.await();
            //重试次数减1
            retry--;
        }

        //重试之后还不成功的任务
        if (RetryTask.ROBOT_TASKS.size() != 0){
            //通过手机号或邮箱发送给主人或对应的学生
            log.info("一共有：" + RetryTask.ROBOT_TASKS.size() + " 个, 分别是" + RetryTask.stringList());
        }else {
            //全部成功 通知
            log.info("全部成功！");
        }
    }

    public ThreadPoolExecutor threadPoolCreater(){
        return new ThreadPoolExecutor(
                robotProperties.getThread(), //核心线程数
                robotProperties.getThread() * 2, //最大线程数
                10,
                TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(robotProperties.getQueue()),//队列
                r -> new Thread(r),//线程工厂
                new ThreadPoolExecutor.DiscardPolicy()//忽略最新的任务
        );
    }

    public List<RobotTask> getTasks(){
        List<UserIdentity> userIdentities = collectTask.collectTask();
        List<RobotTask> robotTasks = new ArrayList<>(userIdentities.size());
        for (UserIdentity identity : userIdentities) {
            if ("金陵科技学院".equals(identity.getSchool())){
                RobotTask robotTask = robotTaskConfiguration.jkTask(jinKeDriverServiceImpl);
                robotTask.setUserIdentity(identity);
                robotTasks.add(robotTask);
            }
        }
        return robotTasks;
    }
}

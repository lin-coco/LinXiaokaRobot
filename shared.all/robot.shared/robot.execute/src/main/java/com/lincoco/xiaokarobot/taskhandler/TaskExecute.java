package com.lincoco.xiaokarobot.taskhandler;

import com.lincoco.xiaokarobot.config.RobotTaskConfiguration;
import com.lincoco.xiaokarobot.config.RobotProperties;
import com.lincoco.xiaokarobot.mailsender.RobotMailSender;
import com.lincoco.xiaokarobot.model.UserIdentity;
import com.lincoco.xiaokarobot.service.DriverService;
import com.lincoco.xiaokarobot.service.UserIdentityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private DriverService jinKeJSDriverServiceImpl;

    /**
     * 收集任务
     */
    @Autowired
    private CollectTask collectTask;

    @Autowired
    private RobotMailSender robotMailSender;

    @Autowired
    private UserIdentityService userIdentityService;

    /**
     * 使用同步机制，同一时间只有 1 个正在执行
     */
    public synchronized void execute(){
        LocalDateTime start = LocalDateTime.now();
        Integer retry = robotProperties.getRetry();

        ThreadPoolExecutor executor = threadPoolCreater();
        List<RobotTask> tasks = getTasks();
//        List<RobotTask> tasks = getTasksById("2012011084");

        int taskSize = tasks.size();
        //初始化计数器
        TaskCounter.resetCounter(tasks.size());
        //执行
        tasks.forEach(executor::execute);
        //等待执行任务完毕
        TaskCounter.await();

        //重试retry次
        while (RetryTask.ROBOT_TASKS.size() != 0 && retry != 0){
            log.info("重试开始");
            //先缓存下来，设置RetryTask为空
            List<RobotTask> tempTasks = new ArrayList<>(RetryTask.ROBOT_TASKS);
            RetryTask.ROBOT_TASKS.clear();
            //初始化计数器
            TaskCounter.resetCounter(tempTasks.size());
            tempTasks.forEach(executor::execute);
            //等待执行任务完毕
            TaskCounter.await();
            //重试次数减1
            retry--;
            log.info("一次重试结束，还剩" + retry +" 此重试");
        }

        //重试之后还不成功的任务，将要通过邮件通知
        UserIdentity master = userIdentityService.getById(robotProperties.getId());
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (RetryTask.ROBOT_TASKS.size() != 0){
            //通过手机号或邮箱发送给主人或对应的学生
            log.info("一共有：" + RetryTask.ROBOT_TASKS.size() + " 个, 分别是" + RetryTask.stringList());
            //通过邮件发送给主人

            String content = RetryTask.getStringSimple() + "\n" + "外加今日自动打卡日志" + "\n" + "开始时间：" + dtf.format(start);
            RetryTask.ROBOT_TASKS.clear();
            String filePath = "./logs/info/" + now.getYear() + "-" + now.getMonthValue() + "/" + now.getYear() + "-" + now.getMonthValue() + "-" + now.getDayOfMonth() + ".0.log";
            try {
                robotMailSender.sendAttachmentMail(master.getMailbox(), "小恩提醒：截止 "+dtf.format(now) + " 自动打卡失败名单",content,filePath);
            } catch (MessagingException e) {
                e.printStackTrace();
                log.info("邮件发送异常，准备发送简单邮件");
                content = content + " ( 邮件发送异常，已改为发送简单邮件，无附件)";
                robotMailSender.sendSimpleMail(master.getMailbox(), "小恩提醒：截止 "+dtf.format(now) + " 打卡失败名单",content);
            }
        }else {
            //全部成功 通知
            log.info("一共 " +taskSize+ " 任务 全部成功！");
            //通过邮件发送
            String filePath = "./logs/info/" + now.getYear() + "-" + now.getMonthValue() + "/" + now.getYear() + "-" + now.getMonthValue() + "-" + now.getDayOfMonth() + ".0.log";
            String content = "一共 " +taskSize+ " 任务 全部成功！" + "\n" + "开始时间：" + dtf.format(start);
            try {
                robotMailSender.sendAttachmentMail(master.getMailbox(), "小恩提醒："+dtf.format(now) + " 全部成功！",content,filePath);
            } catch (MessagingException e) {
                e.printStackTrace();
                log.info("邮件发送异常，准备发送简单邮件");
                content = content + " ( 邮件发送异常，已改为发送简单邮件，无附件)";
                robotMailSender.sendSimpleMail(master.getMailbox(), "小恩提醒："+dtf.format(now) + " 全部成功！",content);
            }
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
                RobotTask robotTask = robotTaskConfiguration.jkTask(jinKeJSDriverServiceImpl);
                robotTask.setUserIdentity(identity);
                robotTasks.add(robotTask);
            }
        }
        return robotTasks;
    }

    public List<RobotTask> getTasksById(String id){
        List<UserIdentity> userIdentities = collectTask.collectTask();
        List<RobotTask> robotTasks = new ArrayList<>(userIdentities.size());
        for (UserIdentity identity : userIdentities) {
            if ("金陵科技学院".equals(identity.getSchool()) && id.equals(identity.getId())){
                RobotTask robotTask = robotTaskConfiguration.jkTask(jinKeJSDriverServiceImpl);
                robotTask.setUserIdentity(identity);
                robotTasks.add(robotTask);
            }
        }
        return robotTasks;
    }
}

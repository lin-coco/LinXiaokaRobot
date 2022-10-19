package com.lincoco.xiaokarobot;

import com.lincoco.xiaokarobot.taskhandler.TaskExecute;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ：xys
 * @description：TODO
 * @date ：2022/10/16
 */
@Slf4j
@RestController
@RequestMapping("/robot")
public class RobotApi {

    @Autowired
    private TaskExecute execute;

    @GetMapping
    public void execute(){
        execute.execute();
    }
}

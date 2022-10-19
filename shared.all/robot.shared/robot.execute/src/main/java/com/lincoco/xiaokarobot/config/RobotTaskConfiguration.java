package com.lincoco.xiaokarobot.config;

import com.lincoco.xiaokarobot.taskhandler.RobotTask;
import com.lincoco.xiaokarobot.service.DriverService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @author ：xys
 * @description：TODO
 * @date ：2022/10/16
 */
@Configuration
public class RobotTaskConfiguration {

//    @Autowired
//    private DriverService jinKeDriverServiceImpl;

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RobotTask jkTask(DriverService jinKeDriverServiceImpl){
        return new RobotTask(jinKeDriverServiceImpl);
    }
}

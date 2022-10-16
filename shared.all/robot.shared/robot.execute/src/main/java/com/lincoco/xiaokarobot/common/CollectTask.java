package com.lincoco.xiaokarobot.common;

import com.lincoco.xiaokarobot.model.UserIdentity;
import com.lincoco.xiaokarobot.service.UserIdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ：xys
 * @description：TODO
 * @date ：2022/10/16
 */
@Component
public class CollectTask {

    @Autowired
    private UserIdentityService userIdentityService;

    public List<UserIdentity> collectTask(){
        return userIdentityService.list();
    }
}

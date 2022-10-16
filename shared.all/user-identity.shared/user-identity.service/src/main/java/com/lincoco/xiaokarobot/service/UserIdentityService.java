package com.lincoco.xiaokarobot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lincoco.xiaokarobot.model.UserIdentity;

import java.util.List;

/**
 * @author ：xys
 * @description：TODO
 * @date ：2022/10/16
 */
public interface UserIdentityService extends IService<UserIdentity> {

    /**
     * 查询
     */
    List<UserIdentity> getUserIdentityPage(String id, String phoneNumber, String mailbox, Integer page, Integer count);

    /**
     * 增加
     */
    UserIdentity addUserIdentity(UserIdentity userIdentity);

    /**
     * 修改
     */
    UserIdentity updateUserIdentity(UserIdentity userIdentity);

    /**
     * 删除
     */
    boolean deleteUserIdentityById(String id);
}

package com.lincoco.xiaokarobot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lincoco.xiaokarobot.dao.UserIdentityDAO;
import com.lincoco.xiaokarobot.exception.FailedException;
import com.lincoco.xiaokarobot.model.UserIdentity;
import com.lincoco.xiaokarobot.mybatis.Page;
import com.lincoco.xiaokarobot.service.UserIdentityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ：xys
 * @description：TODO
 * @date ：2022/10/16
 */
@Slf4j
@Service
public class UserIdentityServiceImpl extends ServiceImpl<UserIdentityDAO,UserIdentity> implements UserIdentityService {


    @Override
    public List<UserIdentity> getUserIdentityPage(String id, String phoneNumber, String mailbox, Integer page, Integer count) {
        IPage<UserIdentity> ipage = page(new Page<>(page, count), new LambdaQueryWrapper<UserIdentity>()
                .eq(id != null, UserIdentity::getId, id)
                .eq(phoneNumber != null, UserIdentity::getPhoneNumber, phoneNumber)
                .eq(mailbox != null, UserIdentity::getMailbox, mailbox));
        return ipage.getRecords();
    }

    @Override
    public UserIdentity addUserIdentity(UserIdentity userIdentity) {
        if (getById(userIdentity.getId()) != null){
            throw new FailedException("增加失败，此学号已经存在");
        }
        save(userIdentity);
        return getById(userIdentity.getId());
    }

    @Override
    public UserIdentity updateUserIdentity(UserIdentity userIdentity) {
        updateById(userIdentity);
        return getById(userIdentity.getId());
    }

    @Override
    public boolean deleteUserIdentityById(String id) {
        return removeById(id);
    }
}

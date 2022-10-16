package com.lincoco.xiaokarobot.api;

import com.lincoco.xiaokarobot.model.UserIdentity;
import com.lincoco.xiaokarobot.mybatis.Page;
import com.lincoco.xiaokarobot.page.PageResponseVO;
import com.lincoco.xiaokarobot.page.PageUtil;
import com.lincoco.xiaokarobot.resultvo.CreatedVO;
import com.lincoco.xiaokarobot.resultvo.DeletedVO;
import com.lincoco.xiaokarobot.resultvo.UpdatedVO;
import com.lincoco.xiaokarobot.service.UserIdentityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author ：xys
 * @description：TODO
 * @date ：2022/10/16
 */
@Slf4j
@Validated
@RestController("/user-identity")
public class UserIdentityApi {

    @Autowired
    private UserIdentityService userIdentityService;

    @GetMapping
    public PageResponseVO<UserIdentity> getUserIdentityPage(@RequestParam(required = false) String id,
                                                            @RequestParam(required = false) String phoneNumber,
                                                            @RequestParam(required = false) String mailBox,
                                                            @RequestParam(defaultValue = "0") @Min(value = 0, message = "最小值为0") Integer page,
                                                            @RequestParam(defaultValue = "10") @Min(value = 5, message = "{page.count.min}") @Max(value = 30, message = "{page.count.max}") Integer count){
        List<UserIdentity> identityPage = userIdentityService.getUserIdentityPage(id, phoneNumber, mailBox, page, count);
        return PageUtil.build(new Page<>(page,count,identityPage.size()),identityPage);
    }

    @PostMapping
    public CreatedVO addUserIdentity(@RequestBody @Validated UserIdentity userIdentity){
        userIdentityService.addUserIdentity(userIdentity);
        return new CreatedVO(100,"增加成功");
    }

    @PutMapping
    public UpdatedVO updateUserIdentity(@RequestBody UserIdentity userIdentity){
        userIdentityService.updateUserIdentity(userIdentity);
        return new UpdatedVO(101,"修改成功");
    }

    public DeletedVO deletedUserIdentity(@RequestParam String id){
        userIdentityService.deleteUserIdentityById(id);
        return new DeletedVO(102,"删除成功");
    }
}

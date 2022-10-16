package com.lincoco.xiaokarobot.resultvo;

import com.lincoco.xiaokarobot.page.UnifyResponseVO;
import org.springframework.http.HttpStatus;

/**
 * @author ：xys
 * @description：TODO
 * @date ：2022/8/2
 */
public class UpdatedVO extends UnifyResponseVO<String> {

    public UpdatedVO(Integer code, String message) {
        super(code, message,HttpStatus.OK);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}

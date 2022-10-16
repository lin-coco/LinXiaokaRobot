package com.lincoco.xiaokarobot.resultvo;

import com.lincoco.xiaokarobot.page.UnifyResponseVO;
import org.springframework.http.HttpStatus;

/**
 * @author ：xys
 * @description：TODO
 * @date ：2022/8/1
 */
public class CreatedVO extends UnifyResponseVO<String> {

    public CreatedVO(Integer code, String message) {
        super(code, message,HttpStatus.CREATED);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}

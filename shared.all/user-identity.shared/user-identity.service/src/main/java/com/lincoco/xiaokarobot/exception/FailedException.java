package com.lincoco.xiaokarobot.exception;

/**
 * 失败异常
 *
 * @author pedro@TaleLin
 * @author Juzi@TaleLin
 * @author colorful@TaleLin
 */
public class FailedException extends RuntimeException {

    public FailedException() {
    }

    public FailedException(String message) {
        super(message);
    }
}

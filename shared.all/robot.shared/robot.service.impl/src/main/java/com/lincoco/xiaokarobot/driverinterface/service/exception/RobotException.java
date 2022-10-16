package com.lincoco.xiaokarobot.driverinterface.service.exception;

/**
 * @author ：xys
 * @description：TODO
 * @date ：2022/10/16
 */
public class RobotException extends RuntimeException{

    private String id;

    public RobotException() {
    }

    public RobotException(String message,String id) {
        super(message);
        this.id = id;
    }
}

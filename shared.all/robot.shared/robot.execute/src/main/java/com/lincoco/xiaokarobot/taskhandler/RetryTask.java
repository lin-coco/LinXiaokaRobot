package com.lincoco.xiaokarobot.taskhandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ：xys
 * @description：TODO
 * @date ：2022/10/16
 */
public class RetryTask {

    public static final List<RobotTask> ROBOT_TASKS = new ArrayList<>();

    public static String stringList() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ROBOT_TASKS.size(); i++) {
            if (i == 0){
                sb.append("[ ").append(ROBOT_TASKS.get(i).getUserIdentity().getId());
            }else if (i == ROBOT_TASKS.size()-1){
                sb.append(ROBOT_TASKS.get(i).getUserIdentity().getId()).append(" ]");
            }else {
                sb.append(ROBOT_TASKS.get(i).getUserIdentity().getId()).append(", ");
            }
        }
        return sb.toString();
    }

    public static String getStringSimple(){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ROBOT_TASKS.size(); i++) {
            if (i == 0){
                sb.append(ROBOT_TASKS.get(i).getUserIdentity().getId()).append("\n");
            }else if (i == ROBOT_TASKS.size()-1){
                sb.append(ROBOT_TASKS.get(i).getUserIdentity().getId());
            }else {
                sb.append(ROBOT_TASKS.get(i).getUserIdentity().getId()).append("\n");
            }
        }
        return sb.toString();
    }
}

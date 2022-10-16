package com.lincoco.xiaokarobot.page;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ：xys
 * @description：请求工具类
 * @date ：2022/8/2
 */
public class RequestUtil {

    /**
     * 获得当前请求
     * @return
     */
    public static HttpServletRequest getRequest(){
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        //当前线程没有绑定 Request
        if (requestAttributes == null){
            return null;
        }
        if (requestAttributes instanceof ServletRequestAttributes){
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
            return servletRequestAttributes.getRequest();
        }else {
            return null;
        }
    }

    /**
     * 获得请求url
     * @return
     */
    public static String getRequestUrl(){
        HttpServletRequest request = RequestUtil.getRequest();
        if (request == null){
            return null;
        }
        return request.getServletPath();
    }

    /**
     * 获得请求简略信息
     * @param request
     * @return
     */
    public static String getSimpleRequest(HttpServletRequest request){
        return request.getMethod() + " " + request.getServletPath();
    }

    /**
     * 获得请求简略信息
     * @return
     */
    public static String getSimpleRequest(){
        HttpServletRequest request = getRequest();
        if (request == null){
            return null;
        }
        return request.getMethod() + " " + request.getServletPath();
    }
}

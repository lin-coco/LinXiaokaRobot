package com.lincoco.xiaokarobot.page;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * @author ：xys
 * @description：TODO
 * @date ：2022/8/4
 */
public class PageUtil {

    public static <T> PageResponseVO<T> build(IPage<T> iPage){
        return new PageResponseVO<T>(Math.toIntExact(iPage.getTotal()),iPage.getRecords(),
                Math.toIntExact(iPage.getCurrent()),Math.toIntExact(iPage.getSize()));
    }

    public static <K,T> PageResponseVO<K> build(IPage<T> iPage, List<K> records){
        return new PageResponseVO<K>(Math.toIntExact(iPage.getTotal()), records,
                Math.toIntExact(iPage.getCurrent()),
                Math.toIntExact(iPage.getSize()));
    }
}

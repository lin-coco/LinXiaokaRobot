package com.lincoco.xiaokarobot.page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ：xys
 * @description：分页数据统一 view object
 * @date ：2022/8/2
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageResponseVO<T> {

    private Integer total;

    private List<T> items;

    private Integer page;

    private Integer count;
}

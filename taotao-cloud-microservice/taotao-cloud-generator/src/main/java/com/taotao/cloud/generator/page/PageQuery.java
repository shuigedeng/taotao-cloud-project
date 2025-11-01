/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.generator.page;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;



/**
 * 分页查询实体类
 *
 * @author Lion Li
 */
@Data
public class PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 分页大小 */
    private Integer pageSize;

    /** 当前页数 */
    private Integer pageNum;

    /** 排序列 */
    private String orderByColumn;

    /** 排序的方向desc或者asc */
    private String isAsc;

    /** 当前记录起始索引 默认值 */
    public static final int DEFAULT_PAGE_NUM = 1;

    /** 每页显示记录数 默认值 默认查全部 */
    public static final int DEFAULT_PAGE_SIZE = Integer.MAX_VALUE;

    public <T> Page<T> build() {
        Integer pageNum = ObjUtil.defaultIfNull(getPageNum(), DEFAULT_PAGE_NUM);
        Integer pageSize = ObjUtil.defaultIfNull(getPageSize(), DEFAULT_PAGE_SIZE);
        if (pageNum <= 0) {
            pageNum = DEFAULT_PAGE_NUM;
        }
        Page<T> page = new Page<>(pageNum, pageSize);
        List<OrderItem> orderItems = buildOrderItem();
        if (CollUtil.isNotEmpty(orderItems)) {
            page.addOrder(orderItems);
        }
        return page;
    }

    /**
     * 构建排序
     *
     * <p>支持的用法如下: {isAsc:"asc",orderByColumn:"id"} order by id asc
     * {isAsc:"asc",orderByColumn:"id,createTime"} order by id asc,create_time asc
     * {isAsc:"desc",orderByColumn:"id,createTime"} order by id desc,create_time desc
     * {isAsc:"asc,desc",orderByColumn:"id,createTime"} order by id asc,create_time desc
     */
    private List<OrderItem> buildOrderItem() {
        // if (StringUtils.isBlank(orderByColumn) || StringUtils.isBlank(isAsc)) {
        //	return null;
        // }
        // String orderBy = SqlUtil.escapeOrderBySql(orderByColumn);
        // orderBy = StringUtils.toUnderScoreCase(orderBy);
        //
        //// 兼容前端排序类型
        // isAsc = StringUtils.replaceEach(isAsc, new String[]{"ascending", "descending"},
        //	new String[]{"asc", "desc"});
        //
        // String[] orderByArr = orderBy.split(StringUtils.SEPARATOR);
        // String[] isAscArr = isAsc.split(StringUtils.SEPARATOR);
        // if (isAscArr.length != 1 && isAscArr.length != orderByArr.length) {
        //	throw new ServiceException("排序参数有误");
        // }

        List<OrderItem> list = new ArrayList<>();
        // 每个字段各自排序
        // for (int i = 0; i < orderByArr.length; i++) {
        //	String orderByStr = orderByArr[i];
        //	String isAscStr = isAscArr.length == 1 ? isAscArr[0] : isAscArr[i];
        //	if ("asc".equals(isAscStr)) {
        //		list.add(OrderItem.asc(orderByStr));
        //	} else if ("desc".equals(isAscStr)) {
        //		list.add(OrderItem.desc(orderByStr));
        //	} else {
        //		throw new ServiceException("排序参数有误");
        //	}
        // }
        return list;
    }
}

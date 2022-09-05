package com.taotao.cloud.schedule.gloriaapi.common.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gloria.schedule.common.constant.SqlConstant;
import com.gloria.schedule.common.xss.SQLFilter;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

public class Query<T> {

    public IPage<T> getPage(Map<String, Object> params) {
        return this.getPage(params, null, false);
    }

    public IPage<T> getPage(Map<String, Object> params, String defaultOrderField, boolean isAsc) {
        //分页参数
        long curPage = 1;
        long limit = 10;

        if(params.get(SqlConstant.PAGE) != null){
            curPage = Long.parseLong((String)params.get(SqlConstant.PAGE));
        }
        if(params.get(SqlConstant.LIMIT) != null){
            limit = Long.parseLong((String)params.get(SqlConstant.LIMIT));
        }

        //分页对象
        Page<T> page = new Page<T>(curPage, limit);

        //分页参数
        params.put(SqlConstant.PAGE, page);

        //排序字段
        //防止SQL注入（因为sidx、order是通过拼接SQL实现排序的，会有SQL注入风险）
        String orderField = SQLFilter.sqlInject((String)params.get(SqlConstant.ORDER_FIELD));
        String order = (String)params.get(SqlConstant.ORDER);

        //前端字段排序
        if(StringUtils.isNotEmpty(orderField) && StringUtils.isNotEmpty(order)){
            if(SqlConstant.ASC.equalsIgnoreCase(order)) {
                return  page.addOrder(OrderItem.asc(orderField));
            }else {
                return page.addOrder(OrderItem.desc(orderField));
            }
        }

        //没有排序字段，则不排序
        if(StringUtils.isBlank(defaultOrderField)){
            return page;
        }

        //默认排序
        if(isAsc) {
            page.addOrder(OrderItem.asc(defaultOrderField));
        }else {
            page.addOrder(OrderItem.desc(defaultOrderField));
        }

        return page;
    }
}

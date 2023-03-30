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

package com.taotao.cloud.order.biz.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taotao.cloud.common.enums.UserEnum;
import com.taotao.cloud.common.utils.common.SecurityUtils;
import com.taotao.cloud.common.utils.lang.StringUtils;
import com.taotao.cloud.order.api.model.page.aftersale.AfterSalePageQuery;

public class QueryUtils {

    public static <T> QueryWrapper<T> queryWrapper(AfterSalePageQuery afterSalePageQuery) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(afterSalePageQuery.getSn())) {
            queryWrapper.like("sn", afterSalePageQuery.getSn());
        }
        if (StringUtils.isNotEmpty(afterSalePageQuery.getOrderSn())) {
            queryWrapper.like("order_sn", afterSalePageQuery.getOrderSn());
        }

        // 按买家查询
        if (SecurityUtils.getCurrentUser().getType() == UserEnum.MEMBER.getCode()) {
            queryWrapper.eq("member_id", SecurityUtils.getCurrentUser().getUserId());
        }

        // 按卖家查询
        if (SecurityUtils.getCurrentUser().getType() == UserEnum.STORE.getCode()) {
            queryWrapper.eq("store_id", SecurityUtils.getCurrentUser().getStoreId());
        }

        if (SecurityUtils.getCurrentUser().getType() == UserEnum.MANAGER.getCode()
                && StringUtils.isNotEmpty(afterSalePageQuery.getStoreId())) {
            queryWrapper.eq("store_id", afterSalePageQuery.getStoreId());
        }
        if (StringUtils.isNotEmpty(afterSalePageQuery.getMemberName())) {
            queryWrapper.like("member_name", afterSalePageQuery.getMemberName());
        }
        if (StringUtils.isNotEmpty(afterSalePageQuery.getStoreName())) {
            queryWrapper.like("store_name", afterSalePageQuery.getStoreName());
        }
        if (StringUtils.isNotEmpty(afterSalePageQuery.getGoodsName())) {
            queryWrapper.like("goods_name", afterSalePageQuery.getGoodsName());
        }
        // 按时间查询
        if (afterSalePageQuery.getStartDate() != null) {
            queryWrapper.ge("create_time", afterSalePageQuery.getStartDate());
        }
        if (afterSalePageQuery.getEndDate() != null) {
            queryWrapper.le("create_time", afterSalePageQuery.getEndDate());
        }
        if (StringUtils.isNotEmpty(afterSalePageQuery.getServiceStatus())) {
            queryWrapper.eq("service_status", afterSalePageQuery.getServiceStatus());
        }
        if (StringUtils.isNotEmpty(afterSalePageQuery.getServiceType())) {
            queryWrapper.eq("service_type", afterSalePageQuery.getServiceType());
        }
        betweenWrapper(queryWrapper, afterSalePageQuery);
        queryWrapper.eq("delete_flag", false);
        return queryWrapper;
    }

    private static <T> void betweenWrapper(
            QueryWrapper<T> queryWrapper, AfterSalePageQuery afterSalePageQuery) {
        if (StringUtils.isNotEmpty(afterSalePageQuery.getApplyRefundPrice())) {
            String[] s = afterSalePageQuery.getApplyRefundPrice().split("_");
            if (s.length > 1) {
                queryWrapper.ge("apply_refund_price", s[1]);
            } else {
                queryWrapper.le("apply_refund_price", s[0]);
            }
        }
        if (StringUtils.isNotEmpty(afterSalePageQuery.getActualRefundPrice())) {
            String[] s = afterSalePageQuery.getActualRefundPrice().split("_");
            if (s.length > 1) {
                queryWrapper.ge("actual_refund_price", s[1]);
            } else {
                queryWrapper.le("actual_refund_price", s[0]);
            }
        }
    }
}

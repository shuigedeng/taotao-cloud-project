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

package com.taotao.cloud.store.biz.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taotao.cloud.common.utils.date.DateUtils;
import com.taotao.cloud.common.utils.lang.StringUtils;
import com.taotao.cloud.store.api.enums.StoreStatusEnum;
import com.taotao.cloud.store.api.model.query.StorePageQuery;

public class QueryUtil {

    public static <T> QueryWrapper<T> queryWrapper(StorePageQuery storePageQuery) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(storePageQuery.getStoreName())) {
            queryWrapper.like("store_name", storePageQuery.getStoreName());
        }
        if (StringUtils.isNotEmpty(storePageQuery.getMemberName())) {
            queryWrapper.like("member_name", storePageQuery.getMemberName());
        }
        if (StringUtils.isNotEmpty(storePageQuery.getStoreDisable())) {
            queryWrapper.eq("store_disable", storePageQuery.getStoreDisable());
        } else {
            queryWrapper
                    .eq("store_disable", StoreStatusEnum.OPEN.name())
                    .or()
                    .eq("store_disable", StoreStatusEnum.CLOSED.name());
        }
        // 按时间查询
        if (StringUtils.isNotEmpty(storePageQuery.getStartDate())) {
            queryWrapper.ge("create_time", DateUtils.parse(storePageQuery.getStartDate()));
        }
        if (StringUtils.isNotEmpty(storePageQuery.getEndDate())) {
            queryWrapper.le("create_time", DateUtils.parse(storePageQuery.getEndDate()));
        }
        return queryWrapper;
    }
}

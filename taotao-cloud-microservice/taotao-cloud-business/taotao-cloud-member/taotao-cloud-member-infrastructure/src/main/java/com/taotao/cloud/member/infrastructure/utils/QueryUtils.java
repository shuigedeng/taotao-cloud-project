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

package com.taotao.cloud.member.biz.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taotao.cloud.common.utils.lang.StringUtils;
import com.taotao.cloud.member.sys.model.page.EvaluationPageQuery;
import java.util.Objects;

/**
 * 查询跑龙套
 *
 * @author shuigedeng
 * @version 2023.01
 * @since 2023-02-01 14:05:22
 */
public class QueryUtils {

    /**
     * 构造查询条件
     *
     * @return 查询条件
     * @author shuigedeng
     * @since 2022/3/14 11:22
     */
    public static <T> QueryWrapper<T> evaluationQueryWrapper(EvaluationPageQuery evaluationPageQuery) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(evaluationPageQuery.getStartTime())
                && StringUtils.isNotEmpty(evaluationPageQuery.getEndTime())) {
            queryWrapper.between("create_time", evaluationPageQuery.getStartTime(), evaluationPageQuery.getEndTime());
        }
        if (StringUtils.isNotEmpty(evaluationPageQuery.getGrade())) {
            queryWrapper.eq("grade", evaluationPageQuery.getGrade());
        }
        if (StringUtils.isNotEmpty(evaluationPageQuery.getGoodsName())) {
            queryWrapper.like("goods_name", evaluationPageQuery.getGoodsName());
        }
        if (StringUtils.isNotEmpty(evaluationPageQuery.getStoreName())) {
            queryWrapper.like("store_name", evaluationPageQuery.getStoreName());
        }
        if (StringUtils.isNotEmpty(evaluationPageQuery.getMemberName())) {
            queryWrapper.like("member_name", evaluationPageQuery.getMemberName());
        }
        if (Objects.nonNull(evaluationPageQuery.getGoodsId())) {
            queryWrapper.eq("goods_id", evaluationPageQuery.getGoodsId());
        }
        if (Objects.nonNull(evaluationPageQuery.getStoreId())) {
            queryWrapper.eq("store_id", evaluationPageQuery.getStoreId());
        }
        if (Objects.nonNull(evaluationPageQuery.getMemberId())) {
            queryWrapper.eq("member_id", evaluationPageQuery.getMemberId());
        }
        if (StringUtils.isNotEmpty(evaluationPageQuery.getHaveImage())) {
            queryWrapper.eq("have_image", evaluationPageQuery.getHaveImage());
        }
        if (StringUtils.isNotEmpty(evaluationPageQuery.getStatus())) {
            queryWrapper.eq("status", evaluationPageQuery.getStatus());
        }
        queryWrapper.eq("delete_flag", false);
        queryWrapper.orderByDesc("create_time");
        return queryWrapper;
    }
}

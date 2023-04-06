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

package com.taotao.cloud.payment.biz.bootx.core.payment.dao;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.taotao.cloud.common.model.PageQuery;
import com.taotao.cloud.payment.biz.bootx.code.pay.PayStatusCode;
import com.taotao.cloud.payment.biz.bootx.core.payment.entity.Payment;
import com.taotao.cloud.payment.biz.bootx.param.payment.PaymentQuery;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentManager extends BaseManager<PaymentMapper, Payment> {

    /** 按业务ID顺序按创建时间Desc查找非取消的支付单 */
    public List<Payment> findByBusinessIdNoCancelDesc(String businessId) {
        // "FROM Payment WHERE businessId = ?1 AND payStatus  ?2 order by createTime description"

        return lambdaQuery()
                .eq(Payment::getBusinessId, businessId)
                .notIn(Payment::getPayStatus, PayStatusCode.TRADE_CANCEL)
                .orderByDesc(Payment::getId)
                .list();
    }

    /** 按业务ID顺序按创建时间Desc查找的支付单 */
    public List<Payment> findByBusinessIdDesc(String businessId) {
        // "FROM Payment WHERE businessId = ?1  order by createTime description"
        return lambdaQuery()
                .eq(Payment::getBusinessId, businessId)
                .orderByDesc(Payment::getId)
                .list();
    }

    /** 根据用户查询 */
    public List<Payment> findByUserId(Long userId) {
        return this.findAllByField(Payment::getUserId, userId);
    }

    /** 分页查询 */
    public Page<Payment> page(PageQuery PageQuery, PaymentQuery param, OrderParam orderParam) {
        Page<Payment> mpPage = MpUtil.getMpPage(PageQuery, Payment.class);
        return query().orderBy(
                        Objects.nonNull(orderParam.getSortField()),
                        orderParam.isAsc(),
                        StrUtil.toUnderlineCase(orderParam.getSortField()))
                .like(Objects.nonNull(param.getPaymentId()), MpUtil.getColumnName(Payment::getId), param.getPaymentId())
                .like(
                        Objects.nonNull(param.getBusinessId()),
                        MpUtil.getColumnName(Payment::getBusinessId),
                        param.getBusinessId())
                .like(Objects.nonNull(param.getTitle()), MpUtil.getColumnName(Payment::getTitle), param.getTitle())
                .page(mpPage);
    }
    /** 分页查询 */
    public Page<Payment> superPage(PageQuery PageQuery, QueryParams queryParams) {
        QueryWrapper<Payment> wrapper = QueryGenerator.generator(queryParams);
        Page<Payment> mpPage = MpUtil.getMpPage(PageQuery, Payment.class);
        return this.page(mpPage, wrapper);
    }
}

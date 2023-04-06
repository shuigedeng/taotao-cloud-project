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

package com.taotao.cloud.payment.biz.bootx.core.payment.service;

import cn.hutool.core.collection.CollectionUtil;
import com.taotao.cloud.common.model.PageQuery;
import com.taotao.cloud.payment.biz.bootx.core.payment.dao.PaymentManager;
import com.taotao.cloud.payment.biz.bootx.dto.payment.PayChannelInfo;
import com.taotao.cloud.payment.biz.bootx.dto.payment.PaymentDto;
import com.taotao.cloud.payment.biz.bootx.param.payment.PaymentQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 支付单查询
 *
 * @author xxm
 * @date 2021/6/28
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentQueryService {
    private final PaymentManager paymentManager;

    /** 根据支付Id查询支付单 */
    public PaymentDto findById(Long id) {
        return paymentManager.findById(id).map(Payment::toDto).orElseThrow(DataNotExistException::new);
    }

    /** 根据业务ID获取成功记录 */
    public List<PaymentDto> findByBusinessId(String businessId) {
        return paymentManager.findByBusinessIdDesc(businessId).stream()
                .map(Payment::toDto)
                .collect(Collectors.toList());
    }

    /** 根据业务ID获取支付状态 */
    public Integer findStatusByBusinessId(String businessId) {
        // 根据订单查询支付记录
        List<Payment> payments = paymentManager.findByBusinessIdNoCancelDesc(businessId);
        if (!CollectionUtil.isEmpty(payments)) {
            Payment payment = payments.get(0);
            return payment.getPayStatus();
        }
        return -1;
    }

    /** 根据businessId获取订单支付方式 */
    public List<PayChannelInfo> findPayTypeInfoByBusinessId(String businessId) {
        List<Payment> payments = paymentManager.findByBusinessIdDesc(businessId);
        return payments.stream().findFirst().map(Payment::getPayChannelInfoList).orElse(new ArrayList<>(1));
    }

    /** 根据id获取订单支付方式 */
    public List<PayChannelInfo> findPayTypeInfoById(Long id) {
        return paymentManager.findById(id).map(Payment::getPayChannelInfoList).orElse(new ArrayList<>(1));
    }

    /** 根据用户id查询 */
    public List<PaymentDto> findByUser(Long userId) {
        return paymentManager.findByUserId(userId).stream().map(Payment::toDto).collect(Collectors.toList());
    }

    /** 分页 */
    public PageResult<PaymentDto> page(PageQuery PageQuery, PaymentQuery param, OrderParam orderParam) {
        return MpUtil.convert2DtoPageResult(paymentManager.page(PageQuery, param, orderParam));
    }

    /** 超级查询 */
    public PageResult<PaymentDto> superPage(PageQuery PageQuery, QueryParams queryParams) {
        return MpUtil.convert2DtoPageResult(paymentManager.superPage(PageQuery, queryParams));
    }
}

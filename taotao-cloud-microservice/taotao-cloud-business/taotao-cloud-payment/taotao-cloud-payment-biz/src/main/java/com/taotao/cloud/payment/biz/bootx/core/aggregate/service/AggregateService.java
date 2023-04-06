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

package com.taotao.cloud.payment.biz.bootx.core.aggregate.service;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.taotao.cloud.payment.biz.bootx.code.pay.PayChannelCode;
import com.taotao.cloud.payment.biz.bootx.core.aggregate.entity.AggregatePayInfo;
import com.taotao.cloud.payment.biz.bootx.exception.payment.PayFailureException;
import com.taotao.cloud.payment.biz.bootx.param.cashier.CashierSinglePayParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 聚合支付
 *
 * @author xxm
 * @date 2022/3/6
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AggregateService {
    private final RedisClient redisClient;
    private final String PREFIX_KEY = "cashier:pay:aggregate:";

    /** 创建聚合支付QR支付, 单渠道 */
    public String createAggregatePay(CashierSinglePayParam param) {
        // 保存并生成code
        AggregatePayInfo aggregatePayInfo = new AggregatePayInfo()
                .setAmount(param.getAmount())
                .setTitle(param.getTitle())
                .setBusinessId(param.getBusinessId());
        String key = RandomUtil.randomString(10);
        redisClient.setWithTimeout(PREFIX_KEY + key, JSONUtil.toJsonStr(aggregatePayInfo), 2 * 60 * 1000);
        return key;
    }

    /** 聚合付款码支付处理 */
    public int getPayChannel(String authCode) {
        if (StrUtil.isBlank(authCode)) {
            throw new PayFailureException("付款码不可为空");
        }
        String[] wx = {"10", "11", "12", "13", "14", "15"};
        String[] ali = {"25", "26", "27", "28", "29", "30"};

        // 微信
        if (StrUtil.startWithAny(authCode.substring(0, 2), wx)) {
            return PayChannelCode.WECHAT;
        }
        // 支付宝
        else if (StrUtil.startWithAny(authCode.substring(0, 2), ali)) {
            return PayChannelCode.ALI;
        } else {
            throw new PayFailureException("不支持的支付方式");
        }
    }
}

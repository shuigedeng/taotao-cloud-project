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

package com.taotao.cloud.payment.biz.demo.test;

import com.taotao.boot.common.utils.log.LogUtils;
import com.yungouos.pay.black.PayBlack;

/**
 * 黑名单API调用演示
 *
 * @author YunGouOS技术部-029
 */
public class PayBlackTest {

    public static void main(String[] args) {
        String mchId = "微信支付商户号";
        String key = "微信支付支付密钥";
        String account = "一般是openid或支付宝buyer_id";
        PayBlack.create(mchId, account, "羊毛党", "2021-06-25 11:10:23", key);
        LogUtils.info("黑名单添加成功");

        boolean check = PayBlack.check(mchId, account, key);
        LogUtils.info("是否黑名单：" + check);
    }
}

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

import com.yungouos.pay.common.PayException;
import com.yungouos.pay.merge.MergePay;

public class MergePayTest {

    public static void main(String[] args) {
        try {
            String mchId = "聚合支付商户号";
            String key = "聚合支付密钥";
            String url = MergePay.nativePay(
                    System.currentTimeMillis() + "",
                    "0.01",
                    mchId,
                    "一码付测试",
                    "2",
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    key);
            LogUtils.info(url);
        } catch (PayException e) {
            LogUtils.error(e);
        }
    }
}

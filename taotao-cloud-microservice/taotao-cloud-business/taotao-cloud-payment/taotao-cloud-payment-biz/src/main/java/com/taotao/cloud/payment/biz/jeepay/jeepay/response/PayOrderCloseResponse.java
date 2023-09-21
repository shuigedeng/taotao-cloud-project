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

package com.taotao.cloud.payment.biz.jeepay.jeepay.response;

import com.taotao.cloud.payment.biz.jeepay.jeepay.model.PayOrderCloseResModel;

/**
 * Jeepay支付 关闭订单响应实现
 *
 * @author xiaoyu
 * @site https://www.jeequan.com
 * @since 2022/1/25 9:56
 */
public class PayOrderCloseResponse extends JeepayResponse {

    private static final long serialVersionUID = 7654172640802954221L;

    public PayOrderCloseResModel get() {
        if (getData() == null) return new PayOrderCloseResModel();
        return getData().toJavaObject(PayOrderCloseResModel.class);
    }
}

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

import com.jeequan.jeepay.model.RefundOrderQueryResModel;

/**
 * Jeepay退款查单响应实现
 *
 * @author jmdhappy
 * @site https://www.jeepay.vip
 * @since 2021-06-18 12:00
 */
public class RefundOrderQueryResponse extends JeepayResponse {

    private static final long serialVersionUID = 7654172640802954221L;

    public RefundOrderQueryResModel get() {
        if (getData() == null) return new RefundOrderQueryResModel();
        return getData().toJavaObject(RefundOrderQueryResModel.class);
    }
}

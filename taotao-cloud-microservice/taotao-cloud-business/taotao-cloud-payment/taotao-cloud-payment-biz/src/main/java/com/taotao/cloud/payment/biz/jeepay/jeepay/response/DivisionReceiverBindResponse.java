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

import com.taotao.cloud.payment.biz.jeepay.jeepay.model.DivisionReceiverBindResModel;

/***
 * 分账账号的绑定响应实现
 *
 * @author terrfly
 * @site https://www.jeepay.vip
 * @since 2021/8/25 10:35
 */
public class DivisionReceiverBindResponse extends JeepayResponse {

    private static final long serialVersionUID = 7419683269497002904L;

    public DivisionReceiverBindResModel get() {
        if (getData() == null) return new DivisionReceiverBindResModel();
        return getData().toJavaObject(DivisionReceiverBindResModel.class);
    }

    @Override
    public boolean isSuccess(String apiKey) {
        if (super.isSuccess(apiKey)) {
            int state = get().getBindState();
            return state == 1;
        }
        return false;
    }
}

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

package com.taotao.cloud.payment.biz.jeepay.pay.rqrs.payorder.payway;

import com.taotao.cloud.payment.biz.jeepay.pay.rqrs.payorder.UnifiedOrderRS;
import lombok.Data;

/*
 * 支付方式： YSF_JSAPI
 *
 * @author pangxiaoyu
 * @site https://www.jeequan.com
 * @date 2021/6/8 17:34
 */
@Data
public class YsfJsapiOrderRS extends UnifiedOrderRS {

    /** 调起支付插件的云闪付订单号 * */
    private String redirectUrl;

    @Override
    public String buildPayDataType() {
        return CS.PAY_DATA_TYPE.YSF_APP;
    }

    @Override
    public String buildPayData() {
        return JsonKit.newJson("redirectUrl", redirectUrl).toString();
    }
}

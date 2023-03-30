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

package com.taotao.cloud.payment.biz.jeepay.core.model.params.xxpay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taotao.cloud.payment.biz.jeepay.core.model.params.NormalMchParams;
import com.taotao.cloud.payment.biz.jeepay.core.utils.StringKit;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/*
 * 小新支付 普通商户参数定义
 *
 * @author jmdhappy
 * @site https://www.jeequan.com
 * @date 2021/9/10 21:51
 */
@Data
public class XxpayNormalMchParams extends NormalMchParams {

    /** 商户号 */
    private String mchId;

    /** 私钥 */
    private String key;

    /** 支付网关地址 */
    private String payUrl;

    @Override
    public String deSenData() {
        XxpayNormalMchParams mchParams = this;
        if (StringUtils.isNotBlank(this.key)) {
            mchParams.setKey(StringKit.str2Star(this.key, 4, 4, 6));
        }
        return ((JSONObject) JSON.toJSON(mchParams)).toJSONString();
    }
}

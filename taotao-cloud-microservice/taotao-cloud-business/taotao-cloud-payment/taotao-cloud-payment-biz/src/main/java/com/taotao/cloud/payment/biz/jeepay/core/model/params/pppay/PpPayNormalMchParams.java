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

package com.taotao.cloud.payment.biz.jeepay.core.model.params.pppay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taotao.cloud.payment.biz.jeepay.core.model.params.NormalMchParams;
import com.taotao.cloud.payment.biz.jeepay.core.utils.StringKit;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * none.
 *
 * @author 陈泉
 * @package com.jeequan.jeepay.core.model.params.pppay
 * @create 2021/11/15 18:10
 */
@Data
public class PpPayNormalMchParams extends NormalMchParams {
    /** 是否沙箱环境 */
    private Byte sandbox;

    /** clientId 客户端 ID */
    private String clientId;

    /** secret 密钥 */
    private String secret;

    /** 支付 Webhook 通知 ID */
    private String notifyWebhook;

    /** 退款 Webhook 通知 ID */
    private String refundWebhook;

    @Override
    public String deSenData() {
        PpPayNormalMchParams mchParams = this;
        if (StringUtils.isNotBlank(this.secret)) {
            mchParams.setSecret(StringKit.str2Star(this.secret, 6, 6, 6));
        }
        return ((JSONObject) JSON.toJSON(mchParams)).toJSONString();
    }
}

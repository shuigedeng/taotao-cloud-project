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

package com.taotao.cloud.payment.biz.jeepay.core.model.params.wxpay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taotao.cloud.payment.biz.jeepay.core.model.params.NormalMchParams;
import com.taotao.cloud.payment.biz.jeepay.core.utils.StringKit;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/*
 * 微信官方支付 配置参数
 *
 * @author zhuxiao
 * @site https://www.jeequan.com
 * @date 2021/6/8 18:02
 */
@Data
public class WxpayNormalMchParams extends NormalMchParams {

    /** 应用App ID */
    private String appId;

    /** 应用AppSecret */
    private String appSecret;

    /** 微信支付商户号 */
    private String mchId;

    /** oauth2地址 */
    private String oauth2Url;

    /** API密钥 */
    private String key;

    /** 微信支付API版本 */
    private String apiVersion;

    /** API V3秘钥 */
    private String apiV3Key;

    /** 序列号 */
    private String serialNo;

    /** API证书(.p12格式) */
    private String cert;

    /** 证书文件(.pem格式) * */
    private String apiClientCert;

    /** 私钥文件(.pem格式) */
    private String apiClientKey;

    @Override
    public String deSenData() {
        WxpayNormalMchParams mchParams = this;
        if (StringUtils.isNotBlank(this.appSecret)) {
            mchParams.setAppSecret(StringKit.str2Star(this.appSecret, 4, 4, 6));
        }
        if (StringUtils.isNotBlank(this.key)) {
            mchParams.setKey(StringKit.str2Star(this.key, 4, 4, 6));
        }
        if (StringUtils.isNotBlank(this.apiV3Key)) {
            mchParams.setApiV3Key(StringKit.str2Star(this.apiV3Key, 4, 4, 6));
        }
        if (StringUtils.isNotBlank(this.serialNo)) {
            mchParams.setSerialNo(StringKit.str2Star(this.serialNo, 4, 4, 6));
        }
        return ((JSONObject) JSON.toJSON(mchParams)).toJSONString();
    }
}

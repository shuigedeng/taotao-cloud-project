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

package com.taotao.cloud.sys.api.feign.response.setting;

import lombok.Data;
import lombok.experimental.Accessors;

/** 微信支付设置 */
@Data
@Accessors(chain = true)
public class WechatPaymentSettingApiResponse {

    /** APP应用id */
    private String appId;
    /** 小程序应用id */
    private String mpAppId;
    /** 服务号应用id */
    private String serviceAppId;
    /** 商户号 */
    private String mchId;
    /** 私钥 */
    private String apiclient_key;
    /** pem 证书 */
    private String apiclient_cert_pem;
    /** p12 证书 */
    private String apiclient_cert_p12;
    /** 商户证书序列号 */
    private String serialNumber;
    /** apiv3私钥 */
    private String apiKey3;
}

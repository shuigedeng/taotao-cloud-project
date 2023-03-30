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

package com.taotao.cloud.payment.biz.jeepay.core.model.params.alipay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taotao.cloud.payment.biz.jeepay.core.model.params.IsvParams;
import com.taotao.cloud.payment.biz.jeepay.core.utils.StringKit;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/*
 * 支付宝 isv参数定义
 *
 * @author terrfly
 * @site https://www.jeequan.com
 * @date 2021/6/8 16:34
 */
@Data
public class AlipayIsvParams extends IsvParams {

    /** 是否沙箱环境 */
    private Byte sandbox;

    /** pid */
    private String pid;

    /** appId */
    private String appId;

    /** privateKey */
    private String privateKey;

    /** alipayPublicKey */
    private String alipayPublicKey;

    /** 签名方式 * */
    private String signType;

    /** 是否使用证书方式 * */
    private Byte useCert;

    /** app 证书 * */
    private String appPublicCert;

    /** 支付宝公钥证书（.crt格式） * */
    private String alipayPublicCert;

    /** 支付宝根证书 * */
    private String alipayRootCert;

    @Override
    public String deSenData() {

        AlipayIsvParams isvParams = this;
        if (StringUtils.isNotBlank(this.privateKey)) {
            isvParams.setPrivateKey(StringKit.str2Star(this.privateKey, 4, 4, 6));
        }
        if (StringUtils.isNotBlank(this.alipayPublicKey)) {
            isvParams.setAlipayPublicKey(StringKit.str2Star(this.alipayPublicKey, 6, 6, 6));
        }
        return ((JSONObject) JSON.toJSON(isvParams)).toJSONString();
    }
}

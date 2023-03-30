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

package com.taotao.cloud.payment.biz.jeepay.pay.rqrs.payorder;

import com.taotao.cloud.payment.biz.jeepay.core.constants.CS;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/*
 * 通用支付数据RS
 * 根据set的值，响应不同的payDataType
 *
 * @author terrfly
 * @site https://www.jeequan.com
 * @date 2021/6/8 17:32
 */
@Data
public class CommonPayDataRS extends UnifiedOrderRS {

    /** 跳转地址 * */
    private String payUrl;

    /** 二维码地址 * */
    private String codeUrl;

    /** 二维码图片地址 * */
    private String codeImgUrl;

    /** 表单内容 * */
    private String formContent;

    @Override
    public String buildPayDataType() {

        if (StringUtils.isNotEmpty(payUrl)) {
            return CS.PAY_DATA_TYPE.PAY_URL;
        }

        if (StringUtils.isNotEmpty(codeUrl)) {
            return CS.PAY_DATA_TYPE.CODE_URL;
        }

        if (StringUtils.isNotEmpty(codeImgUrl)) {
            return CS.PAY_DATA_TYPE.CODE_IMG_URL;
        }

        if (StringUtils.isNotEmpty(formContent)) {
            return CS.PAY_DATA_TYPE.FORM;
        }

        return CS.PAY_DATA_TYPE.PAY_URL;
    }

    @Override
    public String buildPayData() {

        if (StringUtils.isNotEmpty(payUrl)) {
            return payUrl;
        }

        if (StringUtils.isNotEmpty(codeUrl)) {
            return codeUrl;
        }

        if (StringUtils.isNotEmpty(codeImgUrl)) {
            return codeImgUrl;
        }

        if (StringUtils.isNotEmpty(formContent)) {
            return formContent;
        }

        return "";
    }
}

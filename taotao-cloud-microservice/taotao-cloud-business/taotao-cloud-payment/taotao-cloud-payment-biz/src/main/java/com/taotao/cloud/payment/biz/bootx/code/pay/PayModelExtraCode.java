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

package com.taotao.cloud.payment.biz.bootx.code.pay;

/**
 * 支付方式扩展字段
 *
 * @author xxm
 * @date 2022/2/27
 */
public interface PayModelExtraCode {

    /** 付款码 */
    String AUTH_CODE = "authCode";

    /** 单张储值卡 */
    String VOUCHER_NO = "voucherNo";

    /** 同步通知路径 支付完成跳转的页面地址 */
    String RETURN_URL = "returnUrl";
}

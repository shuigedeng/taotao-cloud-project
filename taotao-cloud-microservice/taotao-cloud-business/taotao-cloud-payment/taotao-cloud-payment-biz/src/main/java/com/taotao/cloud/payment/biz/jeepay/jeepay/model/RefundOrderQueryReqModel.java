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

package com.taotao.cloud.payment.biz.jeepay.jeepay.model;

/**
 * 退款查单请求实体类
 *
 * @author jmdhappy
 * @site https://www.jeepay.vip
 * @since 2021-06-18 10:00
 */
public class RefundOrderQueryReqModel extends JeepayObject {

    private static final long serialVersionUID = -5184554341263929245L;

    /** 商户号 */
    @ApiField("mchNo")
    private String mchNo;
    /** 应用ID */
    @ApiField("appId")
    private String appId;
    /** 商户退款单号 */
    @ApiField("mchRefundNo")
    String mchRefundNo;
    /** 支付系统退款订单号 */
    @ApiField("refundOrderId")
    String refundOrderId;

    public String getMchNo() {
        return mchNo;
    }

    public void setMchNo(String mchNo) {
        this.mchNo = mchNo;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMchRefundNo() {
        return mchRefundNo;
    }

    public void setMchRefundNo(String mchRefundNo) {
        this.mchRefundNo = mchRefundNo;
    }

    public String getRefundOrderId() {
        return refundOrderId;
    }

    public void setRefundOrderId(String refundOrderId) {
        this.refundOrderId = refundOrderId;
    }
}

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
 * 支付下单响应实体类
 *
 * @author jmdhappy
 * @site https://www.jeepay.vip
 * @since 2021-06-08 11:00
 */
public class PayOrderCreateResModel extends JeepayObject {

    /** 支付单号(网关生成) */
    private String payOrderId;

    /** 商户单号(商户系统生成) */
    private String mchOrderNo;

    /** 订单状态 0-订单生成 1-支付中 2-支付成功 3-支付失败 4-已撤销 5-已退款 6-订单关闭 */
    private Integer orderState;

    /** 支付参数类型 */
    private String payDataType;

    /** 支付参数 */
    private String payData;

    /** 支付渠道错误码 */
    private String errCode;

    /** 支付渠道错误信息 */
    private String errMsg;

    public String getPayOrderId() {
        return payOrderId;
    }

    public void setPayOrderId(String payOrderId) {
        this.payOrderId = payOrderId;
    }

    public String getMchOrderNo() {
        return mchOrderNo;
    }

    public void setMchOrderNo(String mchOrderNo) {
        this.mchOrderNo = mchOrderNo;
    }

    public Integer getOrderState() {
        return orderState;
    }

    public void setOrderState(Integer orderState) {
        this.orderState = orderState;
    }

    public String getPayDataType() {
        return payDataType;
    }

    public void setPayDataType(String payDataType) {
        this.payDataType = payDataType;
    }

    public String getPayData() {
        return payData;
    }

    public void setPayData(String payData) {
        this.payData = payData;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}

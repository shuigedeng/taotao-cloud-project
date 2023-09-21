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
 * 退款查单响应实体类
 *
 * @author jmdhappy
 * @site https://www.jeepay.vip
 * @since 2021-06-18 10:00
 */
public class RefundOrderQueryResModel extends JeepayObject {

    private static final long serialVersionUID = -5184554341263929245L;

    /** 退款订单号（支付系统生成订单号） */
    private String refundOrderId;

    /** 支付订单号 */
    private String payOrderId;

    /** 商户号 */
    private String mchNo;

    /** 应用ID */
    private String appId;

    /** 商户退款单号 */
    private String mchRefundNo;

    /** 支付金额,单位分 */
    private Long payAmount;

    /** 退款金额,单位分 */
    private Long refundAmount;

    /** 三位货币代码,人民币:cny */
    private String currency;

    /** 退款状态 0-订单生成 1-退款中 2-退款成功 3-退款失败 4-退款关闭 */
    private Byte state;

    /** 渠道订单号 */
    private String channelOrderNo;

    /** 渠道错误码 */
    private String errCode;

    /** 渠道错误描述 */
    private String errMsg;

    /** 扩展参数 */
    private String extParam;

    /** 订单创建时间,13位时间戳 */
    private Long createdAt;

    /** 订单支付成功时间,13位时间戳 */
    private Long successTime;

    public String getRefundOrderId() {
        return refundOrderId;
    }

    public void setRefundOrderId(String refundOrderId) {
        this.refundOrderId = refundOrderId;
    }

    public String getPayOrderId() {
        return payOrderId;
    }

    public void setPayOrderId(String payOrderId) {
        this.payOrderId = payOrderId;
    }

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

    public Long getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(Long payAmount) {
        this.payAmount = payAmount;
    }

    public Long getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(Long refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public String getChannelOrderNo() {
        return channelOrderNo;
    }

    public void setChannelOrderNo(String channelOrderNo) {
        this.channelOrderNo = channelOrderNo;
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

    public String getExtParam() {
        return extParam;
    }

    public void setExtParam(String extParam) {
        this.extParam = extParam;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getSuccessTime() {
        return successTime;
    }

    public void setSuccessTime(Long successTime) {
        this.successTime = successTime;
    }
}

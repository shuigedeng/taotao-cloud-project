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
 * 支付下单请求实体类
 *
 * @author jmdhappy
 * @site https://www.jeepay.vip
 * @since 2021-06-08 11:00
 */
public class PayOrderCreateReqModel extends JeepayObject {

    private static final long serialVersionUID = -3998573128290306948L;

    @ApiField("mchNo")
    private String mchNo; // 商户号

    @ApiField("appId")
    private String appId; // 应用ID

    @ApiField("mchOrderNo")
    String mchOrderNo; // 商户订单号

    @ApiField("wayCode")
    String wayCode; // 支付方式

    @ApiField("amount")
    Long amount; // 支付金额

    @ApiField("currency")
    String currency; // 货币代码，当前只支持cny

    @ApiField("clientIp")
    String clientIp; // 客户端IP

    @ApiField("subject")
    String subject; // 商品标题

    @ApiField("body")
    String body; // 商品描述

    @ApiField("notifyUrl")
    String notifyUrl; // 异步通知地址

    @ApiField("returnUrl")
    String returnUrl; // 跳转通知地址

    @ApiField("expiredTime")
    String expiredTime; // 订单失效时间

    @ApiField("channelExtra")
    String channelExtra; // 特定渠道额外支付参数

    @ApiField("extParam")
    String extParam; // 商户扩展参数

    @ApiField("divisionMode")
    private Byte divisionMode; // 分账模式： 0-该笔订单不允许分账[默认], 1-支付成功按配置自动完成分账, 2-商户手动分账(解冻商户金额)

    public PayOrderCreateReqModel() {}

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

    public String getMchOrderNo() {
        return mchOrderNo;
    }

    public void setMchOrderNo(String mchOrderNo) {
        this.mchOrderNo = mchOrderNo;
    }

    public String getWayCode() {
        return wayCode;
    }

    public void setWayCode(String wayCode) {
        this.wayCode = wayCode;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(String expiredTime) {
        this.expiredTime = expiredTime;
    }

    public String getChannelExtra() {
        return channelExtra;
    }

    public void setChannelExtra(String channelExtra) {
        this.channelExtra = channelExtra;
    }

    public String getExtParam() {
        return extParam;
    }

    public void setExtParam(String extParam) {
        this.extParam = extParam;
    }

    public Byte getDivisionMode() {
        return divisionMode;
    }

    public void setDivisionMode(Byte divisionMode) {
        this.divisionMode = divisionMode;
    }
}

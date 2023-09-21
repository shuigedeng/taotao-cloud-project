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

/***
 * 转账查单响应实体类
 *
 * @author terrfly
 * @site https://www.jeepay.vip
 * @since 2021/8/16 16:08
 */
public class TransferOrderQueryResModel extends JeepayObject {

    /** 转账订单号 */
    private String transferId;

    /** 商户号 */
    private String mchNo;

    /** 应用ID */
    private String appId;

    /** 商户订单号 */
    private String mchOrderNo;

    /** 支付接口代码 */
    private String ifCode;

    /** 入账方式： WX_CASH-微信零钱; ALIPAY_CASH-支付宝转账; BANK_CARD-银行卡 */
    private String entryType;

    /** 转账金额,单位分 */
    private Long amount;

    /** 三位货币代码,人民币:cny */
    private String currency;

    /** 收款账号 */
    private String accountNo;

    /** 收款人姓名 */
    private String accountName;

    /** 收款人开户行名称 */
    private String bankName;

    /** 转账备注信息 */
    private String transferDesc;

    /** 支付状态: 0-订单生成, 1-转账中, 2-转账成功, 3-转账失败, 4-订单关闭 */
    private Byte state;

    /** 特定渠道发起额外参数 */
    private String channelExtra;

    /** 渠道订单号 */
    private String channelOrderNo;

    /** 渠道支付错误码 */
    private String errCode;

    /** 渠道支付错误描述 */
    private String errMsg;

    /** 商户扩展参数 */
    private String extParam;

    /** 转账成功时间 */
    private Long successTime;

    /** 创建时间 */
    private Long createdAt;

    public String getTransferId() {
        return transferId;
    }

    public void setTransferId(String transferId) {
        this.transferId = transferId;
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

    public String getMchOrderNo() {
        return mchOrderNo;
    }

    public void setMchOrderNo(String mchOrderNo) {
        this.mchOrderNo = mchOrderNo;
    }

    public String getIfCode() {
        return ifCode;
    }

    public void setIfCode(String ifCode) {
        this.ifCode = ifCode;
    }

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
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

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getTransferDesc() {
        return transferDesc;
    }

    public void setTransferDesc(String transferDesc) {
        this.transferDesc = transferDesc;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public String getChannelExtra() {
        return channelExtra;
    }

    public void setChannelExtra(String channelExtra) {
        this.channelExtra = channelExtra;
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

    public Long getSuccessTime() {
        return successTime;
    }

    public void setSuccessTime(Long successTime) {
        this.successTime = successTime;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }
}

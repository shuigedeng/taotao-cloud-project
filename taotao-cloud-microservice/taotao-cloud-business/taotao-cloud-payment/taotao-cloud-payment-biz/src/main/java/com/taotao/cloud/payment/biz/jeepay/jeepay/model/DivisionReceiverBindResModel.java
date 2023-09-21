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

import java.math.BigDecimal;

/***
 * 分账账号的响应结果
 *
 * @author terrfly
 * @site https://www.jeepay.vip
 * @since 2021/8/25 10:38
 */
public class DivisionReceiverBindResModel extends JeepayObject {

    /** 分账接收者ID */
    private Long receiverId;

    /** 接收者账号别名 */
    private String receiverAlias;

    /** 组ID（便于商户接口使用） */
    private Long receiverGroupId;

    /** 商户号 */
    private String mchNo;

    /** 应用ID */
    private String appId;

    /** 支付接口代码 */
    private String ifCode;

    /** 分账接收账号类型: 0-个人(对私) 1-商户(对公) */
    private Byte accType;

    /** 分账接收账号 */
    private String accNo;

    /** 分账接收账号名称 */
    private String accName;

    /** 分账关系类型（参考微信）， 如： SERVICE_PROVIDER 服务商等 */
    private String relationType;

    /** 当选择自定义时，需要录入该字段。 否则为对应的名称 */
    private String relationTypeName;

    /** 渠道特殊信息 */
    private String channelExtInfo;

    /** 绑定成功时间 */
    private Long bindSuccessTime;

    /** 分账比例 */
    private BigDecimal divisionProfit;

    /** 分账状态 1-绑定成功, 0-绑定异常 */
    private Byte bindState;

    /** 支付渠道错误码 */
    private String errCode;

    /** 支付渠道错误信息 */
    private String errMsg;

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public String getReceiverAlias() {
        return receiverAlias;
    }

    public void setReceiverAlias(String receiverAlias) {
        this.receiverAlias = receiverAlias;
    }

    public Long getReceiverGroupId() {
        return receiverGroupId;
    }

    public void setReceiverGroupId(Long receiverGroupId) {
        this.receiverGroupId = receiverGroupId;
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

    public String getIfCode() {
        return ifCode;
    }

    public void setIfCode(String ifCode) {
        this.ifCode = ifCode;
    }

    public Byte getAccType() {
        return accType;
    }

    public void setAccType(Byte accType) {
        this.accType = accType;
    }

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    public String getAccName() {
        return accName;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    public String getRelationTypeName() {
        return relationTypeName;
    }

    public void setRelationTypeName(String relationTypeName) {
        this.relationTypeName = relationTypeName;
    }

    public String getChannelExtInfo() {
        return channelExtInfo;
    }

    public void setChannelExtInfo(String channelExtInfo) {
        this.channelExtInfo = channelExtInfo;
    }

    public Long getBindSuccessTime() {
        return bindSuccessTime;
    }

    public void setBindSuccessTime(Long bindSuccessTime) {
        this.bindSuccessTime = bindSuccessTime;
    }

    public BigDecimal getDivisionProfit() {
        return divisionProfit;
    }

    public void setDivisionProfit(BigDecimal divisionProfit) {
        this.divisionProfit = divisionProfit;
    }

    public Byte getBindState() {
        return bindState;
    }

    public void setBindState(Byte bindState) {
        this.bindState = bindState;
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

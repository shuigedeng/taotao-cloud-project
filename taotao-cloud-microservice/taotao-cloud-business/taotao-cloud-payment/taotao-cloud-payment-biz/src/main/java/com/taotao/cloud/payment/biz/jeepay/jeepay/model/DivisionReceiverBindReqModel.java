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
 * 分账账号的绑定
 *
 * @author terrfly
 * @site https://www.jeepay.vip
 * @since 2021/8/25 10:36
 */
public class DivisionReceiverBindReqModel extends JeepayObject {

    private static final long serialVersionUID = -3998573128290306948L;

    @ApiField("mchNo")
    private String mchNo; // 商户号

    @ApiField("appId")
    private String appId; // 应用ID

    /** 支付接口代码 * */
    @ApiField("ifCode")
    private String ifCode;

    /** 接收者账号别名 * */
    @ApiField("receiverAlias")
    private String receiverAlias;

    /** 组ID * */
    @ApiField("receiverGroupId")
    private Long receiverGroupId;

    /** 分账接收账号类型: 0-个人(对私) 1-商户(对公) * */
    @ApiField("accType")
    private Byte accType;

    /** 分账接收账号 * */
    @ApiField("accNo")
    private String accNo;

    /** 分账接收账号名称 * */
    @ApiField("accName")
    private String accName;

    /** 分账关系类型（参考微信）， 如： SERVICE_PROVIDER 服务商等 * */
    @ApiField("relationType")
    private String relationType;

    /** 当选择自定义时，需要录入该字段。 否则为对应的名称 * */
    @ApiField("relationTypeName")
    private String relationTypeName;

    /** 渠道特殊信息 */
    @ApiField("channelExtInfo")
    private String channelExtInfo;

    /** 分账比例 * */
    @ApiField("divisionProfit")
    private String divisionProfit;

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

    public String getDivisionProfit() {
        return divisionProfit;
    }

    public void setDivisionProfit(String divisionProfit) {
        this.divisionProfit = divisionProfit;
    }
}

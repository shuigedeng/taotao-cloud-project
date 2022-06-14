package com.taotao.cloud.payment.biz.jeepay.jeepay.model;


/***
* 发起分账
*
* @author terrfly
* @site https://www.jeepay.vip
* @date 2021/8/27 10:16
*/
public class PayOrderDivisionExecReqModel extends JeepayObject {

    private static final long serialVersionUID = -3998573128290306948L;

    @ApiField("mchNo")
    private String mchNo;      // 商户号

    @ApiField("appId")
    private String appId;      // 应用ID

    /** 商户订单号 **/
    @ApiField("mchOrderNo")
    private String mchOrderNo;

    /** 支付系统订单号 **/
    @ApiField("payOrderId")
    private String payOrderId;

    /**
     * 是否使用系统配置的自动分账组： 0-否 1-是
     **/
    @ApiField("useSysAutoDivisionReceivers")
    private Byte useSysAutoDivisionReceivers;

    /** 接收者账号列表（JSONArray 转换为字符串类型）
     * 仅当useSysAutoDivisionReceivers=0 时有效。
     *
     * 参考：
     *
     * 方式1： 按账号纬度
     * [{
     *     receiverId: 800001,
     *     divisionProfit: 0.1 (若不填入则使用系统默认配置值)
     * }]
     *
     * 方式2： 按组纬度
     * [{
     *     receiverGroupId: 100001, (该组所有 当前订单的渠道账号并且可用状态的全部参与分账)
     *     divisionProfit: 0.1 (每个账号的分账比例， 若不填入则使用系统默认配置值， 建议不填写)
     * }]
     *
     * **/
    @ApiField("receivers")
    private String receivers;

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

    public String getPayOrderId() {
        return payOrderId;
    }

    public void setPayOrderId(String payOrderId) {
        this.payOrderId = payOrderId;
    }

    public Byte getUseSysAutoDivisionReceivers() {
        return useSysAutoDivisionReceivers;
    }

    public void setUseSysAutoDivisionReceivers(Byte useSysAutoDivisionReceivers) {
        this.useSysAutoDivisionReceivers = useSysAutoDivisionReceivers;
    }

    public String getReceivers() {
        return receivers;
    }

    public void setReceivers(String receivers) {
        this.receivers = receivers;
    }
}

package com.taotao.cloud.payment.biz.jeepay.jeepay.model;

/***
* 转账查单请求实体类
*
* @author terrfly
* @site https://www.jeepay.vip
* @date 2021/8/16 16:08
*/
public class TransferOrderQueryReqModel extends JeepayObject {

    private static final long serialVersionUID = -3998573128290306948L;

    @ApiField("mchNo")
    private String mchNo;      // 商户号
    @ApiField("appId")
    private String appId;      // 应用ID
    @ApiField("mchOrderNo")
    String mchOrderNo;          // 商户订单号
    @ApiField("transferId")
    String transferId;          // 支付平台订单号

    public TransferOrderQueryReqModel() {
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

    public String getTransferId() {
        return transferId;
    }

    public void setTransferId(String transferId) {
        this.transferId = transferId;
    }
}

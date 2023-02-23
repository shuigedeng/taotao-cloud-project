package com.taotao.cloud.payment.biz.jeepay.jeepay.model;


/**
 * 支付查单请求实体类
 * @author jmdhappy
 * @site https://www.jeepay.vip
 * @date 2021-06-08 11:00
 */
public class PayOrderQueryReqModel extends JeepayObject{

    private static final long serialVersionUID = -5184554341263929245L;

    /**
     * 商户号
     */
    @ApiField("mchNo")
    private String mchNo;
    /**
     * 应用ID
     */
    @ApiField("appId")
    private String appId;
    /**
     * 商户订单号
     */
    @ApiField("mchOrderNo")
    String mchOrderNo;
    /**
     * 支付订单号
     */
    @ApiField("payOrderId")
    String payOrderId;

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
}

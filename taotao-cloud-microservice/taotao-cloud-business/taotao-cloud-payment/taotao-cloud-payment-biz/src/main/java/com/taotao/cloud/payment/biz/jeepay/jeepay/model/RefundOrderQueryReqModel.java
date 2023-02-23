package com.taotao.cloud.payment.biz.jeepay.jeepay.model;

/**
 * 退款查单请求实体类
 * @author jmdhappy
 * @site https://www.jeepay.vip
 * @date 2021-06-18 10:00
 */
public class RefundOrderQueryReqModel extends JeepayObject{

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
     * 商户退款单号
     */
    @ApiField("mchRefundNo")
    String mchRefundNo;
    /**
     * 支付系统退款订单号
     */
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

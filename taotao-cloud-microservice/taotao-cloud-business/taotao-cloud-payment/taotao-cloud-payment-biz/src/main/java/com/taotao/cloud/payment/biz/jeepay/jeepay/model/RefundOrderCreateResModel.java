package com.taotao.cloud.payment.biz.jeepay.jeepay.model;

/**
 * 退款下单响应实体类
 * @author jmdhappy
 * @site https://www.jeepay.vip
 * @date 2021-06-18 09:00
 */
public class RefundOrderCreateResModel extends JeepayObject {

    /**
     * 退款单号(网关生成)
     */
    private String refundOrderId;

    /**
     * 商户发起的退款订单号
     */
    private String mchRefundNo;

    /**
     * 订单支付金额
     */
    private Long payAmount;

    /**
     * 申请退款金额
     */
    private Long refundAmount;

    /**
     * 退款状态
     * 0-订单生成
     * 1-退款中
     * 2-退款成功
     * 3-退款失败
     * 4-退款关闭
     */
    private Integer state;

    /**
     * 渠道退款单号
     */
    private String channelOrderNo;

    /**
     * 支付渠道错误码
     */
    private String errCode;

    /**
     * 支付渠道错误信息
     */
    private String errMsg;

    public String getRefundOrderId() {
        return refundOrderId;
    }

    public void setRefundOrderId(String refundOrderId) {
        this.refundOrderId = refundOrderId;
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

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
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
}

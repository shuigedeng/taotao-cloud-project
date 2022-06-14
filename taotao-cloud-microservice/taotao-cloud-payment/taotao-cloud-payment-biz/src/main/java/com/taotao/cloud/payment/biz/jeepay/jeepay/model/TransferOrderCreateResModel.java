package com.taotao.cloud.payment.biz.jeepay.jeepay.model;

/***
 * 转账下单响应实体类
 *
 * @author terrfly
 * @site https://www.jeepay.vip
 * @date 2021/8/13 16:08
 */
public class TransferOrderCreateResModel extends JeepayObject {

    /**
     * 转账单号(网关生成)
     */
    private String transferId;

    /**
     * 商户发起的转账订单号
     */
    private String mchOrderNo;

    /**
     * 订单转账金额
     */
    private Long amount;

    /**
     * 收款账号
     */
    private String accountNo;

    /**
     * 收款人姓名
     */
    private String accountName;

    /**
     * 收款人开户行名称
     */
    private String bankName;


    /**
     * 转账状态
     * 0-订单生成
     * 1-转账中
     * 2-转账成功
     * 3-转账失败
     * 4-转账关闭
     */
    private Integer state;

    /**
     * 渠道转账单号
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


    public String getTransferId() {
        return transferId;
    }

    public void setTransferId(String transferId) {
        this.transferId = transferId;
    }

    public String getMchOrderNo() {
        return mchOrderNo;
    }

    public void setMchOrderNo(String mchOrderNo) {
        this.mchOrderNo = mchOrderNo;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
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

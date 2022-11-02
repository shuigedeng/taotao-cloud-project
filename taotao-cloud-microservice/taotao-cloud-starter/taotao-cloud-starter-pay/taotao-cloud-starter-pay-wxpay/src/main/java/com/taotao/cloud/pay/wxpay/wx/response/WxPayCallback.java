package com.taotao.cloud.pay.wxpay.wx.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.taotao.cloud.common.utils.common.JsonUtils;
import com.taotao.cloud.pay.wxpay.wx.WxPay;
import com.taotao.cloud.pay.wxpay.wx.enums.ResponseCode;
import com.taotao.cloud.pay.wxpay.wx.enums.TradeType;
import java.math.BigInteger;
import java.util.Map;

/**
 * @author lingting 2021/2/25 15:43
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class WxPayCallback {

	private String transactionId;

	private String nonceStr;

	private String bankType;

	private String openid;

	private String sign;

	private String feeType;

	private String mchId;

	private BigInteger cashFee;

	private String outTradeNo;

	private String appid;

	private BigInteger totalFee;

	private TradeType tradeType;

	private ResponseCode resultCode;

	private String timeEnd;

	private String isSubscribe;

	private ResponseCode returnCode;

	public static WxPayCallback of(Map<String, String> res) {
		WxPayCallback wxPayCallback = JsonUtils.toObject(JsonUtils.toJSONString(res),
			WxPayCallback.class);
		wxPayCallback.setRaw(res);
		return wxPayCallback;
	}

	/**
	 * 返回的原始数据
	 */
	private Map<String, String> raw;

	/**
	 * 验签
	 *
	 * @param wxPay 微信支付信息
	 * @return boolean
	 */
	public boolean checkSign(WxPay wxPay) {
		return wxPay.checkSign(this);
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	public String getBankType() {
		return bankType;
	}

	public void setBankType(String bankType) {
		this.bankType = bankType;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getFeeType() {
		return feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}

	public BigInteger getCashFee() {
		return cashFee;
	}

	public void setCashFee(BigInteger cashFee) {
		this.cashFee = cashFee;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public BigInteger getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(BigInteger totalFee) {
		this.totalFee = totalFee;
	}

	public TradeType getTradeType() {
		return tradeType;
	}

	public void setTradeType(TradeType tradeType) {
		this.tradeType = tradeType;
	}

	public ResponseCode getResultCode() {
		return resultCode;
	}

	public void setResultCode(ResponseCode resultCode) {
		this.resultCode = resultCode;
	}

	public String getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(String timeEnd) {
		this.timeEnd = timeEnd;
	}

	public String getIsSubscribe() {
		return isSubscribe;
	}

	public void setIsSubscribe(String isSubscribe) {
		this.isSubscribe = isSubscribe;
	}

	public ResponseCode getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(ResponseCode returnCode) {
		this.returnCode = returnCode;
	}

	public Map<String, String> getRaw() {
		return raw;
	}

	public void setRaw(Map<String, String> raw) {
		this.raw = raw;
	}
}

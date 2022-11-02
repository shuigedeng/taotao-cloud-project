package com.taotao.cloud.pay.wxpay.wx.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.taotao.cloud.common.utils.common.JsonUtils;
import com.taotao.cloud.pay.wxpay.wx.enums.ResponseCode;
import com.taotao.cloud.pay.wxpay.wx.enums.TradeState;
import com.taotao.cloud.pay.wxpay.wx.enums.TradeType;
import java.math.BigInteger;
import java.util.Map;

/**
 * @author lingting 2021/2/25 15:19
 */
public class WxPayOrderQueryResponse {

	@JsonProperty("transaction_id")
	private String transactionId;

	@JsonProperty("nonce_str")
	private String nonceStr;

	@JsonProperty("trade_state")
	private TradeState tradeState;

	@JsonProperty("bank_type")
	private String bankType;

	@JsonProperty("openid")
	private String openid;

	@JsonProperty("sign")
	private String sign;

	@JsonProperty("return_msg")
	private String returnMsg;

	@JsonProperty("fee_type")
	private String feeType;

	@JsonProperty("mch_id")
	private String mchId;

	@JsonProperty("cash_fee")
	private BigInteger cashFee;

	@JsonProperty("out_trade_no")
	private String outTradeNo;

	@JsonProperty("cash_fee_type")
	private String cashFeeType;

	@JsonProperty("appid")
	private String appid;

	@JsonProperty("total_fee")
	private BigInteger totalFee;

	@JsonProperty("trade_state_desc")
	private String tradeStateDesc;

	@JsonProperty("trade_type")
	private TradeType tradeType;

	@JsonProperty("result_code")
	private ResponseCode resultCode;

	@JsonProperty("attach")
	private String attach;

	@JsonProperty("time_end")
	private String timeEnd;

	@JsonProperty("is_subscribe")
	private String isSubscribe;

	@JsonProperty("return_code")
	private ResponseCode returnCode;

	public static WxPayOrderQueryResponse of(Map<String, String> res) {
		WxPayOrderQueryResponse wxPayCallback = JsonUtils.toObject(JsonUtils.toJSONString(res),
			WxPayOrderQueryResponse.class);
		wxPayCallback.setRaw(res);
		return wxPayCallback;

	}

	/**
	 * 返回的原始数据
	 */
	private Map<String, String> raw;

	/**
	 * 交易是否成功 . 返回false 表示交易失败
	 *
	 * @return boolean
	 */
	public boolean isSuccess() {
		// 交易成功
		return returnCode == ResponseCode.SUCCESS && resultCode == ResponseCode.SUCCESS
			&& tradeState == TradeState.SUCCESS;
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

	public TradeState getTradeState() {
		return tradeState;
	}

	public void setTradeState(TradeState tradeState) {
		this.tradeState = tradeState;
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

	public String getReturnMsg() {
		return returnMsg;
	}

	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
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

	public String getCashFeeType() {
		return cashFeeType;
	}

	public void setCashFeeType(String cashFeeType) {
		this.cashFeeType = cashFeeType;
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

	public String getTradeStateDesc() {
		return tradeStateDesc;
	}

	public void setTradeStateDesc(String tradeStateDesc) {
		this.tradeStateDesc = tradeStateDesc;
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

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
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

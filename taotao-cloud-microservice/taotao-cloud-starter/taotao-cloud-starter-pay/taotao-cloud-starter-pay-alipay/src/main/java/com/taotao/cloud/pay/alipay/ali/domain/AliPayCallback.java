package com.taotao.cloud.pay.alipay.ali.domain;

import static com.taotao.cloud.pay.alipay.ali.constants.AliPayConstant.FIELD_FUND_BILL_LIST;

import com.alipay.api.AlipayApiException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.taotao.cloud.common.utils.common.JsonUtils;
import com.taotao.cloud.pay.alipay.ali.AliPay;
import com.taotao.cloud.pay.alipay.ali.enums.TradeStatus;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author lingting 2021/1/26 13:31
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AliPayCallback {

	/**
	 * 解析回调参数
	 *
	 * @param callbackParams 所有回调参数
	 * @return com.hccake.extend.pay.ali.domain.AliPayCallback
	 */
	public static AliPayCallback of(Map<String, String> callbackParams) {
		Map<String, Object> map = new HashMap<>(callbackParams);
		String fundBillListStr = callbackParams.get(FIELD_FUND_BILL_LIST).replace("&quot;", "\"");
		map.put(FIELD_FUND_BILL_LIST, JsonUtils.toObject(fundBillListStr, List.class));
		// 覆盖原值
		callbackParams.put(FIELD_FUND_BILL_LIST, fundBillListStr);
		AliPayCallback aliPayCallback = JsonUtils.toObject(JsonUtils.toJson(map),
			AliPayCallback.class);
		aliPayCallback.setRaw(callbackParams);
		return aliPayCallback;
	}

	public boolean checkSign(AliPay aliPay) throws AlipayApiException {
		return aliPay.checkSignV1(getRaw()) || aliPay.checkSignV2(getRaw());
	}

	@JsonIgnore
	private Map<String, String> raw;

	private String gmtCreate;

	private String charset;

	private String sellerEmail;

	private String subject;

	private String sign;

	private String buyerId;

	private BigDecimal invoiceAmount;

	private String notifyId;

	private List<FundBill> fundBillList;

	private String notifyType;

	private TradeStatus tradeStatus;

	private BigDecimal receiptAmount;

	private String appId;

	private BigDecimal buyerPayAmount;

	private String signType;

	private String sellerId;

	private String gmtPayment;

	private String notifyTime;

	private String version;

	private String outTradeNo;

	private BigDecimal totalAmount;

	private String tradeNo;

	private String authAppId;

	private String buyerLogonId;

	private BigDecimal pointAmount;

	public static class FundBill {

		private BigDecimal amount;

		private String fundChannel;

		public BigDecimal getAmount() {
			return amount;
		}

		public void setAmount(BigDecimal amount) {
			this.amount = amount;
		}

		public String getFundChannel() {
			return fundChannel;
		}

		public void setFundChannel(String fundChannel) {
			this.fundChannel = fundChannel;
		}
	}

	public Map<String, String> getRaw() {
		return raw;
	}

	public void setRaw(Map<String, String> raw) {
		this.raw = raw;
	}

	public String getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(String gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getSellerEmail() {
		return sellerEmail;
	}

	public void setSellerEmail(String sellerEmail) {
		this.sellerEmail = sellerEmail;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}

	public BigDecimal getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(BigDecimal invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public String getNotifyId() {
		return notifyId;
	}

	public void setNotifyId(String notifyId) {
		this.notifyId = notifyId;
	}

	public List<FundBill> getFundBillList() {
		return fundBillList;
	}

	public void setFundBillList(
		List<FundBill> fundBillList) {
		this.fundBillList = fundBillList;
	}

	public String getNotifyType() {
		return notifyType;
	}

	public void setNotifyType(String notifyType) {
		this.notifyType = notifyType;
	}

	public TradeStatus getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(TradeStatus tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	public BigDecimal getReceiptAmount() {
		return receiptAmount;
	}

	public void setReceiptAmount(BigDecimal receiptAmount) {
		this.receiptAmount = receiptAmount;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public BigDecimal getBuyerPayAmount() {
		return buyerPayAmount;
	}

	public void setBuyerPayAmount(BigDecimal buyerPayAmount) {
		this.buyerPayAmount = buyerPayAmount;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	public String getGmtPayment() {
		return gmtPayment;
	}

	public void setGmtPayment(String gmtPayment) {
		this.gmtPayment = gmtPayment;
	}

	public String getNotifyTime() {
		return notifyTime;
	}

	public void setNotifyTime(String notifyTime) {
		this.notifyTime = notifyTime;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public String getAuthAppId() {
		return authAppId;
	}

	public void setAuthAppId(String authAppId) {
		this.authAppId = authAppId;
	}

	public String getBuyerLogonId() {
		return buyerLogonId;
	}

	public void setBuyerLogonId(String buyerLogonId) {
		this.buyerLogonId = buyerLogonId;
	}

	public BigDecimal getPointAmount() {
		return pointAmount;
	}

	public void setPointAmount(BigDecimal pointAmount) {
		this.pointAmount = pointAmount;
	}
}

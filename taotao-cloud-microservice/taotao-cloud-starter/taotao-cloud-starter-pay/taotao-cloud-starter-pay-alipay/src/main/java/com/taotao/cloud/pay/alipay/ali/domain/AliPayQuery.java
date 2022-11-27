package com.taotao.cloud.pay.alipay.ali.domain;


import static com.taotao.cloud.pay.alipay.ali.constants.AliPayConstant.CODE_SUCCESS;

import cn.hutool.core.util.StrUtil;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.taotao.cloud.pay.alipay.ali.enums.TradeStatus;
import java.math.BigDecimal;

/**
 * 简化查询结果
 */
public class AliPayQuery {

	public static AliPayQuery of(AlipayTradeQueryResponse raw) {
		AliPayQuery query = new AliPayQuery();
		if (raw == null) {
			return query;
		}
		// 状态处理
		if (CODE_SUCCESS.equals(raw.getCode())) {
			// 成功
			query.setTradeStatus(TradeStatus.of(raw.getTradeStatus()));
		}
		// 异常
		else {
			query.setTradeStatus(TradeStatus.ERROR);
		}

		// 金额
		if (StrUtil.isBlank(raw.getTotalAmount())) {
			query.setAmount(BigDecimal.ZERO);
		} else {
			query.setAmount(new BigDecimal(raw.getTotalAmount()));
		}

		// 信息
		query.setCode(raw.getCode());
		query.setMsg(raw.getMsg());
		query.setSubCode(raw.getSubCode());
		query.setSubMsg(raw.getSubMsg());

		// 基础数据
		query.setTradeNo(raw.getTradeNo());
		query.setSn(raw.getOutTradeNo());
		query.setId(raw.getBuyerLogonId());
		query.setUserId(raw.getBuyerUserId());
		query.setUserName(raw.getBuyerUserName());
		query.setUserType(raw.getBuyerUserType());
		return query;
	}

	/**
	 * 原始数据
	 */
	private AlipayTradeQueryResponse raw;

	/**
	 * 订单状态
	 */
	private TradeStatus tradeStatus;

	private String code;

	private String msg;

	private String subCode;

	private String subMsg;

	/**
	 * 金额(单位: 元)
	 */
	private BigDecimal amount;

	/**
	 * 平台订单号
	 */
	private String sn;

	/**
	 * 支付宝订单号
	 */
	private String tradeNo;

	/**
	 * 支付用户支付宝账号信息
	 */
	private String id;

	/**
	 * 支付用户id
	 */
	private String userId;

	private String userName;

	private String userType;

	public AlipayTradeQueryResponse getRaw() {
		return raw;
	}

	public void setRaw(AlipayTradeQueryResponse raw) {
		this.raw = raw;
	}

	public TradeStatus getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(TradeStatus tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getSubCode() {
		return subCode;
	}

	public void setSubCode(String subCode) {
		this.subCode = subCode;
	}

	public String getSubMsg() {
		return subMsg;
	}

	public void setSubMsg(String subMsg) {
		this.subMsg = subMsg;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
}

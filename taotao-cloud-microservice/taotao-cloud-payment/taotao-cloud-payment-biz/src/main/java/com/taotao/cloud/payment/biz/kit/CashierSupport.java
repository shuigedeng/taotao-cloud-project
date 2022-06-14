package com.taotao.cloud.payment.biz.kit;

import cn.hutool.json.JSONUtil;
import com.taotao.cloud.common.enums.ClientTypeEnum;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.member.api.feign.IFeignMemberWalletService;
import com.taotao.cloud.payment.api.enums.PaymentClientEnum;
import com.taotao.cloud.payment.api.enums.PaymentMethodEnum;
import com.taotao.cloud.payment.biz.kit.dto.PayParam;
import com.taotao.cloud.payment.biz.kit.params.CashierExecute;
import com.taotao.cloud.payment.biz.kit.params.dto.CashierParam;
import com.taotao.cloud.sys.api.enums.SettingEnum;
import com.taotao.cloud.sys.api.feign.IFeignSettingService;
import com.taotao.cloud.sys.api.vo.setting.OrderSettingVO;
import com.taotao.cloud.sys.api.vo.setting.SettingVO;
import com.taotao.cloud.sys.api.vo.setting.payment.PaymentSupportSetting;
import com.taotao.cloud.sys.api.vo.setting.payment.dto.PaymentSupportItem;
import org.apache.shardingsphere.distsql.parser.autogen.CommonDistSQLStatementParser.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;

/**
 * 收银台工具
 */
@Component
public class CashierSupport {
	/**
	 * 收银台
	 */
	@Autowired
	private List<CashierExecute> cashierExecuteList;
	/**
	 * 预存款
	 */
	@Autowired
	private IFeignMemberWalletService memberWalletService;
	/**
	 * 配置
	 */
	@Autowired
	private IFeignSettingService settingService;

	/**
	 * 支付
	 *
	 * @param paymentMethodEnum 支付渠道枚举
	 * @param paymentClientEnum 支付方式枚举
	 * @return 支付消息
	 */
	public Result<Object> payment(PaymentMethodEnum paymentMethodEnum, PaymentClientEnum paymentClientEnum,
								  HttpServletRequest request, HttpServletResponse response,
								  PayParam payParam) {
		if (paymentClientEnum == null || paymentMethodEnum == null) {
			throw new BusinessException(ResultEnum.PAY_NOT_SUPPORT);
		}

		//获取支付插件
		Payment payment = (Payment) SpringContextUtil.getBean(paymentMethodEnum.getPlugin());
		LogUtil.info("支付请求：客户端：{},支付类型：{},请求：{}", paymentClientEnum.name(), paymentMethodEnum.name(), payParam.toString());

		//支付方式调用
		return switch (paymentClientEnum) {
			case H5 -> payment.h5pay(request, response, payParam);
			case APP -> payment.appPay(request, payParam);
			case JSAPI -> payment.jsApiPay(request, payParam);
			case NATIVE -> payment.nativePay(request, payParam);
			case MP -> payment.mpPay(request, payParam);
			default -> null;
		};
	}

	/**
	 * 支付 支持的支付方式
	 *
	 * @param client 客户端类型
	 * @return 支持的支付方式
	 */
	public List<String> support(String client) {

		ClientTypeEnum clientTypeEnum;
		try {
			clientTypeEnum = ClientTypeEnum.valueOf(client);
		} catch (IllegalArgumentException e) {
			throw new BusinessException(ResultEnum.PAY_CLIENT_TYPE_ERROR);
		}
		//支付方式 循环获取
		SettingVO setting = settingService.get(SettingEnum.PAYMENT_SUPPORT.name()).data();
		PaymentSupportSetting paymentSupportSetting = JSONUtil.toBean(setting.getSettingValue(), PaymentSupportSetting.class);
		for (PaymentSupportItem paymentSupportItem : paymentSupportSetting.getPaymentSupportItems()) {
			if (paymentSupportItem.getClient().equals(clientTypeEnum.name())) {
				return paymentSupportItem.getSupports();
			}
		}
		throw new BusinessException(ResultEnum.PAY_NOT_SUPPORT);
	}

	/**
	 * 支付回调
	 *
	 * @param paymentMethodEnum 支付渠道枚举
	 * @return 回调消息
	 */
	public void callback(PaymentMethodEnum paymentMethodEnum,
						 HttpServletRequest request) {

		LogUtil.info("支付回调：支付类型：{}", paymentMethodEnum.name());

		//获取支付插件
		Payment payment = (Payment) SpringContextUtil.getBean(paymentMethodEnum.getPlugin());
		payment.callBack(request);
	}

	/**
	 * 支付通知
	 *
	 * @param paymentMethodEnum 支付渠道
	 */
	public void notify(PaymentMethodEnum paymentMethodEnum,
					   HttpServletRequest request) {

		LogUtil.info("支付异步通知：支付类型：{}", paymentMethodEnum.name());

		//获取支付插件
		Payment payment = (Payment) SpringContextUtil.getBean(paymentMethodEnum.getPlugin());
		payment.notify(request);
	}

	/**
	 * 获取收银台参数
	 *
	 * @param payParam 支付请求参数
	 * @return 收银台参数
	 */
	public CashierParam cashierParam(PayParam payParam) {
		for (CashierExecute paramInterface : cashierExecuteList) {
			CashierParam cashierParam = paramInterface.getPaymentParams(payParam);
			//如果为空，则表示收银台参数初始化不匹配，继续匹配下一条
			if (cashierParam == null) {
				continue;
			}
			//如果订单不需要付款，则抛出异常，直接返回
			if (cashierParam.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
				throw new BusinessException(ResultEnum.PAY_UN_WANTED);
			}
			cashierParam.setSupport(support(payParam.getClientType()));
			cashierParam.setWalletValue(memberWalletService.getMemberWallet(
				UserContext.getCurrentUser().getId()).getMemberWallet());
			OrderSettingVO orderSetting = JSONUtil.toBean(settingService.get(SettingEnum.ORDER_SETTING.name()).getSettingValue(), OrderSetting.class);
			Integer minute = orderSetting.getAutoCancel();
			cashierParam.setAutoCancel(cashierParam.getCreateTime().getTime() + minute * 1000 * 60);
			return cashierParam;
		}
		LogUtil.error("错误的支付请求:{}", payParam.toString());
		throw new BusinessException(ResultEnum.PAY_CASHIER_ERROR);
	}


	/**
	 * 支付结果
	 */
	public Boolean paymentResult(PayParam payParam) {
		for (CashierExecute cashierExecute : cashierExecuteList) {
			if (cashierExecute.cashierEnum().name().equals(payParam.getOrderType())) {
				return cashierExecute.paymentResult(payParam);
			}
		}
		return false;
	}
}

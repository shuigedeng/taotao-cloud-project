/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Dante Engine 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Dante Engine 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.pay.alipay.alipay.definition;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayRequest;
import com.alipay.api.AlipayResponse;
import com.alipay.api.domain.AlipayCommerceCityfacilitatorStationQueryModel;
import com.alipay.api.domain.AlipayCommerceCityfacilitatorVoucherBatchqueryModel;
import com.alipay.api.domain.AlipayCommerceCityfacilitatorVoucherGenerateModel;
import com.alipay.api.domain.AlipayCommerceCityfacilitatorVoucherRefundModel;
import com.alipay.api.domain.AlipayDataDataserviceBillDownloadurlQueryModel;
import com.alipay.api.domain.AlipayFundAccountQueryModel;
import com.alipay.api.domain.AlipayFundAuthOperationCancelModel;
import com.alipay.api.domain.AlipayFundAuthOperationDetailQueryModel;
import com.alipay.api.domain.AlipayFundAuthOrderFreezeModel;
import com.alipay.api.domain.AlipayFundAuthOrderUnfreezeModel;
import com.alipay.api.domain.AlipayFundAuthOrderVoucherCreateModel;
import com.alipay.api.domain.AlipayFundCouponOperationQueryModel;
import com.alipay.api.domain.AlipayFundCouponOrderAgreementPayModel;
import com.alipay.api.domain.AlipayFundCouponOrderAppPayModel;
import com.alipay.api.domain.AlipayFundCouponOrderDisburseModel;
import com.alipay.api.domain.AlipayFundCouponOrderPagePayModel;
import com.alipay.api.domain.AlipayFundCouponOrderRefundModel;
import com.alipay.api.domain.AlipayFundTransCommonQueryModel;
import com.alipay.api.domain.AlipayFundTransOrderQueryModel;
import com.alipay.api.domain.AlipayFundTransToaccountTransferModel;
import com.alipay.api.domain.AlipayFundTransUniTransferModel;
import com.alipay.api.domain.AlipayOpenAuthTokenAppModel;
import com.alipay.api.domain.AlipayOpenAuthTokenAppQueryModel;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeCancelModel;
import com.alipay.api.domain.AlipayTradeCloseModel;
import com.alipay.api.domain.AlipayTradeCreateModel;
import com.alipay.api.domain.AlipayTradeFastpayRefundQueryModel;
import com.alipay.api.domain.AlipayTradeOrderSettleModel;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.domain.AlipayTradePayModel;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.domain.AlipayTradeRoyaltyRelationBatchqueryModel;
import com.alipay.api.domain.AlipayTradeRoyaltyRelationBindModel;
import com.alipay.api.domain.AlipayTradeRoyaltyRelationUnbindModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.domain.ZolozAuthenticationCustomerFacemanageCreateModel;
import com.alipay.api.domain.ZolozAuthenticationCustomerFacemanageDeleteModel;
import com.alipay.api.domain.ZolozAuthenticationCustomerFtokenQueryModel;
import com.alipay.api.domain.ZolozAuthenticationSmilepayInitializeModel;
import com.alipay.api.domain.ZolozIdentificationUserWebInitializeModel;
import com.alipay.api.domain.ZolozIdentificationUserWebQueryModel;
import com.alipay.api.request.AlipayCommerceAdContractSignRequest;
import com.alipay.api.request.AlipayCommerceCityfacilitatorStationQueryRequest;
import com.alipay.api.request.AlipayCommerceCityfacilitatorVoucherBatchqueryRequest;
import com.alipay.api.request.AlipayCommerceCityfacilitatorVoucherGenerateRequest;
import com.alipay.api.request.AlipayCommerceCityfacilitatorVoucherRefundRequest;
import com.alipay.api.request.AlipayDataDataserviceBillDownloadurlQueryRequest;
import com.alipay.api.request.AlipayEbppBillGetRequest;
import com.alipay.api.request.AlipayFundAccountQueryRequest;
import com.alipay.api.request.AlipayFundAuthOperationCancelRequest;
import com.alipay.api.request.AlipayFundAuthOperationDetailQueryRequest;
import com.alipay.api.request.AlipayFundAuthOrderFreezeRequest;
import com.alipay.api.request.AlipayFundAuthOrderUnfreezeRequest;
import com.alipay.api.request.AlipayFundAuthOrderVoucherCreateRequest;
import com.alipay.api.request.AlipayFundCouponOperationQueryRequest;
import com.alipay.api.request.AlipayFundCouponOrderAgreementPayRequest;
import com.alipay.api.request.AlipayFundCouponOrderAppPayRequest;
import com.alipay.api.request.AlipayFundCouponOrderDisburseRequest;
import com.alipay.api.request.AlipayFundCouponOrderPagePayRequest;
import com.alipay.api.request.AlipayFundCouponOrderRefundRequest;
import com.alipay.api.request.AlipayFundTransCommonQueryRequest;
import com.alipay.api.request.AlipayFundTransOrderQueryRequest;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.request.AlipayFundTransUniTransferRequest;
import com.alipay.api.request.AlipayOpenAuthTokenAppQueryRequest;
import com.alipay.api.request.AlipayOpenAuthTokenAppRequest;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeCancelRequest;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradeCreateRequest;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradeOrderSettleRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeRoyaltyRelationBatchqueryRequest;
import com.alipay.api.request.AlipayTradeRoyaltyRelationBindRequest;
import com.alipay.api.request.AlipayTradeRoyaltyRelationUnbindRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.request.ZolozAuthenticationCustomerFacemanageCreateRequest;
import com.alipay.api.request.ZolozAuthenticationCustomerFacemanageDeleteRequest;
import com.alipay.api.request.ZolozAuthenticationCustomerFtokenQueryRequest;
import com.alipay.api.request.ZolozAuthenticationSmilepayInitializeRequest;
import com.alipay.api.request.ZolozIdentificationUserWebInitializeRequest;
import com.alipay.api.request.ZolozIdentificationUserWebQueryRequest;
import com.alipay.api.response.AlipayCommerceAdContractSignResponse;
import com.alipay.api.response.AlipayCommerceCityfacilitatorStationQueryResponse;
import com.alipay.api.response.AlipayCommerceCityfacilitatorVoucherBatchqueryResponse;
import com.alipay.api.response.AlipayCommerceCityfacilitatorVoucherGenerateResponse;
import com.alipay.api.response.AlipayCommerceCityfacilitatorVoucherRefundResponse;
import com.alipay.api.response.AlipayDataDataserviceBillDownloadurlQueryResponse;
import com.alipay.api.response.AlipayEbppBillGetResponse;
import com.alipay.api.response.AlipayFundAccountQueryResponse;
import com.alipay.api.response.AlipayFundAuthOperationCancelResponse;
import com.alipay.api.response.AlipayFundAuthOperationDetailQueryResponse;
import com.alipay.api.response.AlipayFundAuthOrderFreezeResponse;
import com.alipay.api.response.AlipayFundAuthOrderUnfreezeResponse;
import com.alipay.api.response.AlipayFundAuthOrderVoucherCreateResponse;
import com.alipay.api.response.AlipayFundCouponOperationQueryResponse;
import com.alipay.api.response.AlipayFundCouponOrderAgreementPayResponse;
import com.alipay.api.response.AlipayFundCouponOrderAppPayResponse;
import com.alipay.api.response.AlipayFundCouponOrderDisburseResponse;
import com.alipay.api.response.AlipayFundCouponOrderPagePayResponse;
import com.alipay.api.response.AlipayFundCouponOrderRefundResponse;
import com.alipay.api.response.AlipayFundTransCommonQueryResponse;
import com.alipay.api.response.AlipayFundTransOrderQueryResponse;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.alipay.api.response.AlipayFundTransUniTransferResponse;
import com.alipay.api.response.AlipayOpenAuthTokenAppQueryResponse;
import com.alipay.api.response.AlipayOpenAuthTokenAppResponse;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeCancelResponse;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradeCreateResponse;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradeOrderSettleResponse;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.alipay.api.response.AlipayTradeRoyaltyRelationBatchqueryResponse;
import com.alipay.api.response.AlipayTradeRoyaltyRelationBindResponse;
import com.alipay.api.response.AlipayTradeRoyaltyRelationUnbindResponse;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.alipay.api.response.ZolozAuthenticationCustomerFacemanageCreateResponse;
import com.alipay.api.response.ZolozAuthenticationCustomerFacemanageDeleteResponse;
import com.alipay.api.response.ZolozAuthenticationCustomerFtokenQueryResponse;
import com.alipay.api.response.ZolozAuthenticationSmilepayInitializeResponse;
import com.alipay.api.response.ZolozIdentificationUserWebInitializeResponse;
import com.alipay.api.response.ZolozIdentificationUserWebQueryResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;

/**
 * <p>Description: 支付宝支付执行器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/1/8 9:54
 */
public class AlipayPaymentExecuter {

	private static final Logger log = LoggerFactory.getLogger(AlipayPaymentExecuter.class);

	public static final String APP_AUTH_TOKEN = "app_auth_token";

	private final AlipayClient alipayClient;
	private final Boolean certMode;

	private AlipayPaymentExecuter(AlipayClient alipayClient, Boolean certMode) {
		this.alipayClient = alipayClient;
		this.certMode = certMode;
	}

	public static AlipayPaymentExecuter create(AlipayClient alipayClient, Boolean certMode) {
		return new AlipayPaymentExecuter(alipayClient, certMode);
	}

	public static AlipayPaymentExecuter create(AlipayClient alipayClient) {
		return create(alipayClient, false);
	}

	private AlipayClient getAlipayClient() {
		return alipayClient;
	}

	public Boolean getCertMode() {
		return certMode;
	}

	public <T extends AlipayResponse> T execute(AlipayRequest<T> request, String accessToken,
		String appAuthToken, String targetAppId) {
		try {
			return getAlipayClient().execute(request, accessToken, appAuthToken, targetAppId);
		} catch (AlipayApiException e) {
			log.error("[Herodotus] |- Alipay payment execute runtime error!");
			throw new PaymentRuntimeErrorException("Alipay execute error.");
		}
	}

	public <T extends AlipayResponse> T execute(AlipayRequest<T> request, String accessToken,
		String appAuthToken) {
		try {
			return getAlipayClient().execute(request, accessToken, appAuthToken);
		} catch (AlipayApiException e) {
			log.error("[Herodotus] |- Alipay payment execute runtime error!");
			throw new PaymentRuntimeErrorException("Alipay execute error.");
		}
	}

	public <T extends AlipayResponse> T execute(AlipayRequest<T> request, String authToken) {
		try {
			return getAlipayClient().execute(request, authToken);
		} catch (AlipayApiException e) {
			log.error("[Herodotus] |- Alipay payment execute runtime error!");
			throw new PaymentRuntimeErrorException("Alipay execute error.");
		}
	}

	public <T extends AlipayResponse> T execute(AlipayRequest<T> request) {
		try {
			return getAlipayClient().execute(request);
		} catch (AlipayApiException e) {
			log.error("[Herodotus] |- Alipay payment execute runtime error!");
			throw new PaymentRuntimeErrorException("Alipay execute error.");
		}
	}

	public <T extends AlipayResponse> T certificateExecute(AlipayRequest<T> request,
		String accessToken, String appAuthToken, String targetAppId) {
		try {
			return getAlipayClient().certificateExecute(request, accessToken, appAuthToken,
				targetAppId);
		} catch (AlipayApiException e) {
			log.error("[Herodotus] |- Alipay payment certificate execute runtime error!");
			throw new PaymentRuntimeErrorException("Alipay execute error.");
		}
	}

	public <T extends AlipayResponse> T certificateExecute(AlipayRequest<T> request,
		String accessToken, String appAuthToken) {
		try {
			return getAlipayClient().certificateExecute(request, accessToken, appAuthToken);
		} catch (AlipayApiException e) {
			log.error("[Herodotus] |- Alipay payment certificate execute runtime error!");
			throw new PaymentRuntimeErrorException("Alipay execute error.");
		}
	}

	public <T extends AlipayResponse> T certificateExecute(AlipayRequest<T> request,
		String authToken) {
		try {
			return getAlipayClient().certificateExecute(request, authToken);
		} catch (AlipayApiException e) {
			log.error("[Herodotus] |- Alipay payment certificate execute runtime error!");
			throw new PaymentRuntimeErrorException("Alipay execute error.");
		}
	}

	public <T extends AlipayResponse> T certificateExecute(AlipayRequest<T> request) {
		try {
			return getAlipayClient().certificateExecute(request);
		} catch (AlipayApiException e) {
			log.error("[Herodotus] |- Alipay payment certificate execute runtime error!");
			throw new PaymentRuntimeErrorException("Alipay execute error.");
		}
	}

	public <T extends AlipayResponse> T pageExecute(AlipayRequest<T> request, String method) {
		try {
			return getAlipayClient().pageExecute(request, method);
		} catch (AlipayApiException e) {
			log.error("[Herodotus] |- Alipay payment page execute runtime error!");
			throw new PaymentRuntimeErrorException("Alipay execute error.");
		}
	}

	public <T extends AlipayResponse> T pageExecute(AlipayRequest<T> request) {
		try {
			return getAlipayClient().pageExecute(request);
		} catch (AlipayApiException e) {
			log.error("[Herodotus] |- Alipay payment page execute runtime error!");
			throw new PaymentRuntimeErrorException("Alipay execute error.");
		}
	}

	public <T extends AlipayResponse> T sdkExecute(AlipayRequest<T> request) {
		try {
			return getAlipayClient().sdkExecute(request);
		} catch (AlipayApiException e) {
			log.error("[Herodotus] |- Alipay payment sdk execute runtime error!");
			throw new PaymentRuntimeErrorException("Alipay execute error.");
		}
	}

	public <T extends AlipayResponse> T doExecute(AlipayRequest<T> request, String accessToken,
		String appAuthToken, String targetAppId) {
		if (getCertMode()) {
			return certificateExecute(request, accessToken, appAuthToken, targetAppId);
		} else {
			return execute(request, accessToken, appAuthToken, targetAppId);
		}
	}

	public <T extends AlipayResponse> T doExecute(AlipayRequest<T> request, String accessToken,
		String appAuthToken) {
		if (getCertMode()) {
			return certificateExecute(request, accessToken, appAuthToken);
		} else {
			return execute(request, accessToken, appAuthToken);
		}
	}

	public <T extends AlipayResponse> T doExecute(AlipayRequest<T> request, String authToken) {
		if (getCertMode()) {
			return certificateExecute(request, authToken);
		} else {
			return execute(request, authToken);
		}
	}

	public <T extends AlipayResponse> T doExecute(AlipayRequest<T> request) {
		if (getCertMode()) {
			return certificateExecute(request);
		} else {
			return execute(request);
		}
	}

	public <T extends AlipayResponse> T doExecute(AlipayRequest<T> request, HttpMethod method) {
		if (ObjectUtils.isNotEmpty(method)) {
			return pageExecute(request, method.name());
		} else {
			return pageExecute(request);
		}
	}

	/**
	 * APP支付
	 *
	 * @param model        {@link AlipayTradeAppPayModel}
	 * @param notifyUrl    异步通知 URL
	 * @param appAuthToken 应用授权token
	 * @return {@link AlipayTradeAppPayResponse}
	 */
	public AlipayTradeAppPayResponse appPay(AlipayTradeAppPayModel model, String notifyUrl,
		String appAuthToken) {
		AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);

		if (StringUtils.isNotBlank(appAuthToken)) {
			request.putOtherTextParam(APP_AUTH_TOKEN, appAuthToken);
		}
		return sdkExecute(request);
	}

	/**
	 * APP支付
	 *
	 * @param model     {@link AlipayTradeAppPayModel}
	 * @param notifyUrl 异步通知 URL
	 * @return {@link AlipayTradeAppPayResponse}
	 */
	public AlipayTradeAppPayResponse appPay(AlipayTradeAppPayModel model, String notifyUrl) {
		return appPay(model, notifyUrl, null);
	}

	/**
	 * WAP支付
	 *
	 * @param model        {@link AlipayTradeWapPayModel}
	 * @param returnUrl    异步通知URL
	 * @param notifyUrl    同步通知URL
	 * @param appAuthToken 应用授权token
	 * @param method       {@link HttpMethod}
	 * @return {@link AlipayTradeWapPayResponse}
	 */
	public AlipayTradeWapPayResponse wapPay(AlipayTradeWapPayModel model, String returnUrl,
		String notifyUrl, String appAuthToken, HttpMethod method) {
		AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
		request.setBizModel(model);
		request.setReturnUrl(returnUrl);
		request.setNotifyUrl(notifyUrl);

		if (StringUtils.isNotBlank(appAuthToken)) {
			request.putOtherTextParam(APP_AUTH_TOKEN, appAuthToken);
		}

		return doExecute(request, method);
	}

	/**
	 * WAP支付
	 *
	 * @param model        {@link AlipayTradeWapPayModel}
	 * @param returnUrl    异步通知URL
	 * @param notifyUrl    同步通知URL
	 * @param appAuthToken 应用授权token
	 * @return {@link AlipayTradeWapPayResponse}
	 */
	public AlipayTradeWapPayResponse wapPay(AlipayTradeWapPayModel model, String returnUrl,
		String notifyUrl, String appAuthToken) {
		return wapPay(model, returnUrl, notifyUrl, appAuthToken, null);
	}

	/**
	 * WAP支付
	 *
	 * @param model     {@link AlipayTradeWapPayModel}
	 * @param returnUrl 异步通知URL
	 * @param notifyUrl 同步通知URL
	 * @param method    {@link HttpMethod}
	 * @return {@link AlipayTradeWapPayResponse}
	 */
	public AlipayTradeWapPayResponse wapPay(AlipayTradeWapPayModel model, String returnUrl,
		String notifyUrl, HttpMethod method) {
		return wapPay(model, returnUrl, notifyUrl, null, method);
	}

	/**
	 * WAP支付
	 *
	 * @param model     {@link AlipayTradeWapPayModel}
	 * @param returnUrl 异步通知URL
	 * @param notifyUrl 同步通知URL
	 * @return {@link AlipayTradeWapPayResponse}
	 */
	public AlipayTradeWapPayResponse wapPay(AlipayTradeWapPayModel model, String returnUrl,
		String notifyUrl) {
		return wapPay(model, returnUrl, notifyUrl, null, null);
	}


	/**
	 * 统一收单交易支付接口接口 <br> 适用于:条形码支付、声波支付等 <br>
	 *
	 * @param model        {AlipayTradePayModel}
	 * @param notifyUrl    异步通知URL
	 * @param appAuthToken 应用授权token
	 * @return {AlipayTradePayResponse}
	 */
	public AlipayTradePayResponse paymentCodePay(AlipayTradePayModel model, String notifyUrl,
		String appAuthToken) {
		AlipayTradePayRequest request = new AlipayTradePayRequest();
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);

		if (StringUtils.isNotBlank(appAuthToken)) {
			request.putOtherTextParam(APP_AUTH_TOKEN, appAuthToken);
		}
		return doExecute(request);
	}

	/**
	 * 统一收单交易支付接口接口 <br> 适用于:条形码支付、声波支付等 <br>
	 *
	 * @param model     {@link AlipayTradePayModel}
	 * @param notifyUrl 异步通知URL
	 * @return {@link AlipayTradePayResponse}
	 */
	public AlipayTradePayResponse paymentCodePay(AlipayTradePayModel model, String notifyUrl) {
		return paymentCodePay(model, notifyUrl, null);
	}

	/**
	 * 统一收单线下交易预创建 <br> 适用于：扫码支付等 <br>
	 *
	 * @param model     {@link AlipayTradePrecreateModel}
	 * @param notifyUrl 异步通知URL
	 * @return {@link AlipayTradePrecreateResponse}
	 */
	public AlipayTradePrecreateResponse scanCodePay(AlipayTradePrecreateModel model,
		String notifyUrl, String appAuthToken) {
		AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);

		if (StringUtils.isNotBlank(appAuthToken)) {
			request.putOtherTextParam(APP_AUTH_TOKEN, appAuthToken);
		}
		return doExecute(request);
	}

	/**
	 * 统一收单线下交易预创建 <br> 适用于：扫码支付等 <br>
	 *
	 * @param model     {@link AlipayTradePrecreateModel}
	 * @param notifyUrl 异步通知URL
	 * @return {@link AlipayTradePrecreateResponse}
	 */
	public AlipayTradePrecreateResponse scanCodePay(AlipayTradePrecreateModel model,
		String notifyUrl) {
		return scanCodePay(model, notifyUrl, null);
	}

	/**
	 * 电脑网站支付(PC支付)
	 *
	 * @param model        {@link AlipayTradePagePayModel}
	 * @param notifyUrl    异步通知URL
	 * @param returnUrl    同步通知URL
	 * @param appAuthToken 应用授权token
	 * @param method       请求方式 {@link HttpMethod}
	 * @return {@link AlipayTradePagePayResponse}
	 */
	public AlipayTradePagePayResponse pagePay(AlipayTradePagePayModel model, String notifyUrl,
		String returnUrl, String appAuthToken, HttpMethod method) {
		AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		request.setReturnUrl(returnUrl);

		if (StringUtils.isNotBlank(appAuthToken)) {
			request.putOtherTextParam(APP_AUTH_TOKEN, appAuthToken);
		}

		return doExecute(request, method);
	}

	/**
	 * 电脑网站支付(PC支付)
	 *
	 * @param model     {@link AlipayTradePagePayModel}
	 * @param notifyUrl 异步通知URL
	 * @param returnUrl 同步通知URL
	 * @param method    请求方式 {@link HttpMethod}
	 * @return {@link AlipayTradePagePayResponse}
	 */
	public AlipayTradePagePayResponse pagePay(AlipayTradePagePayModel model, String notifyUrl,
		String returnUrl, HttpMethod method) {
		return pagePay(model, notifyUrl, returnUrl, null, method);
	}

	/**
	 * 电脑网站支付(PC支付)
	 *
	 * @param model        {@link AlipayTradePagePayModel}
	 * @param notifyUrl    异步通知URL
	 * @param returnUrl    同步通知URL
	 * @param appAuthToken 应用授权token
	 * @return {@link AlipayTradePagePayResponse}
	 */
	public AlipayTradePagePayResponse pagePay(AlipayTradePagePayModel model, String notifyUrl,
		String returnUrl, String appAuthToken) {
		return pagePay(model, notifyUrl, returnUrl, appAuthToken, null);
	}

	/**
	 * 电脑网站支付(PC支付)
	 *
	 * @param model     {@link AlipayTradePagePayModel}
	 * @param notifyUrl 异步通知URL
	 * @param returnUrl 同步通知URL
	 * @return {@link AlipayTradePagePayResponse}
	 */
	public AlipayTradePagePayResponse pagePay(AlipayTradePagePayModel model, String notifyUrl,
		String returnUrl) {
		return pagePay(model, notifyUrl, returnUrl, null, null);
	}

	/**
	 * 统一收单交易创建接口
	 *
	 * @param model        {@link AlipayTradeCreateModel}
	 * @param notifyUrl    异步通知URL
	 * @param appAuthToken 应用授权token
	 * @return {@link AlipayTradeCreateResponse}
	 */
	public AlipayTradeCreateResponse jsapiPay(AlipayTradeCreateModel model, String notifyUrl,
		String appAuthToken) {
		AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		return execute(request, null, appAuthToken);
	}

	/**
	 * 统一收单交易创建接口
	 *
	 * @param model     {@link AlipayTradeCreateModel}
	 * @param notifyUrl 异步通知URL
	 * @return {@link AlipayTradeCreateResponse}
	 */
	public AlipayTradeCreateResponse jsapiPay(AlipayTradeCreateModel model, String notifyUrl) {
		AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		return doExecute(request);
	}

	/**
	 * 单笔转账到支付宝账户
	 *
	 * @param model {@link AlipayFundTransToaccountTransferModel}
	 * @return {@link AlipayFundTransToaccountTransferResponse}
	 */
	public AlipayFundTransToaccountTransferResponse transfer(
		AlipayFundTransToaccountTransferModel model) {
		AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 转账查询接口
	 *
	 * @param model {@link AlipayFundTransOrderQueryModel}
	 * @return {@link AlipayFundTransOrderQueryResponse}
	 */
	public AlipayFundTransOrderQueryResponse transferQuery(AlipayFundTransOrderQueryModel model) {
		AlipayFundTransOrderQueryRequest request = new AlipayFundTransOrderQueryRequest();
		request.setBizModel(model);
		return doExecute(request);
	}


	/**
	 * 统一转账接口
	 *
	 * @param model        model {@link AlipayFundTransUniTransferModel}
	 * @param appAuthToken 应用授权token
	 * @return {@link AlipayFundTransUniTransferResponse}
	 */
	public AlipayFundTransUniTransferResponse unifiedTransfer(AlipayFundTransUniTransferModel model,
		String appAuthToken) {
		AlipayFundTransUniTransferRequest request = new AlipayFundTransUniTransferRequest();
		request.setBizModel(model);
		if (StringUtils.isNotBlank(appAuthToken)) {
			request.putOtherTextParam(APP_AUTH_TOKEN, appAuthToken);
		}
		return doExecute(request);
	}

	/**
	 * 转账业务单据查询接口
	 *
	 * @param model        model {@link AlipayFundTransCommonQueryModel}
	 * @param appAuthToken 应用授权token
	 * @return {@link AlipayFundTransCommonQueryResponse}
	 */
	public AlipayFundTransCommonQueryResponse transferCommonQuery(
		AlipayFundTransCommonQueryModel model, String appAuthToken) {
		AlipayFundTransCommonQueryRequest request = new AlipayFundTransCommonQueryRequest();
		request.setBizModel(model);

		if (StringUtils.isNotBlank(appAuthToken)) {
			request.putOtherTextParam(APP_AUTH_TOKEN, appAuthToken);
		}
		return doExecute(request);
	}

	/**
	 * 支付宝资金账户资产查询接口
	 *
	 * @param model        model {@link AlipayFundAccountQueryModel}
	 * @param appAuthToken 应用授权token
	 * @return {@link AlipayFundAccountQueryResponse}
	 */
	public AlipayFundAccountQueryResponse accountQuery(AlipayFundAccountQueryModel model,
		String appAuthToken) {
		AlipayFundAccountQueryRequest request = new AlipayFundAccountQueryRequest();
		request.setBizModel(model);

		if (StringUtils.isNotBlank(appAuthToken)) {
			request.putOtherTextParam(APP_AUTH_TOKEN, appAuthToken);
		}
		return doExecute(request);
	}

	/**
	 * 统一收单线下交易查询接口
	 *
	 * @param model        {@link AlipayTradeQueryModel}
	 * @param appAuthToken 应用授权token
	 * @return {@link AlipayTradeQueryResponse}
	 */
	public AlipayTradeQueryResponse query(AlipayTradeQueryModel model, String appAuthToken) {
		AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
		request.setBizModel(model);
		return execute(request, null, appAuthToken);
	}

	/**
	 * 统一收单线下交易查询接口
	 *
	 * @param model {@link AlipayTradeQueryModel}
	 * @return {@link AlipayTradeQueryResponse}
	 */
	public AlipayTradeQueryResponse query(AlipayTradeQueryModel model) {
		AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 统一收单交易撤销接口
	 *
	 * @param model        {@link AlipayTradeCancelModel}
	 * @param appAuthToken 应用授权token
	 * @return {@link AlipayTradeCancelResponse}
	 */
	public AlipayTradeCancelResponse cancel(AlipayTradeCancelModel model, String appAuthToken) {
		AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();
		request.setBizModel(model);
		return execute(request, null, appAuthToken);
	}

	/**
	 * 统一收单交易撤销接口
	 *
	 * @param model {@link AlipayTradeCancelModel}
	 * @return {@link AlipayTradeCancelResponse}
	 */
	public AlipayTradeCancelResponse cancel(AlipayTradeCancelModel model) {
		AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 统一收单交易关闭接口
	 *
	 * @param model        {@link AlipayTradeCloseModel}
	 * @param appAuthToken 应用授权token
	 * @return {@link AlipayTradeCloseResponse}
	 */
	public AlipayTradeCloseResponse close(AlipayTradeCloseModel model, String appAuthToken) {
		AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
		request.setBizModel(model);
		return execute(request, null, appAuthToken);
	}

	/**
	 * 统一收单交易关闭接口
	 *
	 * @param model {@link AlipayTradeCloseModel}
	 * @return {@link AlipayTradeCloseResponse}
	 */
	public AlipayTradeCloseResponse close(AlipayTradeCloseModel model) {
		AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 统一收单交易退款接口
	 *
	 * @param model        {@link AlipayTradeRefundModel}
	 * @param appAuthToken 应用授权token
	 * @return {@link AlipayTradeRefundResponse}
	 */
	public AlipayTradeRefundResponse refund(AlipayTradeRefundModel model, String appAuthToken) {
		AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
		request.setBizModel(model);
		return execute(request, null, appAuthToken);
	}

	/**
	 * 统一收单交易退款接口
	 *
	 * @param model {@link AlipayTradeRefundModel}
	 * @return {@link AlipayTradeRefundResponse}
	 */
	public AlipayTradeRefundResponse refund(AlipayTradeRefundModel model) {
		AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 统一收单交易退款查询
	 *
	 * @param model        {@link AlipayTradeFastpayRefundQueryModel}
	 * @param appAuthToken 应用授权token
	 * @return {@link AlipayTradeFastpayRefundQueryResponse}
	 */
	public AlipayTradeFastpayRefundQueryResponse refundQuery(
		AlipayTradeFastpayRefundQueryModel model, String appAuthToken) {
		AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
		request.setBizModel(model);
		return execute(request, null, appAuthToken);
	}

	/**
	 * 统一收单交易退款查询
	 *
	 * @param model {@link AlipayTradeFastpayRefundQueryModel}
	 * @return {@link AlipayTradeFastpayRefundQueryResponse}
	 */
	public AlipayTradeFastpayRefundQueryResponse refundQuery(
		AlipayTradeFastpayRefundQueryModel model) {
		AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 查询对账单下载地址
	 *
	 * @param model        {@link AlipayDataDataserviceBillDownloadurlQueryModel}
	 * @param appAuthToken 应用授权token
	 * @return {@link AlipayDataDataserviceBillDownloadurlQueryResponse}
	 */
	public AlipayDataDataserviceBillDownloadurlQueryResponse billDownloadUrlQuery(
		AlipayDataDataserviceBillDownloadurlQueryModel model, String appAuthToken) {
		AlipayDataDataserviceBillDownloadurlQueryRequest request = new AlipayDataDataserviceBillDownloadurlQueryRequest();
		request.setBizModel(model);

		if (StringUtils.isNotBlank(appAuthToken)) {
			request.putOtherTextParam(APP_AUTH_TOKEN, appAuthToken);
		}
		return doExecute(request);
	}

	/**
	 * 查询对账单下载地址
	 *
	 * @param model {@link AlipayDataDataserviceBillDownloadurlQueryModel}
	 * @return {@link AlipayDataDataserviceBillDownloadurlQueryResponse}
	 */
	public AlipayDataDataserviceBillDownloadurlQueryResponse billDownloadUrlQuery(
		AlipayDataDataserviceBillDownloadurlQueryModel model) {
		return billDownloadUrlQuery(model, null);
	}

	/**
	 * 统一收单交易结算接口
	 *
	 * @param model        {@link AlipayTradeOrderSettleModel}
	 * @param appAuthToken 应用授权token
	 * @return {@link AlipayTradeOrderSettleResponse}
	 */
	public AlipayTradeOrderSettleResponse orderSettle(AlipayTradeOrderSettleModel model,
		String appAuthToken) {
		AlipayTradeOrderSettleRequest request = new AlipayTradeOrderSettleRequest();
		request.setBizModel(model);
		return execute(request, null, appAuthToken);
	}

	/**
	 * 统一收单交易结算接口
	 *
	 * @param model {@link AlipayTradeOrderSettleModel}
	 * @return {@link AlipayTradeOrderSettleResponse}
	 */
	public AlipayTradeOrderSettleResponse orderSettle(AlipayTradeOrderSettleModel model) {
		AlipayTradeOrderSettleRequest request = new AlipayTradeOrderSettleRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 资金预授权冻结接口
	 *
	 * @param model {@link AlipayFundAuthOrderFreezeModel}
	 * @return {@link AlipayFundAuthOrderFreezeResponse}
	 */
	public AlipayFundAuthOrderFreezeResponse authOrderFreeze(AlipayFundAuthOrderFreezeModel model) {
		AlipayFundAuthOrderFreezeRequest request = new AlipayFundAuthOrderFreezeRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 资金授权解冻接口
	 *
	 * @param model {@link AlipayFundAuthOrderUnfreezeModel}
	 * @return {@link AlipayFundAuthOrderUnfreezeResponse}
	 */
	public AlipayFundAuthOrderUnfreezeResponse authOrderUnfreeze(
		AlipayFundAuthOrderUnfreezeModel model) {
		AlipayFundAuthOrderUnfreezeRequest request = new AlipayFundAuthOrderUnfreezeRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 资金预授权冻结接口
	 *
	 * @param model {@link AlipayFundAuthOrderVoucherCreateModel}
	 * @return {@link AlipayFundAuthOrderVoucherCreateResponse}
	 */
	public AlipayFundAuthOrderVoucherCreateResponse authOrderVoucherCreate(
		AlipayFundAuthOrderVoucherCreateModel model) {
		AlipayFundAuthOrderVoucherCreateRequest request = new AlipayFundAuthOrderVoucherCreateRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 资金授权撤销接口
	 *
	 * @param model {@link AlipayFundAuthOperationCancelModel}
	 * @return {@link AlipayFundAuthOperationCancelResponse}
	 */
	public AlipayFundAuthOperationCancelResponse authOperationCancel(
		AlipayFundAuthOperationCancelModel model) {
		AlipayFundAuthOperationCancelRequest request = new AlipayFundAuthOperationCancelRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 资金授权操作查询接口
	 *
	 * @param model {@link AlipayFundAuthOperationDetailQueryModel}
	 * @return {@link AlipayFundAuthOperationDetailQueryResponse}
	 */
	public AlipayFundAuthOperationDetailQueryResponse authOperationDetailQuery(
		AlipayFundAuthOperationDetailQueryModel model) {
		AlipayFundAuthOperationDetailQueryRequest request = new AlipayFundAuthOperationDetailQueryRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 红包无线支付接口
	 *
	 * @param model {@link AlipayFundCouponOrderAppPayModel}
	 * @return {@link AlipayFundCouponOrderAppPayResponse}
	 */
	public AlipayFundCouponOrderAppPayResponse couponOrderAppPay(
		AlipayFundCouponOrderAppPayModel model) {
		AlipayFundCouponOrderAppPayRequest request = new AlipayFundCouponOrderAppPayRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 红包页面支付接口
	 *
	 * @param model {@link AlipayFundCouponOrderPagePayModel}
	 * @return {@link AlipayFundCouponOrderPagePayResponse}
	 */
	public AlipayFundCouponOrderPagePayResponse couponOrderPagePay(
		AlipayFundCouponOrderPagePayModel model) {
		AlipayFundCouponOrderPagePayRequest request = new AlipayFundCouponOrderPagePayRequest();
		request.setBizModel(model);
		return doExecute(request);
	}


	/**
	 * 红包协议支付接口
	 *
	 * @param model {@link AlipayFundCouponOrderAgreementPayModel}
	 * @return {@link AlipayFundCouponOrderAgreementPayResponse}
	 */
	public AlipayFundCouponOrderAgreementPayResponse couponOrderAgreementPay(
		AlipayFundCouponOrderAgreementPayModel model) {
		AlipayFundCouponOrderAgreementPayRequest request = new AlipayFundCouponOrderAgreementPayRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 红包打款接口
	 *
	 * @param model {@link AlipayFundCouponOrderDisburseModel}
	 * @return {@link AlipayFundCouponOrderDisburseResponse}
	 */
	public AlipayFundCouponOrderDisburseResponse couponOrderDisburse(
		AlipayFundCouponOrderDisburseModel model) {
		AlipayFundCouponOrderDisburseRequest request = new AlipayFundCouponOrderDisburseRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 红包退回接口
	 *
	 * @param model {@link AlipayFundCouponOrderRefundModel}
	 * @return {@link AlipayFundCouponOrderRefundResponse}
	 */
	public AlipayFundCouponOrderRefundResponse couponOrderRefund(
		AlipayFundCouponOrderRefundModel model) {
		AlipayFundCouponOrderRefundRequest request = new AlipayFundCouponOrderRefundRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 红包退回接口
	 *
	 * @param model {@link AlipayFundCouponOperationQueryModel}
	 * @return {@link AlipayFundCouponOperationQueryResponse}
	 */
	public AlipayFundCouponOperationQueryResponse couponOperationQuery(
		AlipayFundCouponOperationQueryModel model) {
		AlipayFundCouponOperationQueryRequest request = new AlipayFundCouponOperationQueryRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 应用授权 URL 拼装
	 *
	 * @param appId       应用编号
	 * @param redirectUri 回调 URI
	 * @return 应用授权 URL
	 * @throws UnsupportedEncodingException 编码异常
	 */
	public String getOauth2Url(String appId, String redirectUri)
		throws UnsupportedEncodingException {
		return "https://openauth.alipay.com/oauth2/appToAppAuth.htm?app_id=" + appId
			+ "&redirect_uri=" + URLEncoder.encode(redirectUri, "UTF-8");
	}

	/**
	 * 使用 app_auth_code 换取 app_auth_token
	 *
	 * @param model {@link AlipayOpenAuthTokenAppModel}
	 * @return {@link AlipayOpenAuthTokenAppResponse}
	 */
	public AlipayOpenAuthTokenAppResponse authTokenApp(AlipayOpenAuthTokenAppModel model) {
		AlipayOpenAuthTokenAppRequest request = new AlipayOpenAuthTokenAppRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 查询授权信息
	 *
	 * @param model {@link AlipayOpenAuthTokenAppQueryModel}
	 * @return {@link AlipayOpenAuthTokenAppQueryResponse}
	 */
	public AlipayOpenAuthTokenAppQueryResponse authTokenAppQuery(
		AlipayOpenAuthTokenAppQueryModel model) {
		AlipayOpenAuthTokenAppQueryRequest request = new AlipayOpenAuthTokenAppQueryRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 地铁购票发码
	 *
	 * @param model {@link AlipayCommerceCityfacilitatorVoucherGenerateModel}
	 * @return {@link AlipayCommerceCityfacilitatorVoucherGenerateResponse}
	 */
	public AlipayCommerceCityfacilitatorVoucherGenerateResponse voucherGenerate(
		AlipayCommerceCityfacilitatorVoucherGenerateModel model) {
		AlipayCommerceCityfacilitatorVoucherGenerateRequest request = new AlipayCommerceCityfacilitatorVoucherGenerateRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 地铁购票发码退款
	 *
	 * @param model {@link AlipayCommerceCityfacilitatorVoucherRefundModel}
	 * @return {@link AlipayCommerceCityfacilitatorVoucherRefundResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public AlipayCommerceCityfacilitatorVoucherRefundResponse metroRefund(
		AlipayCommerceCityfacilitatorVoucherRefundModel model) {
		AlipayCommerceCityfacilitatorVoucherRefundRequest request = new AlipayCommerceCityfacilitatorVoucherRefundRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 地铁车站数据查询
	 *
	 * @param model {@link AlipayCommerceCityfacilitatorStationQueryModel}
	 * @return {@link AlipayCommerceCityfacilitatorStationQueryResponse}
	 */
	public AlipayCommerceCityfacilitatorStationQueryResponse stationQuery(
		AlipayCommerceCityfacilitatorStationQueryModel model) {
		AlipayCommerceCityfacilitatorStationQueryRequest request = new AlipayCommerceCityfacilitatorStationQueryRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 核销码批量查询
	 *
	 * @param model {@link AlipayCommerceCityfacilitatorVoucherBatchqueryModel}
	 * @return {@link AlipayCommerceCityfacilitatorVoucherBatchqueryResponse}
	 */
	public AlipayCommerceCityfacilitatorVoucherBatchqueryResponse voucherBatchquery(
		AlipayCommerceCityfacilitatorVoucherBatchqueryModel model) {
		AlipayCommerceCityfacilitatorVoucherBatchqueryRequest request = new AlipayCommerceCityfacilitatorVoucherBatchqueryRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 生活缴费查询账单
	 *
	 * @param orderType       支付宝订单类型
	 * @param merchantOrderNo 业务流水号
	 * @return {@link AlipayEbppBillGetResponse}
	 */
	public AlipayEbppBillGetResponse ebppBillGet(String orderType, String merchantOrderNo) {
		AlipayEbppBillGetRequest request = new AlipayEbppBillGetRequest();
		request.setOrderType(orderType);
		request.setMerchantOrderNo(merchantOrderNo);
		return doExecute(request);
	}

	/**
	 * H5刷脸认证初始化
	 *
	 * @param model {@link ZolozIdentificationUserWebInitializeModel}
	 * @return {@link ZolozIdentificationUserWebInitializeResponse}
	 */
	public ZolozIdentificationUserWebInitializeResponse identificationUserWebInitialize(
		ZolozIdentificationUserWebInitializeModel model) {
		ZolozIdentificationUserWebInitializeRequest request = new ZolozIdentificationUserWebInitializeRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * H5刷脸认证查询
	 *
	 * @param model {@link ZolozIdentificationUserWebQueryModel}
	 * @return {@link ZolozIdentificationUserWebQueryResponse}
	 */
	public ZolozIdentificationUserWebQueryResponse identificationUserWebQuery(
		ZolozIdentificationUserWebQueryModel model) {
		ZolozIdentificationUserWebQueryRequest request = new ZolozIdentificationUserWebQueryRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 热脸入库
	 *
	 * @param model {@link ZolozAuthenticationCustomerFacemanageCreateModel}
	 * @return {@link ZolozAuthenticationCustomerFacemanageCreateResponse}
	 */
	public ZolozAuthenticationCustomerFacemanageCreateResponse authenticationCustomerFaceManageCreate(
		ZolozAuthenticationCustomerFacemanageCreateModel model) {
		ZolozAuthenticationCustomerFacemanageCreateRequest request = new ZolozAuthenticationCustomerFacemanageCreateRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 热脸出库
	 *
	 * @param model {@link ZolozAuthenticationCustomerFacemanageDeleteModel}
	 * @return {@link ZolozAuthenticationCustomerFacemanageDeleteResponse}
	 */
	public ZolozAuthenticationCustomerFacemanageDeleteResponse authenticationCustomerFaceManageDelete(
		ZolozAuthenticationCustomerFacemanageDeleteModel model) {
		ZolozAuthenticationCustomerFacemanageDeleteRequest request = new ZolozAuthenticationCustomerFacemanageDeleteRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 人脸 ftoken 查询消费接口
	 *
	 * @param model {@link ZolozAuthenticationCustomerFtokenQueryModel}
	 * @return {@link ZolozAuthenticationCustomerFtokenQueryResponse}
	 */
	public ZolozAuthenticationCustomerFtokenQueryResponse authenticationCustomerFTokenQuery(
		ZolozAuthenticationCustomerFtokenQueryModel model) {
		ZolozAuthenticationCustomerFtokenQueryRequest request = new ZolozAuthenticationCustomerFtokenQueryRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 人脸初始化刷脸付
	 *
	 * @param model {@link ZolozAuthenticationSmilepayInitializeModel}
	 * @return {@link ZolozAuthenticationSmilepayInitializeResponse}
	 */
	public ZolozAuthenticationSmilepayInitializeResponse authenticationSmilePayInitialize(
		ZolozAuthenticationSmilepayInitializeModel model) {
		ZolozAuthenticationSmilepayInitializeRequest request = new ZolozAuthenticationSmilepayInitializeRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 生态激励项目ISV代签约接口
	 *
	 * @return {@link AlipayCommerceAdContractSignResponse}
	 */
	public AlipayCommerceAdContractSignResponse commerceAdContractSign() {
		AlipayCommerceAdContractSignRequest request = new AlipayCommerceAdContractSignRequest();
		return doExecute(request);
	}

	/**
	 * 分账关系绑定
	 *
	 * @param model {@link AlipayTradeRoyaltyRelationBindModel}
	 * @return {@link AlipayTradeRoyaltyRelationBindResponse}
	 */
	public AlipayTradeRoyaltyRelationBindResponse royaltyRelationBind(
		AlipayTradeRoyaltyRelationBindModel model) {
		AlipayTradeRoyaltyRelationBindRequest request = new AlipayTradeRoyaltyRelationBindRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 分账关系解绑
	 *
	 * @param model {@link AlipayTradeRoyaltyRelationUnbindModel}
	 * @return {@link AlipayTradeRoyaltyRelationUnbindResponse}
	 */
	public AlipayTradeRoyaltyRelationUnbindResponse royaltyRelationUnBind(
		AlipayTradeRoyaltyRelationUnbindModel model) {
		AlipayTradeRoyaltyRelationUnbindRequest request = new AlipayTradeRoyaltyRelationUnbindRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 分账关系查询
	 *
	 * @param model {@link AlipayTradeRoyaltyRelationBatchqueryModel}
	 * @return {@link AlipayTradeRoyaltyRelationBatchqueryResponse}
	 */
	public AlipayTradeRoyaltyRelationBatchqueryResponse royaltyRelationBatchQuery(
		AlipayTradeRoyaltyRelationBatchqueryModel model) {
		AlipayTradeRoyaltyRelationBatchqueryRequest request = new AlipayTradeRoyaltyRelationBatchqueryRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

}

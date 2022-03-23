package com.taotao.cloud.payment.biz.kit;


import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.payment.api.enums.PaymentMethodEnum;
import com.taotao.cloud.payment.biz.entity.RefundLog;
import com.taotao.cloud.payment.biz.kit.dto.PayParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 支付接口
 */
public interface Payment {
    /**
     * 普通移动网页调用支付app
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @param payParam api参数
     * @return 移动支付所需参数
     */
    default Result<Object> h5pay(HttpServletRequest request, HttpServletResponse response, PayParam payParam) {
        throw new BusinessException(ResultEnum.PAY_ERROR);
    }

    /**
     * 公众号内部调用支付
     *
     * @param request  HttpServletRequest
     * @param payParam api参数
     * @return 公众号内部支付参数
     */
    default Result<Object> jsApiPay(HttpServletRequest request, PayParam payParam) {
        throw new BusinessException(ResultEnum.PAY_ERROR);
    }

    /**
     * app支付
     *
     * @param request  HttpServletRequest
     * @param payParam 支付参数
     * @return app支付所需参数
     */
    default Result<Object> appPay(HttpServletRequest request, PayParam payParam) {
        throw new BusinessException(ResultEnum.PAY_ERROR);
    }

    /**
     * 展示二维码扫描支付
     *
     * @param request  HttpServletRequest
     * @param payParam 支付参数
     * @return 二维码内容
     */
    default Result<Object> nativePay(HttpServletRequest request, PayParam payParam) {
        throw new BusinessException(ResultEnum.PAY_ERROR);
    }

    /**
     * 小程序支付
     *
     * @param request  HttpServletRequest
     * @param payParam 支付参数
     * @return 二维码内容
     */
    default Result<Object> mpPay(HttpServletRequest request, PayParam payParam) {
        throw new BusinessException(ResultEnum.PAY_ERROR);
    }


    /**
     * 退款
     *
     * @param refundLog 退款请求参数
     */
    default void refund(RefundLog refundLog) {
        throw new BusinessException(ResultEnum.PAY_ERROR);
    }


    /**
     * 取消支付订单
     *
     * @param refundLog 支付参数
     */
    default void cancel(RefundLog refundLog) {
        throw new BusinessException(ResultEnum.PAY_ERROR);
    }


    /**
     * 回调
     *
     * @param request HttpServletRequest
     */
    default void callBack(HttpServletRequest request) {
        throw new BusinessException(ResultEnum.PAY_ERROR);
    }

    /**
     * 异步通知
     *
     * @param request HttpServletRequest
     */
    default void notify(HttpServletRequest request) {
        throw new BusinessException(ResultEnum.PAY_ERROR);
    }

    /**
     * 退款异步通知
     *
     * @param request HttpServletRequest
     */
    default void refundNotify(HttpServletRequest request) {
        throw new BusinessException(ResultEnum.PAY_ERROR);
    }

    /**
     * 支付回调地址
     *
     * @param api               api地址
     * @param paymentMethodEnum 支付类型
     * @return 回调地址
     */
    default String callbackUrl(String api, PaymentMethodEnum paymentMethodEnum) {
        return api + "/buyer/payment/cashier/callback/" + paymentMethodEnum.name();
    }
    /**
     * 支付异步通知地址
     *
     * @param api               api地址
     * @param paymentMethodEnum 支付类型
     * @return 异步通知地址
     */
    default String notifyUrl(String api, PaymentMethodEnum paymentMethodEnum) {
        return api + "/buyer/payment/cashier/notify/" + paymentMethodEnum.name();
    }

    /**
     * 退款支付异步通知地址
     *
     * @param api               api地址
     * @param paymentMethodEnum 支付类型
     * @return 异步通知地址
     */
    default String refundNotifyUrl(String api, PaymentMethodEnum paymentMethodEnum) {
        return api + "/buyer/payment/cashierRefund/notify/" + paymentMethodEnum.name();
    }

}

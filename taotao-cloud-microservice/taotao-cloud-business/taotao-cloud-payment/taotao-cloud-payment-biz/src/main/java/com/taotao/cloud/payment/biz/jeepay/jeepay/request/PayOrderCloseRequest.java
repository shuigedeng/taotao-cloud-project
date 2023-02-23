package com.taotao.cloud.payment.biz.jeepay.jeepay.request;


import com.taotao.cloud.payment.biz.jeepay.jeepay.Jeepay;
import com.taotao.cloud.payment.biz.jeepay.jeepay.model.JeepayObject;
import com.taotao.cloud.payment.biz.jeepay.jeepay.response.PayOrderCloseResponse;

/**
 * Jeepay支付 订单关闭请求实现
 *
 * @author xiaoyu
 * @site https://www.jeequan.com
 * @date 2022/1/25 9:56
 */
public class PayOrderCloseRequest implements JeepayRequest<PayOrderCloseResponse> {

    private String apiVersion = Jeepay.VERSION;
    private String apiUri = "api/pay/close";
    private RequestOptions options;
    private JeepayObject bizModel = null;

    @Override
    public String getApiUri() {
        return this.apiUri;
    }

    @Override
    public String getApiVersion() {
        return this.apiVersion;
    }

    @Override
    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    @Override
    public RequestOptions getRequestOptions() {
        return this.options;
    }

    @Override
    public void setRequestOptions(RequestOptions options) {
        this.options = options;
    }

    @Override
    public JeepayObject getBizModel() {
        return this.bizModel;
    }

    @Override
    public void setBizModel(JeepayObject bizModel) {
        this.bizModel = bizModel;
    }

    @Override
    public Class<PayOrderCloseResponse> getResponseClass() {
        return PayOrderCloseResponse.class;
    }

}

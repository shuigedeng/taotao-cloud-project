package com.taotao.cloud.payment.biz.jeepay.jeepay.request;


import com.taotao.cloud.payment.biz.jeepay.jeepay.Jeepay;
import com.taotao.cloud.payment.biz.jeepay.jeepay.model.JeepayObject;
import com.taotao.cloud.payment.biz.jeepay.jeepay.response.RefundOrderQueryResponse;

/**
 * Jeepay退款查单请求实现
 * @author jmdhappy
 * @site https://www.jeepay.vip
 * @date 2021-06-18 12:00
 */
public class RefundOrderQueryRequest implements JeepayRequest<RefundOrderQueryResponse> {

    private String apiVersion = Jeepay.VERSION;
    private String apiUri = "api/refund/query";
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
    public Class<RefundOrderQueryResponse> getResponseClass() {
        return RefundOrderQueryResponse.class;
    }

}

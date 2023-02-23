package com.taotao.cloud.payment.biz.jeepay.jeepay.request;


import com.taotao.cloud.payment.biz.jeepay.jeepay.model.JeepayObject;
import com.taotao.cloud.payment.biz.jeepay.jeepay.net.RequestOptions;
import com.taotao.cloud.payment.biz.jeepay.jeepay.response.JeepayResponse;

/**
 * Jeepay请求接口
 * @author jmdhappy
 * @site https://www.jeepay.vip
 * @date 2021-06-08 11:00
 */
public interface JeepayRequest<T extends JeepayResponse> {

    /**
     * 获取当前接口的路径
     * @return
     */
    String getApiUri();

    /**
     * 获取当前接口的版本
     * @return
     */
    String getApiVersion();

    /**
     * 设置当前接口的版本
     * @return
     */
    void setApiVersion(String apiVersion);

    RequestOptions getRequestOptions();

    void setRequestOptions(RequestOptions options);

    JeepayObject getBizModel();

    void setBizModel(JeepayObject bizModel);

    Class<T> getResponseClass();

}

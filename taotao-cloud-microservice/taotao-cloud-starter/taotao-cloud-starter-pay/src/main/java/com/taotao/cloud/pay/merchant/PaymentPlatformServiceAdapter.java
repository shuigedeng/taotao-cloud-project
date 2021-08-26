package com.taotao.cloud.pay.merchant;

import com.egzosn.pay.common.api.PayService;
import com.egzosn.pay.common.http.HttpConfigStorage;

/**
 * 支付商户服务适配器
 * @author egan
 *         <pre>
 *                 email egzosn@gmail.com
 *
 *                 date 2019/5/9 19:28.
 *                 </pre>
 */
public interface PaymentPlatformServiceAdapter<S extends PayService> {

    /**
     * 初始化服务
     * @return 支付商户服务适配器
     */
    PaymentPlatformServiceAdapter initService();

    /**
     * 获取支付平台对应的支付服务
     * @return 支付服务
     */
    S getPayService();



    /**
     * 获取HTTP请求配置
     * @return HTTP请求配置
     */
    HttpConfigStorage getHttpConfigStorage();
}

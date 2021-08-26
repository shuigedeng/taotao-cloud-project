package com.taotao.cloud.pay;


import com.taotao.cloud.pay.configurers.MerchantDetailsServiceConfigurer;
import com.taotao.cloud.pay.configurers.PayMessageConfigurer;

/**
 * 支付服务配置，用于支付服务相关的配置，暂时主要为商户相关的配置，后期在进行添加别的配置
 * @author egan
 *         <pre>
 *              email egzosn@gmail.com
 *
 *              date 2019/5/8 19:06.
 *         </pre>
 */
public interface PayServiceConfigurer {

    /**
     * 商户配置
     * @param configurer 商户配置
     */
    void configure(MerchantDetailsServiceConfigurer configurer);
    /**
     * 商户配置
     * @param configurer 支付消息配置
     */
    void configure(PayMessageConfigurer configurer) ;




}

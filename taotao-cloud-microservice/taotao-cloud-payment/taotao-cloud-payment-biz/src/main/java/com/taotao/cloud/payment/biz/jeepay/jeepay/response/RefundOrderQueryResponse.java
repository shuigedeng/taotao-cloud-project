package com.taotao.cloud.payment.biz.jeepay.jeepay.response;

import com.jeequan.jeepay.model.RefundOrderQueryResModel;

/**
 * Jeepay退款查单响应实现
 * @author jmdhappy
 * @site https://www.jeepay.vip
 * @date 2021-06-18 12:00
 */
public class RefundOrderQueryResponse extends JeepayResponse {

    private static final long serialVersionUID = 7654172640802954221L;

    public RefundOrderQueryResModel get() {
        if(getData() == null) return new RefundOrderQueryResModel();
        return getData().toJavaObject(RefundOrderQueryResModel.class);
    }

}

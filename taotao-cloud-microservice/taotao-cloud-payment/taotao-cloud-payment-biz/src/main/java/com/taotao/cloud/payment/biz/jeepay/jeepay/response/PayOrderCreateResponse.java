package com.taotao.cloud.payment.biz.jeepay.jeepay.response;


import com.taotao.cloud.payment.biz.jeepay.jeepay.model.PayOrderCreateResModel;

/**
 * Jeepay支付下单响应实现
 * @author jmdhappy
 * @site https://www.jeepay.vip
 * @date 2021-06-08 11:00
 */
public class PayOrderCreateResponse extends JeepayResponse {

    private static final long serialVersionUID = 7419683269497002904L;

    public PayOrderCreateResModel get() {
        if(getData() == null) return new PayOrderCreateResModel();
        return getData().toJavaObject(PayOrderCreateResModel.class);
    }

    @Override
    public boolean isSuccess(String apiKey) {
        if(super.isSuccess(apiKey)) {
            int orderState = get().getOrderState();
            return orderState == 0 || orderState == 1 || orderState == 2;
        }
        return false;
    }

}

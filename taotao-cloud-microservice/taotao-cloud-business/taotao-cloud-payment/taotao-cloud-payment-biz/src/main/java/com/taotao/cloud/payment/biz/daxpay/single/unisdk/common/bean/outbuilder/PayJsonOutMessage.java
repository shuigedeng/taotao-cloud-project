package com.taotao.cloud.payment.biz.daxpay.single.unisdk.common.bean.outbuilder;

import com.taotao.cloud.payment.biz.daxpay.unisdk.common.bean.PayOutMessage;

/**
 * @author egan
 * <pre>
 *      email egzosn@gmail.com
 *      date 2016-6-1 11:40:30
 *   </pre>
 */
public class PayJsonOutMessage extends PayOutMessage {

    public PayJsonOutMessage() {

    }

    @Override
    public String toMessage() {
        return getContent();
    }


}

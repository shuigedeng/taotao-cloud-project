package com.taotao.cloud.payment.biz.infrastructure.daxpay.single.unisdk.common.bean.outbuilder;

import com.taotao.cloud.payment.biz.daxpay.unisdk.common.bean.PayOutMessage;
import lombok.Getter;
import lombok.Setter;

/**
 * @author egan
 * <pre>
 *     email egzosn@gmail.com
 *     date 2016-6-1 13:53:3
 *  </pre>
 */
@Setter
@Getter
public class PayXmlOutMessage extends PayOutMessage{

    private String code;

    public PayXmlOutMessage() {
    }

    @Override
    public String toMessage() {
       return "<xml><return_code><![CDATA[" + code + "]]></return_code><return_msg><![CDATA[" + content
                + "]]></return_msg></xml>";
    }
}

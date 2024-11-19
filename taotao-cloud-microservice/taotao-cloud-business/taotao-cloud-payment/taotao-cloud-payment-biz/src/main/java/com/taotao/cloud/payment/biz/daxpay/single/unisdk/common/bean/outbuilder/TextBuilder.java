package com.taotao.cloud.payment.biz.daxpay.single.unisdk.common.bean.outbuilder;

import com.taotao.cloud.payment.biz.daxpay.unisdk.common.bean.PayOutMessage;

/**
 * <p> source chanjarster/weixin-java-tools</p>
 * @author egan
 * <pre>
 *     email egzosn@gmail.com
 *     date 2016-6-1 11:40:30
 *  </pre>
 */
public class TextBuilder extends BaseBuilder<TextBuilder, PayOutMessage> {
    private String content;

    public TextBuilder content(String content) {
        this.content = content;
        return this;
    }

    @Override
    public PayOutMessage build() {
        PayTextOutMessage message = new PayTextOutMessage();
        setCommon(message);
        message.setContent(content);
        return message;
    }
}

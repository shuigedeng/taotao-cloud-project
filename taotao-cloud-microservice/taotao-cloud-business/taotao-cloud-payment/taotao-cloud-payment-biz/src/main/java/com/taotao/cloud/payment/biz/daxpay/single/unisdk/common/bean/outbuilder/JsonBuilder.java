
package com.taotao.cloud.payment.biz.daxpay.single.unisdk.common.bean.outbuilder;

import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;
import com.taotao.cloud.payment.biz.daxpay.unisdk.common.bean.PayOutMessage;

/**
 * @author egan
 *  <pre>
 *      email egzosn@gmail.com
 *      date 2017/1/13 14:30
 *   </pre>
 */
@Getter
public class JsonBuilder  extends BaseBuilder<JsonBuilder, PayOutMessage>{
    JSONObject json;

    public JsonBuilder(JSONObject json) {
        this.json = json;
    }

    public JsonBuilder content(String key, Object content) {
        this.json.put(key, content);
        return this;
    }

    @Override
    public PayOutMessage build() {
        PayJsonOutMessage message = new PayJsonOutMessage();
        setCommon(message);
        message.setContent(json.toJSONString());
        return message;
    }
}

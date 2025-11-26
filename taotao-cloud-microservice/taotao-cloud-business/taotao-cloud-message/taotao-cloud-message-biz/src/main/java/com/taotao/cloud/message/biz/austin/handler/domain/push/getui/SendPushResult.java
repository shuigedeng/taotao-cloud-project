package com.taotao.cloud.message.biz.austin.handler.domain.push.getui;


import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.annotation.JSONField;
import lombok.*;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.*;
import lombok.NoArgsConstructor;


/**
 * 发送消息后的返回值
 *
 * @author shuigedeng
 * https://docs.getui.com/getui/server/rest_v2/common_args/?id=doc-title-1
 */
@Accessors(chain=true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendPushResult {
    /**
     * msg
     */
    @JSONField(name = "msg")
    private String msg;
    /**
     * code
     */
    @JSONField(name = "code")
    private Integer code;
    /**
     * data
     */
    @JSONField(name = "data")
    private JSONObject data;

}

package com.taotao.cloud.message.biz.infrastructure.austin.support.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 请求token时的参数
 *
 * @author shuigedeng
 * https://docs.getui.com/getui/server/rest_v2/token/
 */
@NoArgsConstructor
@Data

@AllArgsConstructor
public class QueryTokenParamDTO {
    /**
     * sign
     */
    @JSONField(name = "sign")
    private String sign;
    /**
     * timestamp
     */
    @JSONField(name = "timestamp")
    private String timestamp;
    /**
     * appkey
     */
    @JSONField(name = "appkey")
    private String appKey;
}

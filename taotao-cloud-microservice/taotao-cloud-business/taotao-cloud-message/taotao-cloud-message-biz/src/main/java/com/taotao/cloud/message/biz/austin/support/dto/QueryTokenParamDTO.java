package com.taotao.cloud.message.biz.austin.support.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.*;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.*;
import lombok.NoArgsConstructor;


/**
 * 请求token时的参数
 *
 * @author shuigedeng
 * https://docs.getui.com/getui/server/rest_v2/token/
 */
@NoArgsConstructor
@Data
@Accessors(chain=true)
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

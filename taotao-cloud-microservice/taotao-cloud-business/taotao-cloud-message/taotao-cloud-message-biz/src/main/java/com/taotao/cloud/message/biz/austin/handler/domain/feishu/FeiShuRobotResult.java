package com.taotao.cloud.message.biz.austin.handler.domain.feishu;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.*;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.*;
import lombok.NoArgsConstructor;


/**
 * @author shuigedeng
 * 飞书机器人 返回值
 */
@NoArgsConstructor
@Data
@AllArgsConstructor
@Accessors(chain=true)
public class FeiShuRobotResult {

    /**
     * extra
     */
    @JSONField(name = "Extra")
    private Object extra;
    /**
     * statusCode
     */
    @JSONField(name = "StatusCode")
    private Integer statusCode;
    /**
     * statusMessage
     */
    @JSONField(name = "StatusMessage")
    private String statusMessage;
    /**
     * code
     */
    @JSONField(name = "code")
    private Integer code;
    /**
     * msg
     */
    @JSONField(name = "msg")
    private String msg;
    /**
     * data
     */
    @JSONField(name = "data")
    private DataDTO data;

    /**
     * DataDTO
     */
    @NoArgsConstructor
    @Data
    public static class DataDTO {
    }
}

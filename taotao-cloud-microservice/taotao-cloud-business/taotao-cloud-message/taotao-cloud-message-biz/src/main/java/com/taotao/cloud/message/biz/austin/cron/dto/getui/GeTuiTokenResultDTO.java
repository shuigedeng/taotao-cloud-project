package com.taotao.cloud.message.biz.austin.cron.dto.getui;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.*;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.*;
import lombok.NoArgsConstructor;

/**
 * @author shuigedeng
 * @date 2022/5/8
 * <p>
 * https://docs.getui.com/getui/server/rest_v2/token/
 */
@NoArgsConstructor
@Data
@AllArgsConstructor
@Accessors(chain = true)
public class GeTuiTokenResultDTO {


    @JSONField(name = "msg")
    private String msg;
    @JSONField(name = "code")
    private Integer code;
    @JSONField(name = "data")
    private DataDTO data;

    /**
     * DataDTO
     *
     * @author shuigedeng
     * @version 2026.01
     * @since 2025-12-19 09:30:45
     */
    @NoArgsConstructor
    @Data
    public static class DataDTO {

        @JSONField(name = "expire_time")
        private String expireTime;
        @JSONField(name = "token")
        private String token;
    }
}

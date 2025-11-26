package com.taotao.cloud.message.biz.austin.handler.domain.push.getui;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.*;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.*;
import lombok.NoArgsConstructor;

import java.util.Set;


/**
 * 批量推送消息的param
 *
 * @author shuigedeng
 * https://docs.getui.com/getui/server/rest_v2/push/
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain=true)
public class BatchSendPushParam {

    /**
     * audience
     */
    @JSONField(name = "audience")
    private AudienceVO audience;
    /**
     * taskid
     */
    @JSONField(name = "taskid")
    private String taskId;
    /**
     * isAsync
     */
    @JSONField(name = "is_async")
    private Boolean isAsync;

    /**
     * AudienceVO
     */
    @NoArgsConstructor
    @Data
    @Accessors(chain=true)
    @AllArgsConstructor
    public static class AudienceVO {
        /**
         * cid
         */
        @JSONField(name = "cid")
        private Set<String> cid;
    }
}

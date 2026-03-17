package com.taotao.cloud.message.biz.infrastructure.austin.handler.domain.push.getui;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
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
    
    @AllArgsConstructor
    public static class AudienceVO {
        /**
         * cid
         */
        @JSONField(name = "cid")
        private Set<String> cid;
    }
}

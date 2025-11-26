package com.taotao.cloud.message.biz.austin.handler.domain.push.getui;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.*;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.*;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * 推送消息的param
 *
 * @author shuigedeng
 * https://docs.getui.com/getui/server/rest_v2/push/
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain=true)
public class SendPushParam {

    /**
     * requestId
     */
    @JSONField(name = "request_id")
    private String requestId;
    /**
     * settings
     */
    @JSONField(name = "settings")
    private SettingsVO settings;
    /**
     * audience
     */
    @JSONField(name = "audience")
    private AudienceVO audience;
    /**
     * pushMessage
     */
    @JSONField(name = "push_message")
    private PushMessageVO pushMessage;

    /**
     * SettingsVO
     */
    @NoArgsConstructor
    @Data
    public static class SettingsVO {
        /**
         * ttl
         */
        @JSONField(name = "ttl")
        private Integer ttl;
    }

    /**
     * AudienceVO
     */
    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    @Accessors(chain=true)
    public static class AudienceVO {
        /**
         * cid
         */
        @JSONField(name = "cid")
        private Set<String> cid;
    }

    /**
     * PushMessageVO
     */
    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    @Accessors(chain=true)
    public static class PushMessageVO {
        /**
         * notification
         */
        @JSONField(name = "notification")
        private NotificationVO notification;

        /**
         * NotificationVO
         */
        @NoArgsConstructor
        @Data
        @AllArgsConstructor
        @Accessors(chain=true)
        public static class NotificationVO {
            /**
             * title
             */
            @JSONField(name = "title")
            private String title;
            /**
             * body
             */
            @JSONField(name = "body")
            private String body;
            /**
             * clickType
             */
            @JSONField(name = "click_type")
            private String clickType;
            /**
             * url
             */
            @JSONField(name = "url")
            private String url;
        }
    }
}

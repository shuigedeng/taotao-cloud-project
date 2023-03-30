/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.message.biz.austin.handler.domain.push.getui;

import com.alibaba.fastjson.annotation.JSONField;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 推送消息的param
 *
 * @author 3y https://docs.getui.com/getui/server/rest_v2/push/
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SendPushParam {

    /** requestId */
    @JSONField(name = "request_id")
    private String requestId;
    /** settings */
    @JSONField(name = "settings")
    private SettingsVO settings;
    /** audience */
    @JSONField(name = "audience")
    private AudienceVO audience;
    /** pushMessage */
    @JSONField(name = "push_message")
    private PushMessageVO pushMessage;

    /** SettingsVO */
    @NoArgsConstructor
    @Data
    public static class SettingsVO {

        /** ttl */
        @JSONField(name = "ttl")
        private Integer ttl;
    }

    /** AudienceVO */
    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    @Builder
    public static class AudienceVO {

        /** cid */
        @JSONField(name = "cid")
        private Set<String> cid;
    }

    /** PushMessageVO */
    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    @Builder
    public static class PushMessageVO {

        /** notification */
        @JSONField(name = "notification")
        private NotificationVO notification;

        /** NotificationVO */
        @NoArgsConstructor
        @Data
        @AllArgsConstructor
        @Builder
        public static class NotificationVO {

            /** title */
            @JSONField(name = "title")
            private String title;
            /** body */
            @JSONField(name = "body")
            private String body;
            /** clickType */
            @JSONField(name = "click_type")
            private String clickType;
            /** url */
            @JSONField(name = "url")
            private String url;
        }
    }
}

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

package com.taotao.cloud.message.biz.austin.handler.domain.feishu;

import com.alibaba.fastjson.annotation.JSONField;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 3y 飞书机器人 请求参数
 */
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class FeiShuRobotParam {

    /** msgType */
    @JSONField(name = "msg_type")
    private String msgType;
    /** content */
    @JSONField(name = "content")
    private ContentDTO content;
    /** card */
    @JSONField(name = "card")
    private CardDTO card;

    /** ContentDTO */
    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    @Builder
    public static class ContentDTO {

        /** text */
        @JSONField(name = "text")
        private String text;
        /** post */
        @JSONField(name = "post")
        private PostDTO post;
        /** shareChatId */
        @JSONField(name = "share_chat_id")
        private String shareChatId;
        /** imageKey */
        @JSONField(name = "image_key")
        private String imageKey;

        /** PostDTO */
        @NoArgsConstructor
        @Data
        @AllArgsConstructor
        @Builder
        public static class PostDTO {

            /** zhCn */
            @JSONField(name = "zh_cn")
            private ZhCnDTO zhCn;

            /** ZhCnDTO */
            @NoArgsConstructor
            @Data
            @AllArgsConstructor
            @Builder
            public static class ZhCnDTO {

                /** title */
                @JSONField(name = "title")
                private String title;
                /** content */
                @JSONField(name = "content")
                private List<List<PostContentDTO>> content;

                /** ContentDTO */
                @NoArgsConstructor
                @Data
                @AllArgsConstructor
                @Builder
                public static class PostContentDTO {

                    /** tag */
                    @JSONField(name = "tag")
                    private String tag;
                    /** text */
                    @JSONField(name = "text")
                    private String text;
                    /** href */
                    @JSONField(name = "href")
                    private String href;
                    /** userId */
                    @JSONField(name = "user_id")
                    private String userId;
                }
            }
        }
    }

    /** CardDTO */
    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    @Builder
    public static class CardDTO {

        /** config */
        @JSONField(name = "config")
        private ConfigDTO config;
        /** elements */
        @JSONField(name = "elements")
        private List<ElementsDTO> elements;
        /** header */
        @JSONField(name = "header")
        private HeaderDTO header;

        /** ConfigDTO */
        @NoArgsConstructor
        @Data
        @AllArgsConstructor
        @Builder
        public static class ConfigDTO {

            /** wideScreenMode */
            @JSONField(name = "wide_screen_mode")
            private Boolean wideScreenMode;
            /** enableForward */
            @JSONField(name = "enable_forward")
            private Boolean enableForward;
        }

        /** HeaderDTO */
        @NoArgsConstructor
        @Data
        @AllArgsConstructor
        @Builder
        public static class HeaderDTO {

            /** title */
            @JSONField(name = "title")
            private TitleDTO title;

            /** TitleDTO */
            @NoArgsConstructor
            @Data
            @AllArgsConstructor
            @Builder
            public static class TitleDTO {

                /** content */
                @JSONField(name = "content")
                private String content;
                /** tag */
                @JSONField(name = "tag")
                private String tag;
            }
        }

        /** ElementsDTO */
        @NoArgsConstructor
        @Data
        @AllArgsConstructor
        @Builder
        public static class ElementsDTO {

            /** tag */
            @JSONField(name = "tag")
            private String tag;
            /** text */
            @JSONField(name = "text")
            private TextDTO text;
            /** actions */
            @JSONField(name = "actions")
            private List<ActionsDTO> actions;

            /** TextDTO */
            @NoArgsConstructor
            @Data
            @AllArgsConstructor
            @Builder
            public static class TextDTO {

                /** content */
                @JSONField(name = "content")
                private String content;
                /** tag */
                @JSONField(name = "tag")
                private String tag;
            }

            /** ActionsDTO */
            @NoArgsConstructor
            @Data
            @AllArgsConstructor
            @Builder
            public static class ActionsDTO {

                /** tag */
                @JSONField(name = "tag")
                private String tag;
                /** text */
                @JSONField(name = "text")
                private TextDTO text;
                /** url */
                @JSONField(name = "url")
                private String url;
                /** type */
                @JSONField(name = "type")
                private String type;

                /** TextDTO */
                @NoArgsConstructor
                @Data
                public static class TextDTO {}

                /** ValueDTO */
                @Data
                @NoArgsConstructor
                public static class ValueDTO {}
            }
        }
    }
}

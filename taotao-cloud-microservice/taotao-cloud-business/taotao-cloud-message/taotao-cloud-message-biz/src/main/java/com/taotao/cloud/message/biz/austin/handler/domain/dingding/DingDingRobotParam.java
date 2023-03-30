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

package com.taotao.cloud.message.biz.austin.handler.domain.dingding;

import com.alibaba.fastjson.annotation.JSONField;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 钉钉自定义机器人 入参
 *
 * <p>https://open.dingtalk.com/document/group/custom-robot-access
 *
 * @author 3y
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DingDingRobotParam {

    /** at */
    private AtVO at;
    /** text */
    private TextVO text;
    /** link */
    private LinkVO link;
    /** markdown */
    private MarkdownVO markdown;
    /** actionCard */
    private ActionCardVO actionCard;
    /** feedCard */
    private FeedCardVO feedCard;
    /** msgtype */
    private String msgtype;

    /** AtVO */
    @NoArgsConstructor
    @Data
    @Builder
    @AllArgsConstructor
    public static class AtVO {

        /** atMobiles */
        private List<String> atMobiles;
        /** atUserIds */
        private List<String> atUserIds;
        /** isAtAll */
        private Boolean isAtAll;
    }

    /** TextVO */
    @NoArgsConstructor
    @Data
    @Builder
    @AllArgsConstructor
    public static class TextVO {

        /** content */
        private String content;
    }

    /** LinkVO */
    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    @Builder
    public static class LinkVO {

        /** text */
        private String text;
        /** title */
        private String title;
        /** picUrl */
        private String picUrl;
        /** messageUrl */
        private String messageUrl;
    }

    /** MarkdownVO */
    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    @Builder
    public static class MarkdownVO {

        /** title */
        private String title;
        /** text */
        private String text;
    }

    /** ActionCardVO */
    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    @Builder
    public static class ActionCardVO {

        /** title */
        private String title;
        /** text */
        private String text;
        /** btnOrientation */
        private String btnOrientation;
        /** btns */
        private List<BtnsVO> btns;

        /** BtnsVO */
        @NoArgsConstructor
        @Data
        @AllArgsConstructor
        @Builder
        public static class BtnsVO {

            /** title */
            private String title;
            /** actionURL */
            @JSONField(name = "actionURL")
            private String actionUrl;
        }
    }

    /** FeedCardVO */
    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    @Builder
    public static class FeedCardVO {

        /** links */
        private List<LinksVO> links;

        /** LinksVO */
        @NoArgsConstructor
        @Data
        @AllArgsConstructor
        @Builder
        public static class LinksVO {

            /** title */
            private String title;
            /** messageURL */
            @JSONField(name = "messageURL")
            private String messageUrl;
            /** picURL */
            @JSONField(name = "picURL")
            private String picUrl;
        }
    }
}

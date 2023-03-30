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

package com.taotao.cloud.message.biz.austin.web.vo.amis;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 3y
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SmsTimeLineVo {

    /** items */
    private List<ItemsVO> items;

    /** ItemsVO */
    @Data
    @Builder
    public static class ItemsVO {

        /** 业务ID */
        private String businessId;
        /** detail 发送内容 */
        private String content;

        /** 发送状态 */
        private String sendType;

        /** 回执状态 */
        private String receiveType;

        /** 回执报告 */
        private String receiveContent;

        /** 发送时间 */
        private String sendTime;

        /** 回执时间 */
        private String receiveTime;
    }
}

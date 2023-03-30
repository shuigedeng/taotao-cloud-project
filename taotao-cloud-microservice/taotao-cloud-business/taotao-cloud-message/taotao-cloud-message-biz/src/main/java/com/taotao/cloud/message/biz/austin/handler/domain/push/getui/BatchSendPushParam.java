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
 * 批量推送消息的param
 *
 * @author 3y https://docs.getui.com/getui/server/rest_v2/push/
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BatchSendPushParam {

    /** audience */
    @JSONField(name = "audience")
    private AudienceVO audience;
    /** taskid */
    @JSONField(name = "taskid")
    private String taskId;
    /** isAsync */
    @JSONField(name = "is_async")
    private Boolean isAsync;

    /** AudienceVO */
    @NoArgsConstructor
    @Data
    @Builder
    @AllArgsConstructor
    public static class AudienceVO {

        /** cid */
        @JSONField(name = "cid")
        private Set<String> cid;
    }
}

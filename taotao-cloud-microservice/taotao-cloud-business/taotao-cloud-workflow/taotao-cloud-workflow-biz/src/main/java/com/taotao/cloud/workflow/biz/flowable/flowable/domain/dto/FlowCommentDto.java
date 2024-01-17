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

package com.taotao.cloud.workflow.biz.flowable.flowable.domain.dto;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

/**
 * @author Tony
 * @since 2021/3/28 15:50
 */
@Data
@Builder
public class FlowCommentDto implements Serializable {

    /** 意见类别 0 正常意见 1 退回意见 2 驳回意见 */
    private String type;

    /** 意见内容 */
    private String comment;
}

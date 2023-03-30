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

package com.taotao.cloud.workflow.biz.common.model.engine.flowcomment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/** */
@Data
public class FlowCommentListVO {

    @Schema(description = "附件")
    private String file;

    @Schema(description = "图片")
    private String image;

    @Schema(description = "流程id")
    private String taskId;

    @Schema(description = "文本")
    private String text;

    @Schema(description = "创建人")
    private Long creatorUserId;

    @Schema(description = "创建人")
    private String creatorUserName;

    @Schema(description = "头像")
    private String creatorUserHeadIcon;

    @Schema(description = "创建时间")
    private Long creatorTime;

    @Schema(description = "是否本人")
    private Boolean isDel;

    @Schema(description = "主键")
    private String id;
}

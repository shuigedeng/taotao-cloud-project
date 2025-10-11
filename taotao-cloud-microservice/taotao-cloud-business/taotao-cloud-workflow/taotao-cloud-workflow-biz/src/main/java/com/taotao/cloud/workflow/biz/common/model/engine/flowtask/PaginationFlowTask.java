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

package com.taotao.cloud.workflow.biz.common.model.engine.flowtask;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.taotao.boot.common.model.request.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 分页流任务
 *
 * @author shuigedeng
 * @version 2022.09
 * @since 2022-12-08 10:48:35
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PaginationFlowTask extends PageQuery {

    /** 所属流程id */
    private String flowId;
    /** 开始时间 */
    private String startTime;
    /** 结束时间 */
    private String endTime;
    /** 所属分类code */
    private String flowCategory;
    /** 创建者id */
    private String creatorUserId;
    /** 任务状态 0-草稿、1-处理、2-通过、3-驳回、4-撤销、5-终止 */
    private Integer status;
    /** 是否批量 */
    @Schema(hidden = true)
    @JsonIgnore
    private Integer isBatch;
    /** 节点编码 */
    private String nodeCode;
}

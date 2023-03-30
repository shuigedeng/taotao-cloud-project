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

package com.taotao.cloud.workflow.biz.common.model.form.supplementcard;

import lombok.Data;

/** 补卡申请 */
@Data
public class SupplementCardInfoVO {
    @Schema(description = "主键id")
    private String id;

    @Schema(description = "紧急程度")
    private Integer flowUrgent;

    @Schema(description = "员工姓名")
    private String fullName;

    @Schema(description = "流程主键")
    private String description;

    @Schema(description = "证明人")
    private String witness;

    @Schema(description = "流程标题")
    private String flowTitle;

    @Schema(description = "开始时间")
    private Long startTime;

    @Schema(description = "所在职务")
    private String position;

    @Schema(description = "补卡次数")
    private String supplementNum;

    @Schema(description = "结束时间")
    private Long endTime;

    @Schema(description = "申请日期")
    private Long applyDate;

    @Schema(description = "所在部门")
    private String department;

    @Schema(description = "流程主键")
    private String flowId;

    @Schema(description = "流程单据")
    private String billNo;
}

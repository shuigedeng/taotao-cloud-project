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

package com.taotao.cloud.workflow.biz.common.model.form.archivalborrow;

import lombok.Data;

/** 档案借阅申请 */
@Data
public class ArchivalBorrowInfoVO {
    @Schema(description = "主键id", example = "1")
    private String id;

    @Schema(description = "紧急程度", example = "1")
    private Integer flowUrgent;

    @Schema(description = "申请原因")
    private String applyReason;

    @Schema(description = "档案编码")
    private String archivesId;

    @Schema(description = "借阅方式")
    private String borrowMode;

    @Schema(description = "申请人员")
    private String applyUser;

    @Schema(description = "流程标题")
    private String flowTitle;

    @Schema(description = "归还时间")
    private Long returnDate;

    @Schema(description = "档案名称")
    private String archivesName;

    @Schema(description = "借阅部门")
    private String borrowingDepartment;

    @Schema(description = "借阅时间")
    private Long borrowingDate;

    @Schema(description = "档案属性")
    private String archivalAttributes;

    @Schema(description = "流程主键")
    private String flowId;

    @Schema(description = "流程单据")
    private String billNo;
}

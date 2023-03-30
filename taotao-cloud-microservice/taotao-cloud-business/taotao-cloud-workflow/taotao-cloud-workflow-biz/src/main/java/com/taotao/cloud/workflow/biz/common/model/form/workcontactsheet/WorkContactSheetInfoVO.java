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

package com.taotao.cloud.workflow.biz.common.model.form.workcontactsheet;

import lombok.Data;

/** 工作联系单 */
@Data
public class WorkContactSheetInfoVO {
    @Schema(description = "主键id")
    private String id;

    @Schema(description = "相关附件")
    private String fileJson;

    @Schema(description = "收件部门")
    private String serviceDepartment;

    @Schema(description = "流程标题")
    private String flowTitle;

    @Schema(description = "紧急程度")
    private Integer flowUrgent;

    @Schema(description = "收件人")
    private String recipients;

    @Schema(description = "发件日期")
    private Long toDate;

    @Schema(description = "发件人")
    private String drawPeople;

    @Schema(description = "流程主键")
    private String flowId;

    @Schema(description = "流程单据")
    private String billNo;

    @Schema(description = "收件日期")
    private Long collectionDate;

    @Schema(description = "发件部门")
    private String issuingDepartment;

    @Schema(description = "协调事项")
    private String coordination;
}

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

package com.taotao.cloud.workflow.biz.common.model.form.outboundorder;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import lombok.Data;

/** 出库单 */
@Data
public class OutboundOrderForm {
    @NotBlank(message = "流程主键不能为空")
    @Schema(description = "流程主键")
    private String flowId;

    @NotBlank(message = "流程标题不能为空")
    @Schema(description = "流程标题")
    private String flowTitle;

    @NotNull(message = "紧急程度不能为空")
    @Schema(description = "紧急程度")
    private Integer flowUrgent;

    @NotBlank(message = "流程单据不能为空")
    @Schema(description = "流程单据")
    private String billNo;

    @Schema(description = "客户名称")
    private String customerName;

    @Schema(description = "仓库")
    private String warehouse;

    @Schema(description = "仓库人")
    private String outStorage;

    @Schema(description = "业务人员")
    private String businessPeople;

    @Schema(description = "业务类型")
    private String businessType;

    @NotNull(message = "出库日期不能为空")
    @Schema(description = "出库日期")
    private Long outboundDate;

    @Schema(description = "备注")
    private String description;

    @Schema(description = "提交/保存 0-1")
    private String status;

    @Schema(description = "明细")
    List<OutboundEntryEntityInfoModel> entryList;

    @Schema(description = "候选人")
    private Map<String, List<String>> candidateList;
}

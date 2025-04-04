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

package com.taotao.cloud.workflow.biz.common.model.form.contractapproval;

import java.math.BigDecimal;
import lombok.Data;
import lombok.experimental.*;

/** 合同审批 */
@Data
public class ContractApprovalInfoVO {
    @Schema(description = "主键id")
    private String id;

    @Schema(description = "相关附件")
    private String fileJson;

    @Schema(description = "乙方单位")
    private String secondPartyUnit;

    @Schema(description = "合同分类")
    private String contractClass;

    @Schema(description = "紧急程度")
    private Integer flowUrgent;

    @Schema(description = "甲方单位")
    private String firstPartyUnit;

    @Schema(description = "结束时间")
    private Long endDate;

    @Schema(description = "合同类型")
    private String contractType;

    @Schema(description = "签约时间")
    private Long signingDate;

    @Schema(description = "甲方负责人")
    private String firstPartyPerson;

    @Schema(description = "收入金额")
    private BigDecimal incomeAmount;

    @Schema(description = "备注")
    private String description;

    @Schema(description = "填写人员")
    private String inputPerson;

    @Schema(description = "主要内容")
    private String primaryCoverage;

    @Schema(description = "流程标题")
    private String flowTitle;

    @Schema(description = "甲方联系方式")
    private String firstPartyContact;

    @Schema(description = "合同编码")
    private String contractId;

    @Schema(description = "乙方联系方式")
    private String secondPartyContact;

    @Schema(description = "合同名称")
    private String contractName;

    @Schema(description = "业务人员")
    private String businessPerson;

    @Schema(description = "流程主键")
    private String flowId;

    @Schema(description = "流程单据")
    private String billNo;

    @Schema(description = "乙方负责人")
    private String secondPartyPerson;

    @Schema(description = "开始时间")
    private Long startDate;
}

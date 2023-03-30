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

package com.taotao.cloud.workflow.biz.common.model.form.paymentapply;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import lombok.Data;

/** 付款申请单 */
@Data
public class PaymentApplyForm {
    @NotNull(message = "紧急程度不能为空")
    @Schema(description = "紧急程度")
    private Integer flowUrgent;

    @Schema(description = "用途名称")
    private String purposeName;

    @Schema(description = "备注")
    private String description;

    @NotBlank(message = "结算方式不能为空")
    @Schema(description = "结算方式")
    private String settlementMethod;

    @NotBlank(message = "开户银行不能为空")
    @Schema(description = "开户银行")
    private String openingBank;

    @Schema(description = "申请金额")
    private BigDecimal applyAmount;

    @NotBlank(message = "付款类型不能为空")
    @Schema(description = "付款类型")
    private String paymentType;

    @NotBlank(message = "申请人不能为空")
    @Schema(description = "申请人")
    private String applyUser;

    @NotBlank(message = "流程标题不能为空")
    @Schema(description = "流程标题")
    private String flowTitle;

    @Schema(description = "联系方式")
    private String receivableContact;

    @Schema(description = "付款金额")
    private BigDecimal amountPaid;

    @Schema(description = "项目类别")
    private String projectCategory;

    @NotBlank(message = "申请部门不能为空")
    @Schema(description = "申请部门")
    private String departmental;

    @Schema(description = "项目负责人")
    private String projectLeader;

    @Schema(description = "收款账号")
    private String beneficiaryAccount;

    @Schema(description = "付款单位")
    private String paymentUnit;

    @NotNull(message = "申请时间不能为空")
    @Schema(description = "申请时间")
    private Long applyDate;

    @NotBlank(message = "流程主键不能为空")
    @Schema(description = "流程主键")
    private String flowId;

    @NotBlank(message = "流程单据不能为空")
    @Schema(description = "流程单据")
    private String billNo;

    @Schema(description = "提交/保存 0-1")
    private String status;

    @Schema(description = "候选人")
    private Map<String, List<String>> candidateList;
}

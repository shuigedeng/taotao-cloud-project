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

package com.taotao.cloud.workflow.biz.common.model.form.debitbill;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import lombok.Data;

/** 借支单 */
@Data
public class DebitBillForm {
    @Schema(description = "借款原因")
    private String reason;

    @NotNull(message = "紧急程度不能为空")
    @Schema(description = "紧急程度")
    private Integer flowUrgent;

    @Schema(description = "还款票据")
    private String repaymentBill;

    @NotBlank(message = "必填")
    @Schema(description = "流程标题")
    private String flowTitle;

    @NotBlank(message = "必填")
    @Schema(description = "员工职务")
    private String staffPost;

    @Schema(description = "还款日期")
    private Long teachingDate;

    @Schema(description = "工作部门")
    private String departmental;

    @NotBlank(message = "必填")
    @Schema(description = "员工姓名")
    private String staffName;

    @NotBlank(message = "必填")
    @Schema(description = "借款方式")
    private String loanMode;

    @NotBlank(message = "必填")
    @Schema(description = "支付方式")
    private String paymentMethod;

    @Schema(description = "转账账户")
    private String transferAccount;

    @Schema(description = "借支金额")
    private BigDecimal amountDebit;

    @NotNull(message = "申请日期不能为空")
    @Schema(description = "申请日期")
    private Long applyDate;

    @NotBlank(message = "必填")
    @Schema(description = "流程主键")
    private String flowId;

    @NotBlank(message = "必填")
    @Schema(description = "流程单据")
    private String billNo;

    @NotBlank(message = "必填")
    @Schema(description = "员工编码")
    private String staffId;

    @Schema(description = "提交/保存 0-1")
    private String status;

    @Schema(description = "候选人")
    private Map<String, List<String>> candidateList;
}

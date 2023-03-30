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

package com.taotao.cloud.workflow.biz.common.model.form.expenseexpenditure;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import lombok.Data;

/** 费用支出单 */
@Data
public class ExpenseExpenditureForm {
    @Schema(description = "银行账号")
    private String bankAccount;

    @NotNull(message = "必填")
    @Schema(description = "紧急程度")
    private Integer flowUrgent;

    @Schema(description = "支付金额")
    private BigDecimal amountPayment;

    @Schema(description = "备注")
    private String description;

    @Schema(description = "非合同支出")
    private String nonContract;

    @NotBlank(message = "必填")
    @Schema(description = "申请人员")
    private String applyUser;

    @NotBlank(message = "必填")
    @Schema(description = "流程标题")
    private String flowTitle;

    @Schema(description = "合计费用")
    private BigDecimal total;

    @Schema(description = "开户银行")
    private String accountOpeningBank;

    @NotBlank(message = "必填")
    @Schema(description = "支付方式")
    private String paymentMethod;

    @Schema(description = "合同编码")
    private String contractNum;

    @Schema(description = "开户姓名")
    private String openAccount;

    @NotNull(message = "必填")
    @Schema(description = "申请日期")
    private Long applyDate;

    @NotBlank(message = "必填")
    @Schema(description = "申请部门")
    private String department;

    @NotBlank(message = "必填")
    @Schema(description = "流程主键")
    private String flowId;

    @NotBlank(message = "必填")
    @Schema(description = "流程单据")
    private String billNo;

    @Schema(description = "提交/保存 0-1")
    private String status;

    @Schema(description = "候选人")
    private Map<String, List<String>> candidateList;
}

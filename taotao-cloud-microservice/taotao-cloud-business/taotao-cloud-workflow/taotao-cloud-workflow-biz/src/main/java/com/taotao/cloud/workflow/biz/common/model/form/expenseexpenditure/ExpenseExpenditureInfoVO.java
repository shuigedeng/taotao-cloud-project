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

import java.math.BigDecimal;
import lombok.Data;

/** 费用支出单 */
@Data
public class ExpenseExpenditureInfoVO {
    @Schema(description = "主键id")
    private String id;

    @Schema(description = "银行账号")
    private String bankAccount;

    @Schema(description = "紧急程度")
    private Integer flowUrgent;

    @Schema(description = "支付金额")
    private BigDecimal amountPayment;

    @Schema(description = "备注")
    private String description;

    @Schema(description = "非合同支出")
    private String nonContract;

    @Schema(description = "申请人员")
    private String applyUser;

    @Schema(description = "流程标题")
    private String flowTitle;

    @Schema(description = "合计费用")
    private BigDecimal total;

    @Schema(description = "开户银行")
    private String accountOpeningBank;

    @Schema(description = "支付方式")
    private String paymentMethod;

    @Schema(description = "合同编码")
    private String contractNum;

    @Schema(description = "开户姓名")
    private String openAccount;

    @Schema(description = "申请日期")
    private Long applyDate;

    @Schema(description = "所在部门")
    private String department;

    @Schema(description = "流程主键")
    private String flowId;

    @Schema(description = "流程单据")
    private String billNo;
}

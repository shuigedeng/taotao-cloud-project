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

import java.math.BigDecimal;
import lombok.Data;

/** 借支单 */
@Data
public class DebitBillInfoVO {
    @Schema(description = "主键id")
    private String id;

    @Schema(description = "借款原因")
    private String reason;

    @Schema(description = "紧急程度")
    private Integer flowUrgent;

    @Schema(description = "还款票据")
    private String repaymentBill;

    @Schema(description = "流程标题")
    private String flowTitle;

    @Schema(description = "员工职务")
    private String staffPost;

    @Schema(description = "还款日期")
    private Long teachingDate;

    @Schema(description = "工作部门")
    private String departmental;

    @Schema(description = "员工姓名")
    private String staffName;

    @Schema(description = "借款方式")
    private String loanMode;

    @Schema(description = "支付方式")
    private String paymentMethod;

    @Schema(description = "转账账户")
    private String transferAccount;

    @Schema(description = "借支金额")
    private BigDecimal amountDebit;

    @Schema(description = "申请日期")
    private Long applyDate;

    @Schema(description = "流程主键")
    private String flowId;

    @Schema(description = "流程单据")
    private String billNo;

    @Schema(description = "员工编码")
    private String staffId;
}

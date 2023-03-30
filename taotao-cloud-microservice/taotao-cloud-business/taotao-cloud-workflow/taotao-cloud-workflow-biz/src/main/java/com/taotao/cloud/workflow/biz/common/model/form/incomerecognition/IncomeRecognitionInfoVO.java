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

package com.taotao.cloud.workflow.biz.common.model.form.incomerecognition;

import java.math.BigDecimal;
import lombok.Data;

/** 收入确认分析表 */
@Data
public class IncomeRecognitionInfoVO {
    @Schema(description = "主键id")
    private String id;

    @Schema(description = "紧急程度")
    private Integer flowUrgent;

    @Schema(description = "联系人姓名")
    private String contactName;

    @Schema(description = "联系电话")
    private String contacPhone;

    @Schema(description = "到款金额")
    private BigDecimal actualAmount;

    @Schema(description = "到款银行")
    private String moneyBank;

    @Schema(description = "备注")
    private String description;

    @Schema(description = "客户名称")
    private String customerName;

    @Schema(description = "合同金额")
    private BigDecimal totalAmount;

    @Schema(description = "流程标题")
    private String flowTitle;

    @Schema(description = "未付金额")
    private BigDecimal unpaidAmount;

    @Schema(description = "已付金额")
    private BigDecimal amountPaid;

    @Schema(description = "结算月份")
    private String settlementMonth;

    @Schema(description = "合同编码")
    private String contractNum;

    @Schema(description = "到款日期")
    private Long paymentDate;

    @Schema(description = "流程主键")
    private String flowId;

    @Schema(description = "流程单据")
    private String billNo;

    @Schema(description = "联系QQ")
    private String contactQQ;
}

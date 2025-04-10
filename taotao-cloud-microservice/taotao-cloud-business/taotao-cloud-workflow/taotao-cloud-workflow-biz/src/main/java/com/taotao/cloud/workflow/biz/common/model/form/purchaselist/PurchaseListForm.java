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

package com.taotao.cloud.workflow.biz.common.model.form.purchaselist;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.experimental.*;

/** 日常物品采购清单 */
@Data
public class PurchaseListForm {
    @NotBlank(message = "必填")
    @Schema(description = "流程主键")
    private String flowId;

    @NotBlank(message = "必填")
    @Schema(description = "流程标题")
    private String flowTitle;

    @NotNull(message = "紧急程度不能为空")
    @Schema(description = "紧急程度")
    private Integer flowUrgent;

    @NotBlank(message = "必填")
    @Schema(description = "流程单据")
    private String billNo;

    @NotBlank(message = "必填")
    @Schema(description = "申请人")
    private String applyUser;

    @NotBlank(message = "必填")
    @Schema(description = "所在部门")
    private String departmental;

    @Schema(description = "供应商名称")
    private String vendorName;

    @Schema(description = "采购人员")
    private String buyer;

    @NotNull(message = "采购日期不能为空")
    @Schema(description = "采购日期")
    private Long purchaseDate;

    @Schema(description = "仓库")
    private String warehouse;

    @Schema(description = "联系方式")
    private String telephone;

    @NotBlank(message = "必填")
    @Schema(description = "支付方式")
    private String paymentMethod;

    @Schema(description = "支付总额")
    private BigDecimal paymentMoney;

    @Schema(description = "相关附件")
    private String fileJson;

    @Schema(description = "用途原因")
    private String reason;

    @Schema(description = "提交/保存 0-1")
    private String status;

    @Schema(description = "明细")
    List<PurchaseListEntryEntityInfoModel> entryList;

    @Schema(description = "候选人")
    private Map<String, List<String>> candidateList;
}

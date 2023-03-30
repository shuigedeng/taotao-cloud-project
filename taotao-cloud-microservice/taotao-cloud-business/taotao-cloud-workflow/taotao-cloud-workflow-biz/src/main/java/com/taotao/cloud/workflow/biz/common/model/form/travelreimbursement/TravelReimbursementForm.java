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

package com.taotao.cloud.workflow.biz.common.model.form.travelreimbursement;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import lombok.Data;

/** 差旅报销申请表 */
@Data
public class TravelReimbursementForm {
    @Schema(description = "车船费")
    private Long fare;

    @Schema(description = "其他费用")
    private Long other;

    @NotNull(message = "必填")
    @Schema(description = "紧急程度")
    private Integer flowUrgent;

    @NotBlank(message = "必填")
    @Schema(description = "出差任务")
    private String businessMission;

    @Schema(description = "出差人员")
    private String travelerUser;

    @NotBlank(message = "必填")
    @Schema(description = "到达地")
    private String destination;

    @Schema(description = "出差补助")
    private Long travelAllowance;

    @Schema(description = "票据数")
    private String billsNum;

    @NotBlank(message = "必填")
    @Schema(description = "申请人")
    private String applyUser;

    @Schema(description = "故障报修费")
    private Long breakdownFee;

    @NotNull(message = "必填")
    @Schema(description = "回归日期")
    private Long returnDate;

    @Schema(description = "合计")
    private Long total;

    @Schema(description = "机票费")
    private Long planeTicket;

    @Schema(description = "停车费")
    private Long parkingRate;

    @Schema(description = "住宿费用")
    private Long getAccommodation;

    @Schema(description = "报销金额")
    private Long reimbursementAmount;

    @NotBlank(message = "必填")
    @Schema(description = "流程主键")
    private String flowId;

    @NotBlank(message = "必填")
    @Schema(description = "流程单据")
    private String billNo;

    @Schema(description = "报销编码")
    private String reimbursementId;

    @NotNull(message = "必填")
    @Schema(description = "出发日期")
    private Long setOutDate;

    @Schema(description = "补找金额")
    private Long sumOfMoney;

    @Schema(description = "借款金额")
    private Long loanAmount;

    @Schema(description = "车辆里程")
    private Long vehicleMileage;

    @NotBlank(message = "必填")
    @Schema(description = "流程标题")
    private String flowTitle;

    @Schema(description = "过路费")
    private Long roadFee;

    @NotBlank(message = "必填")
    @Schema(description = "申请部门")
    private String departmental;

    @Schema(description = "轨道交通费")
    private Long railTransit;

    @NotNull(message = "必填")
    @Schema(description = "申请时间")
    private Long applyDate;

    @Schema(description = "餐补费用")
    private Long mealAllowance;

    @Schema(description = "提交/保存 0-1")
    private String status;

    @Schema(description = "候选人")
    private Map<String, List<String>> candidateList;
}

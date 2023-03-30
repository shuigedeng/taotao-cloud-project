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

package com.taotao.cloud.workflow.biz.common.model.form.salessupport;

import lombok.Data;

/** 销售支持表 */
@Data
public class SalesSupportInfoVO {
    @Schema(description = "主键id")
    private String id;

    @Schema(description = "相关附件")
    private String fileJson;

    @Schema(description = "紧急程度")
    private Integer flowUrgent;

    @Schema(description = "售前支持")
    private String pSaleSupInfo;

    @Schema(description = "结束时间")
    private Long endDate;

    @Schema(description = "相关项目")
    private String project;

    @Schema(description = "交付说明")
    private String consultResult;

    @Schema(description = "售前顾问")
    private String pSalSupConsul;

    @Schema(description = "咨询评价")
    private String iEvaluation;

    @Schema(description = "支持天数")
    private String pSaleSupDays;

    @Schema(description = "发起人总结")
    private String conclusion;

    @Schema(description = "申请人")
    private String applyUser;

    @Schema(description = "准备天数")
    private String pSalePreDays;

    @Schema(description = "流程标题")
    private String flowTitle;

    @Schema(description = "销售总结")
    private String salSupConclu;

    @Schema(description = "申请部门")
    private String applyDept;

    @Schema(description = "机构咨询")
    private String consulManager;

    @Schema(description = "申请日期")
    private Long applyDate;

    @Schema(description = "流程主键")
    private String flowId;

    @Schema(description = "流程单据")
    private String billNo;

    @Schema(description = "开始时间")
    private Long startDate;

    @Schema(description = "相关客户")
    private String customer;
}

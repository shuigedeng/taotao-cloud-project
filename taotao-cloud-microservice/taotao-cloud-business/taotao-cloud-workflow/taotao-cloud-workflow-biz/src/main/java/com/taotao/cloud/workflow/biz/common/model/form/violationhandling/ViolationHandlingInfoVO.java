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

package com.taotao.cloud.workflow.biz.common.model.form.violationhandling;

import java.math.BigDecimal;
import lombok.Data;

/** 违章处理申请表 */
@Data
public class ViolationHandlingInfoVO {
    @Schema(description = "主键id")
    private String id;

    @Schema(description = "紧急程度")
    private Integer flowUrgent;

    @Schema(description = "违章地点")
    private String violationSite;

    @Schema(description = "备注")
    private String description;

    @Schema(description = "车牌号")
    private String plateNum;

    @Schema(description = "负责人")
    private String leadingOfficial;

    @Schema(description = "违章日期")
    private Long peccancyDate;

    @Schema(description = "流程标题")
    private String flowTitle;

    @Schema(description = "驾驶人")
    private String driver;

    @Schema(description = "违章扣分")
    private String deduction;

    @Schema(description = "通知日期")
    private Long noticeDate;

    @Schema(description = "限处理日期")
    private Long limitDate;

    @Schema(description = "违章行为")
    private String violationBehavior;

    @Schema(description = "违章罚款")
    private BigDecimal amountMoney;

    @Schema(description = "流程主键")
    private String flowId;

    @Schema(description = "流程单据")
    private String billNo;
}

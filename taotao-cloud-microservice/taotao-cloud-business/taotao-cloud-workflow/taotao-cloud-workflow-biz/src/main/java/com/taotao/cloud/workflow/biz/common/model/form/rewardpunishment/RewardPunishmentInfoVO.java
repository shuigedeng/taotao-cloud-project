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

package com.taotao.cloud.workflow.biz.common.model.form.rewardpunishment;

import java.math.BigDecimal;
import lombok.Data;

/** 行政赏罚单 */
@Data
public class RewardPunishmentInfoVO {
    @Schema(description = "主键id")
    private String id;

    @Schema(description = "赏罚原因")
    private String reason;

    @Schema(description = "流程标题")
    private String flowTitle;

    @Schema(description = "紧急程度")
    private Integer flowUrgent;

    @Schema(description = "员工姓名")
    private String fullName;

    @Schema(description = "填表日期")
    private Long fillFromDate;

    @Schema(description = "赏罚金额")
    private BigDecimal rewardPun;

    @Schema(description = "员工职位")
    private String position;

    @Schema(description = "员工部门")
    private String department;

    @Schema(description = "流程主键")
    private String flowId;

    @Schema(description = "流程单据")
    private String billNo;
}

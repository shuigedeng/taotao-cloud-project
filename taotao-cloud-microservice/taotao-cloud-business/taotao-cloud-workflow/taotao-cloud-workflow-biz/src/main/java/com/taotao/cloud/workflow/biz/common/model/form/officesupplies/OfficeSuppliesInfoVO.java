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

package com.taotao.cloud.workflow.biz.common.model.form.officesupplies;

import lombok.Data;

/** 领用办公用品申请表 */
@Data
public class OfficeSuppliesInfoVO {
    @Schema(description = "主键id")
    private String id;

    @Schema(description = "用品名称")
    private String articlesName;

    @Schema(description = "紧急程度")
    private Integer flowUrgent;

    @Schema(description = "用品分类")
    private String classification;

    @Schema(description = "用品编码")
    private String articlesId;

    @Schema(description = "申请原因")
    private String applyReasons;

    @Schema(description = "申请部门")
    private String applyUser;

    @Schema(description = "流程标题")
    private String flowTitle;

    @Schema(description = "领用仓库")
    private String useStock;

    @Schema(description = "用品数量")
    private String articlesNum;

    @Schema(description = "申请时间")
    private Long applyDate;

    @Schema(description = "所属部门")
    private String department;

    @Schema(description = "流程主键")
    private String flowId;

    @Schema(description = "流程单据")
    private String billNo;
}

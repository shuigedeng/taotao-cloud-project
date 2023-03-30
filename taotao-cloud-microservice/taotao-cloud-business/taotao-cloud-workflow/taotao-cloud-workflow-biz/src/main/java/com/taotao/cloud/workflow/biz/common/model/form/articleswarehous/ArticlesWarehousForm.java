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

package com.taotao.cloud.workflow.biz.common.model.form.articleswarehous;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import lombok.Data;

/** 用品入库申请表 */
@Data
public class ArticlesWarehousForm {
    @NotNull(message = "必填")
    @Schema(description = "紧急程度")
    private Integer flowUrgent;

    @Schema(description = "用品分类")
    private String classification;

    @Schema(description = "用品编码")
    private String articlesId;

    @Schema(description = "申请原因")
    private String applyReasons;

    @NotBlank(message = "必填")
    @Schema(description = "申请人员")
    private String applyUser;

    @NotBlank(message = "必填")
    @Schema(description = "流程标题")
    private String flowTitle;

    @Schema(description = "数量")
    private String estimatePeople;

    @Schema(description = "单位")
    private String company;

    @NotNull(message = "必填")
    @Schema(description = "申请时间")
    private Long applyDate;

    @NotBlank(message = "必填")
    @Schema(description = "所属部门")
    private String department;

    @NotBlank(message = "必填")
    @Schema(description = "流程主键")
    private String flowId;

    @NotBlank(message = "必填")
    @Schema(description = "流程单据")
    private String billNo;

    @Schema(description = "用品库存")
    private String articles;

    @Schema(description = "提交/保存 0-1")
    private String status;

    @Schema(description = "候选人")
    private Map<String, List<String>> candidateList;
}

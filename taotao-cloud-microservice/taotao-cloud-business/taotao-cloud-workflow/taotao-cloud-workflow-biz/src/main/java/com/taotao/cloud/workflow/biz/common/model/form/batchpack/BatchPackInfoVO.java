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

package com.taotao.cloud.workflow.biz.common.model.form.batchpack;

import lombok.Data;

/** 批包装指令 */
@Data
public class BatchPackInfoVO {
    @Schema(description = "主键id")
    private String id;

    @Schema(description = "产品规格")
    private String standard;

    @Schema(description = "操作日期")
    private Long operationDate;

    @Schema(description = "紧急程度")
    private Integer flowUrgent;

    @Schema(description = "编制人员")
    private String compactor;

    @Schema(description = "生产车间")
    private String production;

    @Schema(description = "备注")
    private String description;

    @Schema(description = "包装规格")
    private String packing;

    @Schema(description = "编制日期")
    private Long compactorDate;

    @Schema(description = "产品名称")
    private String productName;

    @Schema(description = "流程标题")
    private String flowTitle;

    @Schema(description = "工艺规程")
    private String regulations;

    @Schema(description = "批产数量")
    private String productionQuty;

    @Schema(description = "入库序号")
    private String warehousNo;

    @Schema(description = "流程主键")
    private String flowId;

    @Schema(description = "流程单据")
    private String billNo;
}

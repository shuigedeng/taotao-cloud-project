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

package com.taotao.cloud.workflow.biz.common.model.form.documentapproval;

import lombok.Data;

/** 文件签批意见表 */
@Data
public class DocumentApprovalInfoVO {
    @Schema(description = "主键id")
    private String id;

    @Schema(description = "拟稿人")
    private String draftedPerson;

    @Schema(description = "相关附件")
    private String fileJson;

    @Schema(description = "流程标题")
    private String flowTitle;

    @Schema(description = "文件名称")
    private String fileName;

    @Schema(description = "紧急程度")
    private Integer flowUrgent;

    @Schema(description = "发文单位")
    private String serviceUnit;

    @Schema(description = "修改意见")
    private String modifyOpinion;

    @Schema(description = "收文日期")
    private Long receiptDate;

    @Schema(description = "文件拟办")
    private String fillPreparation;

    @Schema(description = "流程主键")
    private String flowId;

    @Schema(description = "流程单据")
    private String billNo;

    @Schema(description = "文件编码")
    private String fillNum;
}

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

package com.taotao.cloud.workflow.biz.form.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/** 批包装指令 */
@Data
@TableName("wform_batchpack")
public class BatchPackEntity {
    /** 主键 */
    @TableId("F_ID")
    private String id;

    /** 流程主键 */
    @TableField("F_FLOWID")
    private String flowId;

    /** 流程标题 */
    @TableField("F_FLOWTITLE")
    private String flowTitle;

    /** 紧急程度 */
    @TableField("F_FLOWURGENT")
    private Integer flowUrgent;

    /** 流程单据 */
    @TableField("F_BILLNO")
    private String billNo;

    /** 产品名称 */
    @TableField("F_PRODUCTNAME")
    private String productName;

    /** 生产车间 */
    @TableField("F_PRODUCTION")
    private String production;

    /** 编制人员 */
    @TableField("F_COMPACTOR")
    private String compactor;

    /** 编制日期 */
    @TableField("F_COMPACTORDATE")
    private Date compactorDate;

    /** 产品规格 */
    @TableField("F_STANDARD")
    private String standard;

    /** 入库序号 */
    @TableField("F_WAREHOUSNO")
    private String warehousNo;

    /** 批产数量 */
    @TableField("F_PRODUCTIONQUTY")
    private String productionQuty;

    /** 操作日期 */
    @TableField("F_OPERATIONDATE")
    private Date operationDate;

    /** 工艺规程 */
    @TableField("F_REGULATIONS")
    private String regulations;

    /** 包装规格 */
    @TableField("F_PACKING")
    private String packing;

    /** 备注 */
    @TableField("F_DESCRIPTION")
    private String description;
}

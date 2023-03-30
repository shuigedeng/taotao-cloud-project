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

/** 入库申请单 */
@Data
@TableName("wform_warehousereceipt")
public class WarehouseReceiptEntity {
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

    /** 供应商名称 */
    @TableField("F_SUPPLIERNAME")
    private String supplierName;

    /** 联系电话 */
    @TableField("F_CONTACTPHONE")
    private String contactPhone;

    /** 入库类别 */
    @TableField("F_WAREHOUSCATEGORY")
    private String warehousCategory;

    /** 仓库 */
    @TableField("F_WAREHOUSE")
    private String warehouse;

    /** 入库人 */
    @TableField("F_WAREHOUSESPEOPLE")
    private String warehousesPeople;

    /** 送货单号 */
    @TableField("F_DELIVERYNO")
    private String deliveryNo;

    /** 入库单号 */
    @TableField("F_WAREHOUSENO")
    private String warehouseNo;

    /** 入库日期 */
    @TableField("F_WAREHOUSDATE")
    private Date warehousDate;
}

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
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/** 采购原材料 */
@Data
@TableName("wform_procurementmaterial")
public class ProcurementMaterialEntity {
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

    /** 申请人 */
    @TableField("F_APPLYUSER")
    private String applyUser;

    /** 申请部门 */
    @TableField("F_DEPARTMENTAL")
    private String departmental;

    /** 申请日期 */
    @TableField("F_APPLYDATE")
    private Date applyDate;

    /** 采购单位 */
    @TableField("F_PURCHASEUNIT")
    private String purchaseUnit;

    /** 送货方式 */
    @TableField("F_DELIVERYMODE")
    private String deliveryMode;

    /** 送货地址 */
    @TableField("F_DELIVERYADDRESS")
    private String deliveryAddress;

    /** 付款方式 */
    @TableField("F_PAYMENTMETHOD")
    private String paymentMethod;

    /** 付款金额 */
    @TableField("F_PAYMENTMONEY")
    private BigDecimal paymentMoney;

    /** 相关附件 */
    @TableField("F_FILEJSON")
    private String fileJson;

    /** 用途原因 */
    @TableField("F_REASON")
    private String reason;
}

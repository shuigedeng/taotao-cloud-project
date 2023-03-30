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
import lombok.Data;

/** 入库单明细 */
@Data
@TableName("wform_warehousereceiptentry")
public class WarehouseEntryEntity {
    /** 主键 */
    @TableId("F_ID")
    private String id;

    /** 入库主键 */
    @TableField("F_WAREHOUSEID")
    private String warehouseId;

    /** 商品名称 */
    @TableField("F_GOODSNAME")
    private String goodsName;

    /** 规格型号 */
    @TableField("F_SPECIFICATIONS")
    private String specifications;

    /** 单位 */
    @TableField("F_UNIT")
    private String unit;

    /** 数量 */
    @TableField("F_QTY")
    private BigDecimal qty;

    /** 单价 */
    @TableField("F_PRICE")
    private BigDecimal price;

    /** 金额 */
    @TableField("F_AMOUNT")
    private BigDecimal amount;

    /** 备注 */
    @TableField("F_DESCRIPTION")
    private String description;

    /** 排序码 */
    @TableField("F_SORTCODE")
    private Long sortCode;
}

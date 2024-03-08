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

package com.taotao.cloud.workflow.biz.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

@Data
@TableName("base_billrule")
public class BillRuleEntity {

    /** 单据主键 */
    @TableId("F_ID")
    private String id;

    /** 单据名称 */
    @TableField("F_FULLNAME")
    private String fullName;

    /** 单据编码 */
    @TableField("F_ENCODE")
    private String enCode;

    /** 单据前缀 */
    @TableField("F_PREFIX")
    private String prefix;

    /** 日期格式 */
    @TableField("F_DATEFORMAT")
    private String dateFormat;

    /** 流水位数 */
    @TableField("F_DIGIT")
    private Integer digit;

    /** 流水起始 */
    @TableField("F_STARTNUMBER")
    private String startNumber;

    /** 流水范例 */
    @TableField("F_EXAMPLE")
    private String example;

    /** 当前流水号 */
    @TableField("F_THISNUMBER")
    private Integer thisNumber;

    /** 输出流水号 */
    @TableField("F_OUTPUTNUMBER")
    private String outputNumber;

    /** 描述 */
    @TableField("F_DESCRIPTION")
    private String description;

    /** 排序码 */
    @TableField("F_SORTCODE")
    private Long sortCode;

    /** 有效标志 */
    @TableField("F_ENABLEDMARK")
    private Integer enabledMark;

    /** 创建时间 */
    @TableField(value = "F_CREATORTIME", fill = FieldFill.INSERT)
    private Date creatorTime;

    /** 创建用户 */
    @TableField(value = "F_CREATORUSERID", fill = FieldFill.INSERT)
    private String creatorUserId;

    /** 修改时间 */
    @TableField(value = "F_LASTMODIFYTIME", fill = FieldFill.UPDATE)
    private Date lastModifyTime;

    /** 修改用户 */
    @TableField(value = "F_LASTMODIFYUSERID", fill = FieldFill.UPDATE)
    private String lastModifyUserId;

    /** 删除时间 */
    @TableField("F_DELETETIME")
    private Date deleteTime;

    /** 删除用户 */
    @TableField("F_DELETEUSERID")
    private String deleteUserId;

    /** 删除标志 */
    @TableField("F_DELETEMARK")
    private Integer deleteMark;
}

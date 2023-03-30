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

/** 档案借阅申请 */
@Data
@TableName("wform_archivalborrow")
public class ArchivalBorrowEntity {
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

    /** 申请人员 */
    @TableField("F_APPLYUSER")
    private String applyUser;

    /** 借阅部门 */
    @TableField("F_BORROWINGDEPARTMENT")
    private String borrowingDepartment;

    /** 档案名称 */
    @TableField("F_ARCHIVESNAME")
    private String archivesName;

    /** 借阅时间 */
    @TableField("F_BORROWINGDATE")
    private Date borrowingDate;

    /** 归还时间 */
    @TableField("F_RETURNDATE")
    private Date returnDate;

    /** 档案属性 */
    @TableField("F_ARCHIVALATTRIBUTES")
    private String archivalAttributes;

    /** 借阅方式 */
    @TableField("F_BORROWMODE")
    private String borrowMode;

    /** 申请原因 */
    @TableField("F_APPLYREASON")
    private String applyReason;

    /** 档案编码 */
    @TableField("F_ARCHIVESID")
    private String archivesId;
}

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

/** 工作联系单 */
@Data
@TableName("wform_workcontactsheet")
public class WorkContactSheetEntity {
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

    /** 发件人 */
    @TableField("F_DRAWPEOPLE")
    private String drawPeople;

    /** 发件部门 */
    @TableField("F_ISSUINGDEPARTMENT")
    private String issuingDepartment;

    /** 发件日期 */
    @TableField("F_TODATE")
    private Date toDate;

    /** 收件部门 */
    @TableField("F_SERVICEDEPARTMENT")
    private String serviceDepartment;

    /** 收件人 */
    @TableField("F_RECIPIENTS")
    private String recipients;

    /** 收件日期 */
    @TableField("F_COLLECTIONDATE")
    private Date collectionDate;

    /** 协调事项 */
    @TableField("F_COORDINATION")
    private String coordination;

    /** 相关附件 */
    @TableField("F_FILEJSON")
    private String fileJson;
}

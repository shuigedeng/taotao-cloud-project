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

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;
import lombok.experimental.*;

@Data
@TableName("base_comfields")
public class ComFieldsEntity {

    @TableId("F_ID")
    private String id;

    @TableField("F_FIELDNAME")
    private String fieldName;

    @TableField("F_FIELD")
    private String field;

    @TableField("F_DATATYPE")
    private String datatype;

    @TableField("F_DATALENGTH")
    private String datalength;

    @TableField("F_ALLOWNULL")
    private String allowNull;

    @TableField("F_DESCRIPTION")
    private String description;

    @TableField("F_SORTCODE")
    private Long sortcode;

    @TableField("F_ENABLEDMARK")
    private Integer enabledmark;

    @TableField("F_CREATORTIME")
    private Date creatortime;

    @TableField("F_CREATORUSERID")
    private String creatoruserid;

    @TableField("F_LASTMODIFYTIME")
    @JSONField(name = "F_LastModifyTime")
    private Date lastModifyTime;

    @TableField("F_LASTMODIFYUSERID")
    private String lastmodifyuserid;

    @TableField("F_DELETEMARK")
    private Integer deletemark;

    @TableField("F_DELETETIME")
    private Date deletetime;

    @TableField("F_DELETEUSERID")
    private String deleteuserid;
}

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

package com.taotao.cloud.workflow.biz.engine.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.boot.webagg.entity.SuperEntity;
import java.util.Date;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.*;

/** 流程引擎 */
@Data
@TableName("flow_engine")
@Accessors(chain=true)
public class FlowEngineEntity extends SuperEntity<FlowEngineEntity, String> {

    /** 流程主键 */
    @TableId("id")
    private String id;

    /** 流程编码 */
    @TableField("en_code")
    private String enCode;

    /** 流程名称 */
    @TableField("full_name")
    private String fullName;

    /** 流程类型(0.发起流程 1.功能流程) */
    @TableField("type")
    private Integer type;

    /** 流程分类 */
    @TableField("category")
    private String category;

    /** 可见类型 0-全部可见、1-指定经办 */
    @TableField("visible_type")
    private Integer visibleType;

    /** 图标 */
    @TableField("icon")
    private String icon;

    /** 图标背景色 */
    @TableField("icon_background")
    private String iconBackground;

    /** 流程版本 */
    @TableField("version")
    private String version;

    /** 表单字段 */
    @TableField("form_template_json")
    private String formData;

    /** 表单分类(1.系统表单 2.自定义表单) */
    @TableField("form_type")
    private Integer formType;

    /** 流程引擎 */
    @TableField("flow_template_json")
    private String flowTemplateJson;

    /** 描述 */
    @TableField("description")
    private String description;

    /** 列表 */
    @TableField("tables")
    @JSONField(name = "tables")
    private String flowTables;

    /** 数据连接 */
    @TableField("db_link_id")
    private String dbLinkId;

    /** app表单路径 */
    @TableField("app_formurl")
    private String appFormUrl;

    /** pc表单路径 */
    @TableField("formurl")
    private String formUrl;

    /** 排序码 */
    @TableField("sort_code")
    private Long sortCode;

    /** 有效标志 */
    @TableField("enabled_mark")
    private Integer enabledMark;

    /** 创建时间 */
    @TableField(value = "creator_time", fill = FieldFill.INSERT)
    private Date creatorTime;

    /** 创建用户 */
    @TableField(value = "creator_user_id", fill = FieldFill.INSERT)
    private Long creatorUser;

    /** 修改时间 */
    @TableField(value = "lastmodify_time", fill = FieldFill.UPDATE)
    private Date lastModifyTime;

    /** 修改用户 */
    @TableField(value = "lastmodify_user_id", fill = FieldFill.UPDATE)
    private Long lastModifyUser;

    /** 删除标志 */
    @TableField("delete_mark")
    private Integer deleteMark;

    /** 删除时间 */
    @TableField("delete_time")
    private Date deleteTime;

    /** 删除用户 */
    @TableField("delete_user_id")
    private String deleteUserId;
}

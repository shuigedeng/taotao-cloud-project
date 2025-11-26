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
import lombok.Data;
import lombok.experimental.*;

/** 流程评论 */
@Data
@TableName("flow_comment")
public class FlowCommentEntity extends SuperEntity<FlowCommentEntity, String> {

    /** 主键 */
    @TableId("id")
    private String id;

    /** 任务主键 */
    @TableField("task_id")
    private String taskId;

    /** 任务主键 */
    @TableField("text")
    private String text;

    /** 任务主键 */
    @TableField("image")
    private String image;

    /** 任务主键 */
    @TableField("file")
    @JSONField(name = "file")
    private String fileName;

    /** 有效标志 */
    @TableField("enabled_mark")
    private Integer enabledMark;

    /** 创建时间 */
    @TableField(value = "creator_time", fill = FieldFill.INSERT)
    private Date creatorTime;

    /** 创建用户 */
    @TableField(value = "creator_user_id", fill = FieldFill.INSERT)
    private Long creatorUserId;

    /** 修改时间 */
    @TableField(value = "lastmodify_time", fill = FieldFill.UPDATE)
    private Date lastModifyTime;

    /** 修改用户 */
    @TableField(value = "lastmodify_user_id", fill = FieldFill.UPDATE)
    private String lastModifyUserId;

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

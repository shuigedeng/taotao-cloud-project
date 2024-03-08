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
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@TableName("base_message_template")
public class MessageTemplateEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 自然主键 */
    @TableId(value = "F_ID")
    private String id;

    /** 分类（数据字典） */
    @TableField(value = "F_CATEGORY")
    private String category;

    /** 模板名称 */
    @TableField(value = "F_FULLNAME")
    private String fullName;

    /** 标题 */
    @TableField(value = "F_TITLE")
    private String title;

    /** 是否站内信 */
    @TableField(value = "F_ISSTATIONLETTER")
    private Integer isStationLetter;

    /** 是否邮箱 */
    @TableField(value = "F_ISEMAIL")
    private Integer isEmail;

    /** 是否企业微信 */
    @TableField(value = "F_ISWECOM")
    private Integer isWecom;

    /** 是否钉钉 */
    @TableField(value = "F_ISDINGTALK")
    private Integer isDingTalk;

    /** 是否短信 */
    @TableField(value = "F_ISSMS")
    private Integer isSms;

    /** 短信模板ID */
    @TableField(value = "F_SMSID")
    private String smsId;

    /** 模板参数JSON */
    @TableField(value = "F_TEMPLATEJSON")
    private String templateJson;

    /** 内容 */
    @TableField(value = "F_CONTENT")
    private String content;

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
    @TableField("F_LASTMODIFYTIME")
    private Date lastModifyTime;

    /** 修改用户 */
    @TableField(value = "F_LASTMODIFYUSERID", fill = FieldFill.UPDATE)
    private String lastModifyUserId;

    /** 删除标志 */
    @TableField("F_DELETEMARK")
    private Integer deleteMark;

    /** 删除时间 */
    @TableField("F_DELETETIME")
    private Date deleteTime;

    /** 删除用户 */
    @TableField("F_DELETEUSERID")
    private String deleteUserId;

    /** 编码 */
    @TableField("F_ENCODE")
    private String enCode;
}

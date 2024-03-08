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

@TableName(value = "base_sms_template")
@Data
public class SmsTemplateEntity implements Serializable {

    /** 自然主键 */
    @TableId(value = "F_ID")
    private String id;

    /** 短信提供商 */
    @TableField(value = "F_COMPANY")
    private Integer company;

    /** 应用编号 */
    @TableField(value = "F_APPID")
    private String appId;

    /** 签名内容 */
    @TableField(value = "F_SIGNCONTENT")
    private String signContent;

    /** 模板编号 */
    @TableField(value = "F_TEMPLATEID")
    private String templateId;

    /** 模板名称 */
    @TableField(value = "F_FULLNAME")
    private String fullName;

    /** 模板参数JSON */
    @TableField(value = "F_TEMPLATEJSON")
    private String templateJson;

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

    /** endpoint */
    @TableField("F_ENDPOINT")
    private String endpoint;

    /** 地域参数 */
    @TableField("F_REGION")
    private String region;
}

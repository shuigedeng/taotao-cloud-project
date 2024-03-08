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
@TableName("ext_emailconfig")
public class EmailConfigEntity {

    /** 邮件账户主键 */
    @TableId("F_ID")
    private String id;

    /** POP3服务 */
    @TableField("F_POP3HOST")
    private String pop3Host;

    /** POP3端口 */
    @TableField("F_POP3PORT")
    private Integer pop3Port;

    /** SMTP服务 */
    @TableField("F_SMTPHOST")
    private String smtpHost;

    /** SMTP端口 */
    @TableField("F_SMTPPORT")
    private Integer smtpPort;

    /** 账户 */
    @TableField("F_ACCOUNT")
    private String account;

    /** 密码 */
    @TableField("F_PASSWORD")
    private String password;

    /** SSL登录 */
    @TableField("F_SSL")
    private Integer emailSsl = 0;

    /** 发件人名称 */
    @TableField("F_SENDERNAME")
    private String senderName;

    /** 我的文件夹 */
    @TableField("F_FOLDERJSON")
    private String folderJson;

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
}

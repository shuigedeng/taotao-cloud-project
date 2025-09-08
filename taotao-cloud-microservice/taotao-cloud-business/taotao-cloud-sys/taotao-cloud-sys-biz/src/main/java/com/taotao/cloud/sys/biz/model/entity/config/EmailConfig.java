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

package com.taotao.cloud.sys.biz.model.entity.config;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.boot.webagg.entity.BaseSuperEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 邮件配置表
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 21:10:22
 */
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain=true)
@Getter
@Setter
@Entity
@Table(name = EmailConfig.TABLE_NAME)
@TableName(EmailConfig.TABLE_NAME)
@org.springframework.data.relational.core.mapping.Table(name = EmailConfig.TABLE_NAME)
public class EmailConfig extends BaseSuperEntity<EmailConfig, Long> {

    public static final String TABLE_NAME = "tt_email_config";

    /** 收件人 */
    @Column(name = "from_user", columnDefinition = "varchar(64) not null comment '收件人'")
    private String fromUser;

    /** 邮件服务器SMTP地址 */
    @Column(name = "host", columnDefinition = "varchar(64) not null comment '邮件服务器SMTP地址'")
    private String host;

    /** 密码 */
    @Column(name = "pass", columnDefinition = "varchar(64) not null comment '密码'")
    private String pass;

    /** 端口 */
    @Column(name = "port", columnDefinition = "varchar(64) not null comment '端口'")
    private String port;

    /** 发件者用户名 */
    @Column(name = "user", columnDefinition = "varchar(64) not null comment '发件者用户名'")
    private String user;
}

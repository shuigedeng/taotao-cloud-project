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

package com.taotao.cloud.member.infrastructure.persistent.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 会员公司表
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 21:11:38
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@TableName(MemberCompany.TABLE_NAME)
@Table(name = MemberCompany.TABLE_NAME)
// @org.hibernate.annotations.Table(appliesTo = MemberCompany.TABLE_NAME, comment = "会员公司表")
public class MemberCompany extends BaseSuperEntity<MemberCompany, Long> {

    public static final String TABLE_NAME = "tt_member_company";

    /** 租户id */
    @Column(name = "tenant_id", unique = true, columnDefinition = "bigint NOT NULL COMMENT '租户id'")
    private Long tenantId;

    /** 租户密钥 */
    @Column(name = "tenant_secret", columnDefinition = "varchar(255)  NOT NULL COMMENT '租户密钥'")
    private String tenantSecret;

    /** 公司名称 */
    @Column(name = "name", columnDefinition = "varchar(255) not null comment '公司名称'")
    private String name;

    /** 企业全称 */
    @Column(name = "full_name", columnDefinition = "varchar(255) not null comment '企业全称'")
    private String fullName;

    /** 信用代码 */
    @Pattern(regexp = "^|[a-zA-Z0-9]{18}$", message = "信用代码格式错误")
    @Column(name = "credit_code", columnDefinition = "varchar(18) not null comment '信用代码'")
    private String creditCode;

    /** 邮箱 */
    @Column(name = "email", unique = true, columnDefinition = "varchar(255) comment '邮箱'")
    private String email;

    /** 联系人 */
    @Column(name = "username", unique = true, columnDefinition = "varchar(255) not null comment '联系人'")
    private String username;

    /** 联系人手机号 */
    @Column(name = "phone", unique = true, columnDefinition = "varchar(255) not null comment '联系人手机号'")
    private String phone;

    /** 联系人地址 */
    @Column(name = "address", columnDefinition = "varchar(255) not null comment '联系人地址'")
    private String address;

    /** 请求域名 */
    @Column(name = "domain", columnDefinition = "varchar(32) comment '请求域名'")
    private String domain;

    /** 公司网址 */
    @Column(name = "webSite", columnDefinition = "varchar(32) comment '公司网址'")
    private String webSite;

    /** 所在地区 */
    @Column(name = "region_info", columnDefinition = "varchar(255) comment '所在地区'")
    private String regionInfo;

    /** 公司类型 */
    @Column(name = "type", columnDefinition = "int not null default 1 comment '公司类型 1运营商 2供应商'")
    private Integer type;
}

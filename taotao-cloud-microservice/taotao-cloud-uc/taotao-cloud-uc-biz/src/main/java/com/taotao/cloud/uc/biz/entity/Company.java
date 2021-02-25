package com.taotao.cloud.uc.biz.entity;

import com.taotao.cloud.data.jpa.entity.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;

/**
 * 公司表
 *
 * @author dengtao
 * @date 2020/10/16 13:43
 * @since v1.0
 */
@Data
@SuperBuilder
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tt_company")
@org.hibernate.annotations.Table(appliesTo = "tt_company", comment = "公司表")
public class Company extends BaseEntity {

    private static final long serialVersionUID = -8206387430513096669L;

    /**
     * 租户id
     */
    @Column(name = "tenant_id", nullable = false, unique = true, columnDefinition = "varchar(32) NOT NULL COMMENT '租户id'")
    private String tenantId;

    /**
     * 租户密钥
     */
    @Column(name = "tenant_secret", nullable = false, columnDefinition = "varchar(256)  NOT NULL COMMENT '租户密钥'")
    private String tenantSecret;

    /**
     * 公司名称
     */
    @Column(name = "name", nullable = false, columnDefinition = "varchar(32) not null comment '公司名称'")
    private String name;

    /**
     * 企业全称
     */
    @Column(name = "full_name", nullable = false, columnDefinition = "varchar(64) not null comment '企业全称'")
    private String fullName;

    /**
     * 信用代码
     */
    @Pattern(regexp = "^|[a-zA-Z0-9]{18}$", message = "信用代码格式错误")
    @Column(name = "credit_code", nullable = false, columnDefinition = "varchar(18) not null comment '信用代码'")
    private String creditCode;

    /**
     * 邮箱
     */
    @Column(name = "email", unique = true, columnDefinition = "varchar(30) comment '邮箱'")
    private String email;

    /**
     * 联系人
     */
    @Column(name = "username", unique = true, nullable = false, columnDefinition = "varchar(32) not null comment '联系人'")
    private String username;

    /**
     * 联系人手机号
     */
    @Column(name = "phone", unique = true, nullable = false, columnDefinition = "varchar(14) not null comment '联系人手机号'")
    private String phone;

    /**
     * 联系人地址
     */
    @Column(name = "address", nullable = false, columnDefinition = "varchar(32) not null comment '联系人地址'")
    private String address;

    /**
     * 请求域名
     */
    @Column(name = "domain", columnDefinition = "varchar(32) comment '请求域名'")
    private String domain;

    /**
     * 公司网址
     */
    @Column(name = "webSite", columnDefinition = "varchar(32) comment '公司网址'")
    private String webSite;

    /**
     * 所在地区
     */
    @Column(name = "region_info", columnDefinition = "varchar(32) comment '所在地区'")
    private String regionInfo;

    /**
     * 公司类型
     */
    @Builder.Default
    @Column(name = "type", nullable = false, columnDefinition = "tinyint(1) unsigned not null default 1 comment '公司类型 1运营商 2供应商'")
    private byte type = 1;


}

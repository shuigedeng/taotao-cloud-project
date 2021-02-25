package com.taotao.cloud.uc.biz.entity;

import com.taotao.cloud.common.enums.UserSexTypeEnum;
import com.taotao.cloud.common.enums.UserTypeEnum;
import com.taotao.cloud.data.jpa.entity.BaseEntity;
import com.taotao.cloud.uc.api.vo.user.UserVO;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 后台管理用户表
 *
 * @author dengtao
 * @date 2020/6/15 11:00
 */
@Data
@SuperBuilder
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tt_sys_user")
@org.hibernate.annotations.Table(appliesTo = "tt_sys_user", comment = "后台管理用户表")
public class SysUser extends BaseEntity {

    /**
     * 昵称
     */
    @Column(name = "nickname", nullable = false, columnDefinition = "varchar(255) not null comment '昵称'")
    private String nickname;

    /**
     * 真实用户名
     */
    @Column(name = "username", nullable = false, columnDefinition = "varchar(255) not null comment '真实用户名'")
    private String username;

    /**
     * 手机号
     */
    @Column(name = "phone", unique = true, nullable = false, columnDefinition = "varchar(11) not null comment '手机号'")
    private String phone;

    /**
     * 密码
     */
    @Column(name = "password", nullable = false, columnDefinition = "varchar(255) not null comment '密码'")
    private String password;

    @Builder.Default
    @Column(name = "type", nullable = false, columnDefinition = "int not null default 3 comment '用户类型 1前端用户 2商户用户 3后台管理用户'")
    private int type = 3;

    /**
     * @see UserSexTypeEnum
     */
    @Column(name = "sex", nullable = false, columnDefinition = "int not null default 0 comment '性别 1男 2女 0未知'")
    @Builder.Default
    private int sex = 0;

    /**
     * 邮箱
     */
    @Column(name = "email", columnDefinition = "varchar(50) not null comment '邮箱'")
    private String email;

    /**
     * 部门ID
     */
    @Column(name = "dept_id", columnDefinition = "bigint comment '部门ID'")
    private Long deptId;

    /**
     * 岗位ID
     */
    @Column(name = "job_id", columnDefinition = "bigint comment '岗位ID'")
    private Long jobId;

    /**
     * 头像
     */
    @Column(name = "avatar", columnDefinition = "varchar(255) comment '头像'")
    private String avatar;

    /**
     * 是否锁定 0-正常，1-锁定o
     */
    @Builder.Default
    @Column(name = "is_lock", nullable = false, columnDefinition = "tinyint(1) NOT NULL DEFAULT 0 comment '是否锁定 0-正常，1-锁定'")
    private Boolean isLock = false;

    /**
     * 租户id
     */
    @Column(name = "tenant_id", unique = true, columnDefinition = "varchar(32) COMMENT '租户id'")
    private String tenantId;
}

package com.taotao.cloud.uc.biz.entity;

import com.taotao.cloud.data.jpa.entity.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 角色表
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
@Table(name = "tt_sys_role")
@org.hibernate.annotations.Table(appliesTo = "tt_sys_role", comment = "角色表")
public class SysRole extends BaseEntity {

    /**
     * 角色名称
     */
    @Column(name = "name", nullable = false, columnDefinition = "varchar(32) not null comment '角色名称'")
    private String name;

    /**
     * 角色标识
     */
    @Column(name = "code", unique = true, nullable = false, columnDefinition = "varchar(32) not null comment '角色标识'")
    private String code;

    /**
     * 备注
     */
    @Column(name = "remark", columnDefinition = "varchar(255) comment '备注'")
    private String remark;

    /**
     * 租户id
     */
    @Column(name = "tenant_id", unique = true, columnDefinition = "varchar(32) COMMENT '租户id'")
    private String tenantId;
}

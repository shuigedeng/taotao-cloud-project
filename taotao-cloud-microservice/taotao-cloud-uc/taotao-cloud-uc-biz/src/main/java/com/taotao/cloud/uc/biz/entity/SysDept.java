package com.taotao.cloud.uc.biz.entity;

import com.taotao.cloud.data.jpa.entity.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 部门表
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
@Table(name = "tt_sys_dept")
@org.hibernate.annotations.Table(appliesTo = "tt_sys_dept", comment = "后台部门表")
public class SysDept extends BaseEntity {

    /**
     * 部门名称
     */
    @Column(name = "name", nullable = false, columnDefinition = "varchar(32) not null comment '部门名称'")
    private String name;

    /**
     * 上级部门id
     */
    @Builder.Default
    @Column(name = "parent_id", columnDefinition = "int not null default 0 comment '上级部门id'")
    private Long parentId = 0L;

    /**
     * 备注
     */
    @Column(name = "remark", columnDefinition = "varchar(255) comment '备注'")
    private String remark;

    /**
     * 排序值
     */
    @Builder.Default
    @Column(name = "sort_num", columnDefinition = "int not null default 0 comment '排序值'")
    private Integer sortNum = 0;

    /**
     * 租户id
     */
    @Column(name = "tenant_id", unique = true, columnDefinition = "varchar(32) COMMENT '租户id'")
    private String tenantId;
}

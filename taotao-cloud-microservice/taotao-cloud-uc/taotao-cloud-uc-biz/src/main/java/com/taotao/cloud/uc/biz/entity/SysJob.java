package com.taotao.cloud.uc.biz.entity;

import com.taotao.cloud.data.jpa.entity.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 岗位表
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
@Table(name = "tt_sys_job")
@org.hibernate.annotations.Table(appliesTo = "tt_sys_job", comment = "岗位表")
public class SysJob extends BaseEntity {

    /**
     * 岗位名称
     */
    @Column(name = "name", nullable = false, columnDefinition = "varchar(32) not null comment '岗位名称'")
    private String name;

    /**
     * 部门id
     */
    @Column(name = "dept_id", columnDefinition = "bigint not null comment '部门id'")
    private Long deptId;

    /**
     * 备注
     */
    @Column(name = "remark", columnDefinition = "varchar(255) comment '备注'")
    private String remark;

    /**
     * 排序值
     */
    @Builder.Default
    @Column(name = "sort_num", columnDefinition = "int(11) not null default 0 comment '排序值'")
    private Integer sortNum = 0;

    /**
     * 租户id
     */
    @Column(name = "tenant_id", unique = true, columnDefinition = "varchar(32) COMMENT '租户id'")
    private String tenantId;
}

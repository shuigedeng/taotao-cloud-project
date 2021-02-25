package com.taotao.cloud.uc.biz.entity;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * 角色-部门第三方表
 *
 * @author dengtao
 * @date 2020/6/15 11:00
 */
@Data
@Builder
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tt_sys_role_dept")
@org.hibernate.annotations.Table(appliesTo = "tt_sys_role_dept", comment = "角色-部门第三方表")
public class SysRoleDept {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint not null comment 'id'")
    private Long id;

    /**
     * 角色ID
     */
    @Column(name = "role_id", nullable = false, columnDefinition = "bigint not null comment '角色ID'")
    private Long roleId;

    /**
     * 部门ID
     */
    @Column(name = "dept_id", nullable = false, columnDefinition = "bigint not null comment '部门ID'")
    private Long deptId;

}

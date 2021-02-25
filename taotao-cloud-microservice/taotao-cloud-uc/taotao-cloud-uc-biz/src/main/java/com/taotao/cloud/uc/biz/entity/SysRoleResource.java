package com.taotao.cloud.uc.biz.entity;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * 角色-资源第三方表
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
@Table(name = "tt_sys_role_resource")
@org.hibernate.annotations.Table(appliesTo = "tt_sys_role_resource", comment = "角色-资源第三方表")
public class SysRoleResource {

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
     * 资源ID
     */
    @Column(name = "resource_id", nullable = false, columnDefinition = "bigint not null comment '资源ID'")
    private Long resourceId;

}

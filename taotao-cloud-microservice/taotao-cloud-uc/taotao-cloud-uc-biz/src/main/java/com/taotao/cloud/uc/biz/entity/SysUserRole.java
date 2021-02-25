package com.taotao.cloud.uc.biz.entity;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * 用户-角色第三方表
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
@Table(name = "tt_sys_user_role")
@org.hibernate.annotations.Table(appliesTo = "tt_sys_user_role", comment = "用户-角色第三方表")
public class SysUserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint not null comment 'id'")
    private Long id;

    /**
     * 用户ID
     */
    @Column(name = "user_id", nullable = false, columnDefinition = "bigint not null comment '用户ID'")
    private Long userId;

    /**
     * 角色ID
     */
    @Column(name = "role_id", nullable = false, columnDefinition = "bigint not null comment '角色ID'")
    private Long roleId;

}

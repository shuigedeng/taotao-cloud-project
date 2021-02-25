package com.taotao.cloud.member.biz.entity;

import com.taotao.cloud.data.jpa.entity.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 第三方登录信息
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
@Table(name = "tt_member_platform")
@org.hibernate.annotations.Table(appliesTo = "tt_member_platform", comment = "第三方登录信息")
public class MemberPlatform extends BaseEntity {

    /**
     * 会员id
     *
     * @see Member
     */
    @Column(name = "member_user_id", nullable = false, columnDefinition = "int(11) not null comment '会员id'")
    private Long memberUserId;

    /**
     * 平台id
     */
    @Column(name = "platform_id", nullable = false, columnDefinition = "varchar(255) not null default '' comment '平台id'")
    @Builder.Default
    private String platformId = "";

    /**
     * 平台类型
     *
     * @see PlatformTypeEnum
     */
    @Builder.Default
    @Column(name = "type", nullable = false, columnDefinition = "tinyint(1) NOT NULL DEFAULT 0 COMMENT '平台类型 0:未知,1:facebook,2:google,3:wechat,4:qq,5:weibo,6:twitter'")
    private byte type = 0;

    /**
     * 昵称
     */
    @Column(name = "nickname", unique = true, nullable = false, columnDefinition = "varchar(255) not null comment '昵称'")
    private String nickname;

    /**
     * 头像
     */
    @Builder.Default
    @Column(name = "avatar", columnDefinition = "varchar(255) NOT NULL DEFAULT '' comment '头像'")
    private String avatar = "";
}

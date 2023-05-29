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

package com.taotao.cloud.member.biz.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 联合登陆表
 *
 * @author shuigedeng
 * @since 2020/6/15 11:00
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = MemberConnect.TABLE_NAME)
@TableName(MemberConnect.TABLE_NAME)
// @org.hibernate.annotations.Table(appliesTo = MemberConnect.TABLE_NAME, comment = "联合登陆表表")
public class MemberConnect extends BaseSuperEntity<MemberConnect, Long> {

    public static final String TABLE_NAME = "tt_member_connect";

    @Column(name = "user_id", columnDefinition = "varchar(32) not null comment '用户id'")
    private Long userId;

    @Column(name = "union_id", columnDefinition = "varchar(32) not null comment '联合登录id'")
    private String unionId;

    @Column(name = "union_type", columnDefinition = "varchar(32) not null comment '联合登录类型'")
    private String unionType;

    /** 会员id */
    @Column(name = "member_id", columnDefinition = "bigint not null comment '会员id'")
    private Long memberId;

    /** 平台id */
    @Column(name = "platform_id", columnDefinition = "varchar(255) not null default '' comment '平台id'")
    private String platformId;

    /**
     * 平台类型
     *
     * @see PlatformTypeEnum
     */
    @Column(
            name = "type",
            columnDefinition = "int NOT NULL DEFAULT 0 COMMENT '平台类型"
                    + " 0:未知,1:facebook,2:google,3:wechat,4:qq,5:weibo,6:twitter'")
    private Integer type;

    /** 昵称 */
    @Column(name = "nickname", unique = true, columnDefinition = "varchar(255) not null comment '昵称'")
    private String nickname;

    /** 头像 */
    @Column(name = "avatar", columnDefinition = "varchar(255) NOT NULL DEFAULT '' comment '头像'")
    private String avatar;
}

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

package com.taotao.cloud.message.biz.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.message.api.enums.MessageStatusEnum;
import com.taotao.boot.webagg.entity.BaseSuperEntity;
import com.taotao.boot.webagg.entity.JpaEntityListener;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/** 会员消息表 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = MemberMessage.TABLE_NAME)
@TableName(MemberMessage.TABLE_NAME)
@EntityListeners({JpaEntityListener.class})
@org.springframework.data.relational.core.mapping.Table(name = MemberMessage.TABLE_NAME, comment = "会员消息表")
public class MemberMessage extends BaseSuperEntity<MemberMessage, Long> {

    public static final String TABLE_NAME = "tt_member_message";
    /** 会员id */
    @Column(name = "member_id", columnDefinition = "varchar(255) not null default '' comment '会员id'")
    private String memberId;

    /** 会员名称 */
    @Column(name = "member_name", columnDefinition = "varchar(255) not null default '' comment '会员名称'")
    private String memberName;

    /** 消息标题 */
    @Column(name = "title", columnDefinition = "varchar(255) not null default '' comment '消息标题'")
    private String title;

    /** 消息内容 */
    @Column(name = "content", columnDefinition = "varchar(255) not null default '' comment '消息内容'")
    private String content;

    /** 关联消息id */
    @Column(name = "message_id", columnDefinition = "varchar(255) not null default '' comment '关联消息id'")
    private String messageId;

    /**
     * 状态
     *
     * @see MessageStatusEnum
     */
    @Column(name = "status", columnDefinition = "varchar(255) not null default '' comment '状态'")
    private String status = MessageStatusEnum.UN_READY.name();
}

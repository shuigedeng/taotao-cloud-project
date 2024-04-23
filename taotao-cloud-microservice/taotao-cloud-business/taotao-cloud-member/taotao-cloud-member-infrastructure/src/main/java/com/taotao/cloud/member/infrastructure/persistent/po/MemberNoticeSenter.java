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

package com.taotao.cloud.member.infrastructure.persistent.po;

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
 * 会员消息中心表
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-11 15:24:19
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = MemberNoticeSenter.TABLE_NAME)
@TableName(MemberNoticeSenter.TABLE_NAME)
// @org.hibernate.annotations.Table(appliesTo = MemberNoticeSenter.TABLE_NAME, comment = "会员消息中心表")
public class MemberNoticeSenter extends BaseSuperEntity<MemberNoticeSenter, Long> {

    public static final String TABLE_NAME = "tt_member_notice_senter";

    /** 标题 */
    @Column(name = "title", columnDefinition = "varchar(255) not null comment '标题'")
    private String title;

    /** 消息内容 */
    @Column(name = "content", columnDefinition = "text not null comment '消息内容'")
    private String content;

    /** 会员id */
    @Column(name = "member_ids", columnDefinition = "varchar(1024) not null comment '会员id'")
    private String memberIds;

    /** 发送类型,ALL 全站，SELECT 指定会员 */
    @Column(name = "send_type", columnDefinition = "varchar(32) not null comment '发送类型,ALL 全站，SELECT 指定会员'")
    private String sendType;
}

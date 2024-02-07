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

package com.taotao.cloud.member.infratructure.persistent.notice.po;

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
 * 会员站内信表
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-11 15:18:49
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = MemberNoticePO.TABLE_NAME)
@TableName(MemberNoticePO.TABLE_NAME)
// @org.hibernate.annotations.Table(appliesTo = MemberNotice.TABLE_NAME, comment = "会员站内信表")
public class MemberNoticePO extends BaseSuperEntity<MemberNoticePO, Long> {

    public static final String TABLE_NAME = "tt_member_notice";

    /** 会员id */
    @Column(name = "member_id", columnDefinition = "bigint not null comment '会员id'")
    private Long memberId;

    /** 是否已读 */
    @Column(name = "read", columnDefinition = "boolean not null default false comment '是否已读'")
    private Boolean read;

    /** 阅读时间 */
    @Column(name = "receive_time", columnDefinition = "bigint not null default 0 comment '阅读时间'")
    private Long receiveTime;

    /** 标题 */
    @Column(name = "title", columnDefinition = "varchar(32) not null comment '标题'")
    private String title;

    /** 站内信内容 */
    @Column(name = "content", columnDefinition = "varchar(1024) not null comment '站内信内容'")
    private String content;
}

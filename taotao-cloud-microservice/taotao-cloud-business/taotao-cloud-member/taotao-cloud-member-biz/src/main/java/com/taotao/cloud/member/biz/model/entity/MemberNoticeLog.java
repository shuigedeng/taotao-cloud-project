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
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 会员消息发送日志表
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-11 15:20:59
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = MemberNoticeLog.TABLE_NAME)
@TableName(MemberNoticeLog.TABLE_NAME)
// @org.hibernate.annotations.Table(appliesTo = MemberNoticeLog.TABLE_NAME, comment = "会员消息发送日志表")
public class MemberNoticeLog extends BaseSuperEntity<MemberNoticeLog, Long> {

    public static final String TABLE_NAME = "tt_member_notice_log";

    /** 标题 */
    @Column(name = "title", columnDefinition = "varchar(255) not null comment '标题'")
    private String title;

    /** 消息内容 */
    @Column(name = "content", columnDefinition = "varchar(1024) not null comment '消息内容'")
    private String content;

    /** 会员id 逗号分割 */
    @Column(name = "member_ids", columnDefinition = "text not null comment '会员id 逗号分割'")
    private String memberIds;

    /** 管理员id */
    @Column(name = "admin_id", columnDefinition = "bigint not null comment '管理员id'")
    private Long adminId;

    /** 管理员名称 */
    @Column(name = "admin_name", columnDefinition = "varchar(255) not null comment '管理员名称'")
    private String adminName;

    /** 发送时间 */
    @Column(name = "send_time", columnDefinition = "datetime not null comment '发送时间'")
    private LocalDateTime sendTime;

    /** 发送类型,0全站，1指定会员 */
    @Column(name = "send_type", columnDefinition = "int not null default 0 comment '发送类型,0全站，1指定会员'")
    private Integer sendType;
}

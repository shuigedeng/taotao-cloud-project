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
import com.taotao.cloud.common.enums.SwitchEnum;
import com.taotao.cloud.message.api.enums.NoticeMessageParameterEnum;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import com.taotao.cloud.web.base.entity.JpaEntityListener;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/** 通知类消息模板表 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = NoticeMessage.TABLE_NAME)
@TableName(NoticeMessage.TABLE_NAME)
@EntityListeners({JpaEntityListener.class})
// @org.hibernate.annotations.Table(appliesTo = NoticeMessage.TABLE_NAME, comment = "通知类消息模板表")
public class NoticeMessage extends BaseSuperEntity<NoticeMessage, Long> {

    public static final String TABLE_NAME = "tt_notice_message";

    /** 站内信节点 */
    @Column(name = "notice_node", columnDefinition = "varchar(255) not null default '' comment '站内信节点'")
    private String noticeNode;

    /** 站内信标题 */
    @Column(name = "notice_title", columnDefinition = "varchar(255) not null default '' comment '站内信标题'")
    private String noticeTitle;

    /** 站内信内容 */
    @Column(name = "notice_content", columnDefinition = "varchar(255) not null default '' comment '站内信内容'")
    private String noticeContent;

    /**
     * 站内信是否开启
     *
     * @see SwitchEnum
     */
    @Column(name = "notice_status", columnDefinition = "varchar(255) not null default '' comment '站内信是否开启'")
    private String noticeStatus;

    /**
     * 消息变量
     *
     * @see NoticeMessageParameterEnum
     */
    @Column(name = "variable", columnDefinition = "varchar(255) not null default '' comment '字典名称'")
    private String variable;
}

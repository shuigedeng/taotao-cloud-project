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

package com.taotao.cloud.message.biz.austin.support.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Accessors(chain = true)
/**
 * 消息模板DO
 *
 * @author 3y
 */
public class MessageTemplate implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 模板标题 */
    private String name;

    /** 审核状态 */
    private Integer auditStatus;

    /** 工单ID（审核模板走工单） */
    private String flowId;

    /** 消息状态 */
    private Integer msgStatus;

    /** 定时任务Id(由xxl-job返回) */
    private Integer cronTaskId;

    /** 定时发送的人群的文件路径 */
    private String cronCrowdPath;

    /** 发送的Id类型 */
    private Integer idType;

    /** 发送渠道 */
    private Integer sendChannel;

    /** 模板类型 */
    private Integer templateType;

    /** 屏蔽类型 */
    private Integer shieldType;

    /** 消息类型 */
    private Integer msgType;

    /** 推送消息的时间 0：立即发送 else：crontab 表达式 */
    private String expectPushTime;

    /** 消息内容 {$var} 为占位符 */
    private String msgContent;

    /** 发送账号（邮件下可有多个发送账号、短信可有多个发送账号..） */
    private Integer sendAccount;

    /** 创建者 */
    private String creator;

    /** 修改者 */
    private String updator;

    /** 审核者 */
    private String auditor;

    /** 业务方团队 */
    private String team;

    /** 业务方 */
    private String proposer;

    /** 是否删除 0：未删除 1：已删除 */
    private Integer isDeleted;

    /** 创建时间 单位 s */
    private Integer created;

    /** 更新时间 单位s */
    private Integer updated;
}

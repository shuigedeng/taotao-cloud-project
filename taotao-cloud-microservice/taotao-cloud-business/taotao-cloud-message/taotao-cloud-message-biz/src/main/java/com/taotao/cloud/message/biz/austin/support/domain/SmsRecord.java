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

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
/**
 * 短信（回执和发送记录）
 *
 * @author 3y
 */
public class SmsRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 消息模板Id */
    private Long messageTemplateId;

    /** 手机号 */
    private Long phone;

    /** 渠道商Id */
    private Integer supplierId;

    /** 渠道商名字 */
    private String supplierName;

    /** 短信发送的内容 */
    private String msgContent;

    /** 批次号Id */
    private String seriesId;

    /** 计费条数 */
    private Integer chargingNum;

    /** 回执信息 */
    private String reportContent;

    /** 短信状态 */
    private Integer status;

    /** 发送日期 */
    private Integer sendDate;

    /** 创建时间 */
    private Integer created;

    /** 更新时间 */
    private Integer updated;
}

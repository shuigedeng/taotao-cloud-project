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

package com.taotao.cloud.sys.biz.model.entity.sms;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

/** 短信任务 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = SmsReach.TABLE_NAME)
@TableName(SmsReach.TABLE_NAME)
// @org.hibernate.annotations.Table(appliesTo = SmsReach.TABLE_NAME, comment = "短信任务表")
public class SmsReach extends BaseSuperEntity<SmsReach, Long> {

    public static final String TABLE_NAME = "tt_tt_sms_reach";

    @Column(name = "sign_name", columnDefinition = "varchar(2000) not null comment '签名名称'")
    private String signName;

    @Column(name = "sms_name", columnDefinition = "varchar(2000) not null comment '模板名称'")
    private String smsName;

    @Column(name = "message_code", columnDefinition = "varchar(2000) not null comment '消息CODE'")
    private String messageCode;

    @Column(name = "context", columnDefinition = "varchar(2000) not null comment '消息内容'")
    private String context;

    @Column(name = "sms_range", columnDefinition = "varchar(2000) not null comment '接收人 1:全部会员，2：选择会员 '")
    private String smsRange;

    @Column(name = "num", columnDefinition = "varchar(2000) not null comment '预计发送条数'")
    private String num;

    @Builder
    public SmsReach(
            Long id,
            LocalDateTime createTime,
            Long createBy,
            LocalDateTime updateTime,
            Long updateBy,
            Integer version,
            Boolean delFlag,
            String signName,
            String smsName,
            String messageCode,
            String context,
            String smsRange,
            String num) {
        super(id, createTime, createBy, updateTime, updateBy, version, delFlag);
        this.signName = signName;
        this.smsName = smsName;
        this.messageCode = messageCode;
        this.context = context;
        this.smsRange = smsRange;
        this.num = num;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        SmsReach smsReach = (SmsReach) o;
        return getId() != null && Objects.equals(getId(), smsReach.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

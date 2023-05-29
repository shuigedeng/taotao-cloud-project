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

/** 短信签名 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = SmsSign.TABLE_NAME)
@TableName(SmsSign.TABLE_NAME)
// @org.hibernate.annotations.Table(appliesTo = SmsSign.TABLE_NAME, comment = "短信签名表")
public class SmsSign extends BaseSuperEntity<SmsSign, Long> {

    public static final String TABLE_NAME = "tt_tt_sms_sign";

    @Column(name = "sign_name", columnDefinition = "varchar(2000) not null comment '签名名称'")
    private String signName;

    @Column(name = "sign_source", columnDefinition = "varchar(2000) not null comment '签名来源'")
    private Integer signSource;

    @Column(name = "remark", columnDefinition = "varchar(2000) not null comment '短信签名申请说明'")
    private String remark;

    @Column(name = "business_license", columnDefinition = "varchar(2000) not null comment '营业执照'")
    private String businessLicense;

    @Column(name = "license", columnDefinition = "varchar(2000) not null comment '授权委托书'")
    private String license;

    @Column(
            name = "sign_status",
            columnDefinition = "int not null default 0 comment '签名审核状态  0：审核中。"
                    + "     * 1：审核通过。"
                    + "     * 2：审核失败，请在返回参数Reason中查看审核失败原因。'")
    private Integer signStatus;

    @Column(name = "reason", columnDefinition = "varchar(2000) not null comment '审核备注'")
    private String reason;

    @Builder
    public SmsSign(
            Long id,
            LocalDateTime createTime,
            Long createBy,
            LocalDateTime updateTime,
            Long updateBy,
            Integer version,
            Boolean delFlag,
            String signName,
            Integer signSource,
            String remark,
            String businessLicense,
            String license,
            Integer signStatus,
            String reason) {
        super(id, createTime, createBy, updateTime, updateBy, version, delFlag);
        this.signName = signName;
        this.signSource = signSource;
        this.remark = remark;
        this.businessLicense = businessLicense;
        this.license = license;
        this.signStatus = signStatus;
        this.reason = reason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        SmsSign smsSign = (SmsSign) o;
        return getId() != null && Objects.equals(getId(), smsSign.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

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

package com.taotao.cloud.sys.biz.model.entity.config;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.boot.webagg.entity.BaseSuperEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/** 物流公司设置 */
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain=true)
@Getter
@Setter
@Entity
@Table(name = LogisticsConfig.TABLE_NAME)
@TableName(LogisticsConfig.TABLE_NAME)
@org.springframework.data.relational.core.mapping.Table(name = LogisticsConfig.TABLE_NAME)
public class LogisticsConfig extends BaseSuperEntity<LogisticsConfig, Long> {

    public static final String TABLE_NAME = "tt_logistics_config";

    /** 物流公司名称 */
    @Column(name = "name", columnDefinition = "varchar(255) not null COMMENT '物流公司名称'")
    private String name;

    /** 物流公司code */
    @Column(name = "code", columnDefinition = "varchar(255) not null COMMENT '物流公司code'")
    private String code;

    /** 物流公司联系人 */
    @Column(name = "contact_name", columnDefinition = "varchar(32) not null COMMENT '物流公司联系人'")
    private String contactName;

    /** 物流公司联系电话 */
    @Column(name = "contact_mobile", columnDefinition = "varchar(32) not null COMMENT '物流公司联系电话'")
    private String contactMobile;

    /** 支持电子面单 */
    @Column(name = "stand_by", columnDefinition = "varchar(255) not null COMMENT '支持电子面单'")
    private String standBy;

    /** 物流公司电子面单表单 */
    @Column(name = "form_items", columnDefinition = "varchar(255) not null COMMENT '物流公司电子面单表单'")
    private String formItems;

    /** 禁用状态 OPEN：开启，CLOSE：禁用 */
    @Column(name = "disabled", columnDefinition = "varchar(12) not null COMMENT '禁用状态 OPEN：开启，CLOSE：禁用'")
    private String disabled;
}

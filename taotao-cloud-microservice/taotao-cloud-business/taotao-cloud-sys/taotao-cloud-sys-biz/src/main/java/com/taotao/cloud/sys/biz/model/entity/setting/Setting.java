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

package com.taotao.cloud.sys.biz.model.entity.setting;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
import org.hibernate.annotations.Type;

/**
 * 配置表
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-21 21:54:40
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = Setting.TABLE_NAME)
@TableName(value = Setting.TABLE_NAME, autoResultMap = true)
// @org.hibernate.annotations.Table(appliesTo = Setting.TABLE_NAME, comment = "配置表")
public class Setting extends BaseSuperEntity<Setting, Long> {

    public static final String TABLE_NAME = "tt_setting";

    @Column(name = "name", columnDefinition = "varchar(255) not null comment '名称'")
    private String name;

    @Column(name = "category", columnDefinition = "varchar(255) not null comment '分类'")
    private String category;

    @Column(name = "en_code", unique = true, columnDefinition = "varchar(255) not null comment '编码'")
    private String enCode;

    @Type(value = JsonType.class)
    @TableField(typeHandler = JacksonTypeHandler.class)
    @Column(name = "value", columnDefinition = "json not null comment 'json数据'")
    private String value;

    @Override
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public void setId(Long id) {
        super.setId(id);
    }

    @Builder
    public Setting(
            Long id,
            LocalDateTime createTime,
            Long createBy,
            LocalDateTime updateTime,
            Long updateBy,
            Integer version,
            Boolean delFlag,
            String name,
            String category,
            String enCode,
            String value) {
        super(id, createTime, createBy, updateTime, updateBy, version, delFlag);
        this.name = name;
        this.category = category;
        this.enCode = enCode;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Setting setting = (Setting) o;
        return getId() != null && Objects.equals(getId(), setting.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

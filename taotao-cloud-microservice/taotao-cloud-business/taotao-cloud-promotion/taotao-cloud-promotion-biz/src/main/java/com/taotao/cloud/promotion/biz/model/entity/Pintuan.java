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

package com.taotao.cloud.promotion.biz.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

/**
 * 拼团活动实体类
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:24:42
 */
@Getter
@Setter
@ToString(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = Pintuan.TABLE_NAME)
@TableName(Pintuan.TABLE_NAME)
// @org.hibernate.annotations.Table(appliesTo = Pintuan.TABLE_NAME, comment = "拼团活动实体类")
public class Pintuan extends BasePromotions<Pintuan, Long> {

    public static final String TABLE_NAME = "tt_pintuan";
    /** 成团人数 */
    @Column(name = "required_num", columnDefinition = "int not null  comment '成团人数'")
    private Integer requiredNum;
    /** 限购数量 */
    @Column(name = "limit_num", columnDefinition = "int not null  comment '限购数量'")
    private Integer limitNum;
    /** 虚拟成团 */
    @Column(name = "fictitious", columnDefinition = "boolean not null  comment '虚拟成团'")
    private Boolean fictitious;
    /** 拼团规则 */
    @Column(name = "pintuan_rule", columnDefinition = "varchar(255) not null  comment '拼团规则'")
    private String pintuanRule;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Pintuan pintuan = (Pintuan) o;
        return getId() != null && Objects.equals(getId(), pintuan.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

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

package com.taotao.cloud.goods.biz.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

/**
 * 商品品牌表
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:55:08
 */
@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = Brand.TABLE_NAME)
@TableName(Brand.TABLE_NAME)
public class Brand extends BaseSuperEntity<Brand, Long> {

    public static final String TABLE_NAME = "tt_brand";

    /** 品牌名称 */
    @Column(name = "name", columnDefinition = "varchar(255) not null comment '品牌名称'")
    private String name;

    /** 品牌图标 */
    @Column(name = "logo", columnDefinition = "varchar(255) not null comment '品牌图标'")
    private String logo;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Brand brand = (Brand) o;
        return getId() != null && Objects.equals(getId(), brand.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

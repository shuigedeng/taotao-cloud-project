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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

/** 商品计量单位表 */
@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = GoodsUnit.TABLE_NAME)
@TableName(GoodsUnit.TABLE_NAME)
// @org.hibernate.annotations.Table(appliesTo = GoodsUnit.TABLE_NAME, comment = "商品计量单位表")
public class GoodsUnit extends BaseSuperEntity<GoodsUnit, Long> {

    public static final String TABLE_NAME = "tt_goods_unit";

    /** 计量单位名称 */
    @Column(name = "name", columnDefinition = "varchar(255) not null comment '计量单位名称'")
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        GoodsUnit goodsUnit = (GoodsUnit) o;
        return getId() != null && Objects.equals(getId(), goodsUnit.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

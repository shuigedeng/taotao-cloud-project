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

package com.taotao.cloud.goods.infrastructure.persistent.po;

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

/**
 * 商品销售范围表
 *
 * <p>todo 暂时未用
 *
 * @author shuigedeng
 * @since 2020/4/30 16:04
 */
@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = GoodsArea.TABLE_NAME)
@TableName(GoodsArea.TABLE_NAME)
// @org.hibernate.annotations.Table(appliesTo = GoodsArea.TABLE_NAME, comment = "商品销售范围表")
public class GoodsArea extends BaseSuperEntity<GoodsArea, Long> {

    public static final String TABLE_NAME = "tt_goods_area";

    /** 区域json */
    @Column(name = "region_json", columnDefinition = "json not null comment '区域json'")
    private String regionJson;

    /** 商品id */
    @Column(name = "type", columnDefinition = "int not null comment '类型'")
    private Integer type;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        GoodsArea goodsArea = (GoodsArea) o;
        return getId() != null && Objects.equals(getId(), goodsArea.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

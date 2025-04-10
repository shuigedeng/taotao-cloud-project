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
import com.taotao.boot.webagg.entity.BaseSuperEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

/**
 * 积分商品分类
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:24:35
 */
@Getter
@Setter
@ToString(callSuper = true)
@Accessors(chain=true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = PointsGoodsCategory.TABLE_NAME)
@TableName(PointsGoodsCategory.TABLE_NAME)
@org.springframework.data.relational.core.mapping.Table(name = PointsGoodsCategory.TABLE_NAME, comment = "积分商品分类")
public class PointsGoodsCategory extends BaseSuperEntity<PointsGoodsCategory, Long> {

    public static final String TABLE_NAME = "tt_points_goods_category";
    /** 分类名称 */
    @Column(name = "name", columnDefinition = "varchar(255) not null  comment '分类名称'")
    private String name;
    /** 父id, 根节点为0 */
    @Column(name = "parent_id", columnDefinition = "bigint not null  comment '父id, 根节点为0'")
    private Long parentId;
    /** 层级, 从0开始 */
    @Column(name = "level", columnDefinition = "int not null  comment '层级, 从0开始'")
    private Integer level;
    /** 排序值 */
    @Column(name = "sort_order", columnDefinition = "int not null  comment '排序值'")
    private Integer sortOrder;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        PointsGoodsCategory pointsGoodsCategory = (PointsGoodsCategory) o;
        return getId() != null && Objects.equals(getId(), pointsGoodsCategory.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

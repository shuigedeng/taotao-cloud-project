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
import java.math.BigDecimal;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

/**
 * 商品分类表
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-20 16:59:38
 */
@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = Category.TABLE_NAME)
@TableName(Category.TABLE_NAME)
// @org.hibernate.annotations.Table(appliesTo = Category.TABLE_NAME, comment = "商品分类表")
public class Category extends BaseSuperEntity<Category, Long> {

    public static final String TABLE_NAME = "tt_category";

    /** 分类名称 */
    @Column(name = "name", columnDefinition = "varchar(255) not null comment '分类名称'")
    private String name;

    /** 父id, 根节点为0 */
    @Column(name = "parent_id", columnDefinition = "bigint not null comment '父id, 根节点为0'")
    private Long parentId;

    /** 层级, 从0开始 */
    @Column(name = "level", columnDefinition = "int not null default 0 comment '层级, 从0开始'")
    private Integer level;

    /** 排序值 */
    @Column(name = "sort_order", columnDefinition = "int not null default 0 comment '排序值'")
    private Integer sortOrder;

    /** 佣金比例 */
    @Column(name = "commission_rate", columnDefinition = "decimal(10,2) not null comment '佣金比例'")
    private BigDecimal commissionRate;

    /** 分类图标 */
    @Column(name = "image", columnDefinition = "varchar(255) not null comment '分类图标'")
    private String image;

    /** 是否支持频道 */
    @Column(name = "support_channel", columnDefinition = "boolean null default false comment '是否支持频道'")
    private Boolean supportChannel;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Category category = (Category) o;
        return getId() != null && Objects.equals(getId(), category.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

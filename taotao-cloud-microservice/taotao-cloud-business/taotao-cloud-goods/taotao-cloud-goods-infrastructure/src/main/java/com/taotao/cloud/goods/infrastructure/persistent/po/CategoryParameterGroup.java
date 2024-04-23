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

/** 分类绑定参数组表 */
@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = CategoryParameterGroup.TABLE_NAME)
@TableName(CategoryParameterGroup.TABLE_NAME)
// @org.hibernate.annotations.Table(appliesTo = CategoryParameterGroup.TABLE_NAME, comment = "分类绑定参数组表")
public class CategoryParameterGroup extends BaseSuperEntity<CategoryParameterGroup, Long> {

    public static final String TABLE_NAME = "tt_category_parameter_group";

    /** 参数组名称 */
    @Column(name = "group_name", columnDefinition = "varchar(255) not null comment '参数组名称'")
    private String groupName;

    /** 关联分类id */
    @Column(name = "category_id", columnDefinition = "bigint not null comment '关联分类id'")
    private Long categoryId;

    /** 排序 */
    @Column(name = "sort_order", columnDefinition = "int not null default 0 comment '排序'")
    private Integer sortOrder;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        CategoryParameterGroup that = (CategoryParameterGroup) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

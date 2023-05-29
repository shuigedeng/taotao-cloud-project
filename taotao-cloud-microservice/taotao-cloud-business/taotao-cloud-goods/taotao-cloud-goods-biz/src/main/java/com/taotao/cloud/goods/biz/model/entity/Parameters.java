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

/**
 * 商品参数表
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
@Table(name = Parameters.TABLE_NAME)
@TableName(Parameters.TABLE_NAME)
// @org.hibernate.annotations.Table(appliesTo = Parameters.TABLE_NAME, comment = "商品参数表")
public class Parameters extends BaseSuperEntity<Parameters, Long> {

    public static final String TABLE_NAME = "tt_parameters";

    /** 参数名称 */
    @Column(name = "param_name", columnDefinition = "varchar(255) not null comment '参数名称'")
    private String paramName;

    /** 选择值 */
    @Column(name = "options", columnDefinition = "varchar(255) not null comment '选择值'")
    private String options;

    /** 是否可索引，0 不显示 1 显示 */
    @Column(name = "is_index", columnDefinition = "int not null default 1 comment '是否可索引，0 不显示 1 显示'")
    private Integer isIndex;

    /** 是否必填 是1否0 */
    @Column(name = "required", columnDefinition = "int not null comment '是否必填 是1否0'")
    private Integer required;

    /** 参数分组id */
    @Column(name = "group_id", columnDefinition = "bigint not null comment '参数分组id'")
    private Long groupId;

    /** 分类id */
    @Column(name = "category_id", columnDefinition = "bigint not null comment '分类id'")
    private Long categoryId;

    /** 排序 */
    @Column(name = "sort", columnDefinition = "int not null comment '排序'")
    private Integer sort;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Parameters that = (Parameters) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

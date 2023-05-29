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
 * 规格项表规格项表
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-20 16:59:38
 * @since 2020-02-18 15:18:56
 */
@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = Specification.TABLE_NAME)
@TableName(Specification.TABLE_NAME)
// @org.hibernate.annotations.Table(appliesTo = Specification.TABLE_NAME, comment = "规格项表")
public class Specification extends BaseSuperEntity<Specification, Long> {

    public static final String TABLE_NAME = "tt_specification";

    /** 规格名称 */
    @Column(name = "spec_name", columnDefinition = "varchar(255) not null comment '会员规格名称ID'")
    private String specName;

    /**
     * 所属卖家 0属于平台
     *
     * <p>店铺自定义规格暂时废弃 2021-06-23 后续推出新配置方式
     */
    @Column(name = "store_id", columnDefinition = "bigint not null comment '所属卖家'")
    private Long storeId;

    /** 规格值名字, 《,》分割 */
    @Column(name = "spec_value", columnDefinition = "varchar(1024) not null comment '规格值名字'")
    private String specValue;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Specification that = (Specification) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

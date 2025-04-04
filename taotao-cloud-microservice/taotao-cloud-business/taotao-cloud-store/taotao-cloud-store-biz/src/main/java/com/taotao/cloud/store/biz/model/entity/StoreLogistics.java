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

package com.taotao.cloud.store.biz.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.boot.webagg.entity.BaseSuperEntity;
import com.taotao.boot.webagg.entity.JpaEntityListener;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

/** 店铺-物流公司设置 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain=true)
@Entity
@Table(name = StoreLogistics.TABLE_NAME)
@TableName(StoreLogistics.TABLE_NAME)
@EntityListeners({JpaEntityListener.class})
@org.springframework.data.relational.core.mapping.Table(name = StoreLogistics.TABLE_NAME, comment = "店铺-物流公司设置表")
public class StoreLogistics extends BaseSuperEntity<StoreLogistics, String> {

    public static final String TABLE_NAME = "tt_store_logistics";

    @Column(name = "store_id", columnDefinition = "varchar(64) not null comment '店铺ID'")
    private String storeId;

    @Column(name = "logistics_id", columnDefinition = "varchar(64) not null comment '物流公司ID'")
    private String logisticsId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        StoreLogistics dict = (StoreLogistics) o;
        return getId() != null && Objects.equals(getId(), dict.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

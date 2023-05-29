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
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import com.taotao.cloud.web.base.entity.JpaEntityListener;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
 * 店铺自提点
 *
 * @since 2020/12/7 15:09
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = StoreAddress.TABLE_NAME)
@TableName(StoreAddress.TABLE_NAME)
@EntityListeners({JpaEntityListener.class})
// @org.hibernate.annotations.Table(appliesTo = StoreAddress.TABLE_NAME, comment = "店铺自提点表")
public class StoreAddress extends BaseSuperEntity<StoreAddress, String> {

    public static final String TABLE_NAME = "tt_store_address";

    @Column(name = "store_id", columnDefinition = "varchar(64) not null comment '店铺id'")
    private String storeId;

    @Column(name = "address_name", columnDefinition = "varchar(64) not null comment '自提点名称'")
    private String addressName;

    @Column(name = "center", columnDefinition = "varchar(64) not null comment '经纬度'")
    private String center;

    @Column(name = "address", columnDefinition = "varchar(64) not null comment '地址'")
    private String address;

    @Column(name = "mobile", columnDefinition = "varchar(64) not null comment '电话'")
    private String mobile;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        StoreAddress dict = (StoreAddress) o;
        return getId() != null && Objects.equals(getId(), dict.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

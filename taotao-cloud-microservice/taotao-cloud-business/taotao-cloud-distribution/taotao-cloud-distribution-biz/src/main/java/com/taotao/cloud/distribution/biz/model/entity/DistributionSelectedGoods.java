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

package com.taotao.cloud.distribution.biz.model.entity;

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
 * 分销商已选择分销商品表
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 14:59:04
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain=true)
@Entity
@Table(name = DistributionSelectedGoods.TABLE_NAME)
@TableName(DistributionSelectedGoods.TABLE_NAME)
@org.springframework.data.relational.core.mapping.Table(name = DistributionSelectedGoods.TABLE_NAME, comment = "分销商已选择分销商品表")
public class DistributionSelectedGoods extends BaseSuperEntity<DistributionSelectedGoods, Long> {

    public static final String TABLE_NAME = "tt_distribution_selected_goods";

    /** 分销员ID */
    @Column(name = "distribution_id", columnDefinition = "bigint not null  comment '分销员ID'")
    private Long distributionId;

    /** 分销商品ID */
    @Column(name = "distribution_goods_id", columnDefinition = "bigint not null  comment '分销商品ID'")
    private Long distributionGoodsId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        DistributionSelectedGoods distributionSelectedGoods = (DistributionSelectedGoods) o;
        return getId() != null && Objects.equals(getId(), distributionSelectedGoods.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

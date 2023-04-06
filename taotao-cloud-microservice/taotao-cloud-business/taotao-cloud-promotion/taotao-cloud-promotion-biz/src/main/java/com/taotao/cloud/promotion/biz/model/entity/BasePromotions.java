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

import com.taotao.cloud.promotion.api.enums.PromotionsScopeTypeEnum;
import com.taotao.cloud.promotion.api.enums.PromotionsStatusEnum;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import com.taotao.cloud.web.base.entity.SuperEntity;
import jakarta.persistence.Column;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 促销活动基础类
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 15:36:24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BasePromotions<T extends SuperEntity<T, I>, I extends Serializable> extends BaseSuperEntity<T, I> {

    @Serial
    private static final long serialVersionUID = 7814832369110695758L;

    /** 商家名称，如果是平台，这个值为 platform */
    @Column(name = "store_name", columnDefinition = "varchar(255) not null comment '商家名称，如果是平台，这个值为 platform'")
    private String storeName;

    /** 商家id，如果是平台，这个值为 0 */
    @Column(name = "store_id", columnDefinition = "bigint not null comment '商家id，如果是平台，这个值为 0'")
    private Long storeId;

    /** 活动名称 */
    @Column(name = "promotion_name", columnDefinition = "varchar(255) not null comment '活动名称'")
    private String promotionName;

    /** 活动开始时间 */
    @Column(name = "start_time", columnDefinition = "datetime not null comment '活动开始时间'")
    private LocalDateTime startTime;

    /** 活动结束时间 */
    @Column(name = "end_time", columnDefinition = "datetime not null comment '活动结束时间'")
    private LocalDateTime endTime;

    /**
     * @see PromotionsScopeTypeEnum 关联范围类型
     */
    @Column(name = "scope_type", columnDefinition = "varchar(255) not null comment '关联范围类型'")
    private String scopeType = PromotionsScopeTypeEnum.PORTION_GOODS.name();

    /** 范围关联的id */
    @Column(name = "scope_id", columnDefinition = "bigint not null comment '范围关联的id'")
    private Long scopeId;

    /**
     * @return 促销状态
     * @see PromotionsStatusEnum
     */
    public String getPromotionStatus() {
        if (endTime == null) {
            return startTime != null ? PromotionsStatusEnum.START.name() : PromotionsStatusEnum.CLOSE.name();
        }
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(startTime)) {
            return PromotionsStatusEnum.NEW.name();
        } else if (endTime.isBefore(now)) {
            return PromotionsStatusEnum.END.name();
        } else if (now.isBefore(endTime)) {
            return PromotionsStatusEnum.START.name();
        }
        return PromotionsStatusEnum.CLOSE.name();
    }
}

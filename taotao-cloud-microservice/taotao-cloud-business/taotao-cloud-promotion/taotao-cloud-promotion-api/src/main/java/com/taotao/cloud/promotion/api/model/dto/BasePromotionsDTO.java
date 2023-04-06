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

package com.taotao.cloud.promotion.api.model.dto;

import com.taotao.cloud.promotion.api.enums.PromotionsScopeTypeEnum;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/** 促销活动基础类 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BasePromotionsDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 7814832369110695758L;

    private Long id;

    private String storeName;

    private Long storeId;

    private String promotionName;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    /**
     * @see PromotionsScopeTypeEnum PromotionsScopeTypeEnum.PORTION_GOODS.name()
     */
    private String scopeType;

    private Long scopeId;
}

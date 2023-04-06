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

package com.taotao.cloud.promotion.api.model.vo;

import com.taotao.cloud.promotion.api.enums.CouponActivitySendTypeEnum;
import com.taotao.cloud.promotion.api.enums.CouponActivityTypeEnum;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 优惠券活动实体类
 *
 * @since 2020-03-19 10:44 上午
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CouponActivityBaseVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 7814832369110695758L;

    private Long id;

    /**
     * @see CouponActivityTypeEnum
     */
    private String couponActivityType;

    /**
     * @see CouponActivitySendTypeEnum
     */
    private String activityScope;

    private String activityScopeInfo;
}

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

package com.taotao.cloud.promotion.api.model.page;

import com.taotao.boot.common.model.request.PageQuery;
import com.taotao.cloud.promotion.api.enums.PromotionsStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import lombok.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.*;

/** 砍价活动商品查询通用类 */
@Getter
@Setter
@Accessors(chain=true)
@AllArgsConstructor
@NoArgsConstructor
public class KanjiaActivityGoodsPageQuery extends PageQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1344104067705714289L;

    @Schema(description = "活动商品")
    private String goodsName;

    @Schema(description = "活动开始时间")
    private Long startTime;

    @Schema(description = "活动结束时间")
    private Long endTime;

    @Schema(description = "skuId")
    private String skuId;

    /**
     * @see PromotionsStatusEnum
     */
    @Schema(description = "活动状态")
    private String promotionStatus;



}

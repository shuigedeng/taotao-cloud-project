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

package com.taotao.cloud.order.application.command.cart.dto.clientobject;

import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 商品促销VO
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-21 16:59:38
 */
@RecordBuilder
@Schema(description = "商品促销VO 购物车中")
public record GoodsPromotionCO(
        @Schema(description = "活动开始时间") Date startTime,
        @Schema(description = "活动结束时间") Date endTime,
        @Schema(description = "活动id") String promotionId,

        /**
         * @see PromotionTypeEnum
         */
        @Schema(description = "活动工具类型") String promotionType,
        @Schema(description = "活动名称") String title,
        @Schema(description = "限购数量") Integer limitNum)
        implements Serializable {

    @Serial
    private static final long serialVersionUID = 1622051257060817414L;

    // public GoodsPromotionVO(PromotionGoods promotionGoods) {
    // 	this.startTime = promotionGoods.getStartTime();
    // 	this.endTime = promotionGoods.getEndTime();
    // 	this.promotionId = promotionGoods.getPromotionId();
    // 	this.setPromotionType(promotionGoods.getPromotionType());
    // 	this.setLimitNum(promotionGoods.getLimitNum());
    // }

}

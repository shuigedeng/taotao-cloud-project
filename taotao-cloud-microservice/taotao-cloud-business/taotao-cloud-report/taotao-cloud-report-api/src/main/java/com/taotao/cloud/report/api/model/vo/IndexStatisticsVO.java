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

package com.taotao.cloud.report.api.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 首页统计内容 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IndexStatisticsVO {

    @Schema(description = "订单总数量")
    private Long orderNum;

    @Schema(description = "商品总数量")
    private Long goodsNum;

    @Schema(description = "会员总数量")
    private Long memberNum;

    @Schema(description = "店铺总数量")
    private Long storeNum;

    /** 流量概括 */
    @Schema(description = "今日访问数UV")
    private Integer todayUV;

    @Schema(description = "昨日访问数UV")
    private Integer yesterdayUV;

    @Schema(description = "前七日访问数UV")
    private Integer lastSevenUV;

    @Schema(description = "三十日访问数UV")
    private Integer lastThirtyUV;

    /** 今日信息概括 */
    @Schema(description = "今日订单数")
    private Long todayOrderNum;

    @Schema(description = "今日下单金额")
    private BigDecimal todayOrderPrice;

    @Schema(description = "今日新增会员数量")
    private Long todayMemberNum;

    @Schema(description = "今日新增商品数量")
    private Long todayGoodsNum;

    @Schema(description = "今日新增店铺数量")
    private Long todayStoreNum;

    @Schema(description = "今日新增评论数量")
    private Long todayMemberEvaluation;

    @Schema(description = "当前在线人数")
    private Long currentNumberPeopleOnline;
}

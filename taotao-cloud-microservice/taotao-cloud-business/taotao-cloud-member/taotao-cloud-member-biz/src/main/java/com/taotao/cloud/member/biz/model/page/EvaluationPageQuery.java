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

package com.taotao.cloud.member.biz.model.page;

import com.taotao.boot.common.model.request.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import lombok.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 评价查询条件
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-14 11:22:15
 */
@Setter
@Getter
@Accessors(chain=true)
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "评价查询条件")
public class EvaluationPageQuery extends PageQuery {

    @Serial
    private static final long serialVersionUID = -7605952923416404638L;

    @Schema(description = "skuid")
    private Long skuId;

    @Schema(description = "买家ID")
    private Long memberId;

    @Schema(description = "会员名称")
    private String memberName;

    @Schema(description = "卖家名称")
    private String storeName;

    @Schema(description = "卖家ID")
    private Long storeId;

    @Schema(description = "商品名称")
    private String goodsName;

    @Schema(description = "商品ID")
    private Long goodsId;

    @Schema(description = "好中差评 , GOOD：好评，MODERATE：中评，WORSE：差评", allowableValues = "GOOD,MODERATE,WORSE")
    private String grade;

    @Schema(description = "是否有图")
    private String haveImage;

    @Schema(description = "评论日期--开始时间")
    private String startTime;

    @Schema(description = "评论日期--结束时间")
    private String endTime;

    @Schema(description = "状态")
    private String status;
}

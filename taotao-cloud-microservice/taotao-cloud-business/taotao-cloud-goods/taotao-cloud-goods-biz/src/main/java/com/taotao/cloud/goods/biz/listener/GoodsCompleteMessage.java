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

package com.taotao.cloud.goods.biz.listener;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 商品购买完成信息 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsCompleteMessage {

    @Schema(description = "商品id")
    private String goodsId;

    @Schema(description = "商品skuId")
    private String skuId;

    @Schema(description = "购买会员sn")
    private String memberId;

    @Schema(description = "购买数量")
    private Integer buyNum;
}

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

package com.taotao.cloud.goods.biz.model.vo;

import com.taotao.boot.common.model.result.PageResult;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import lombok.*;
import lombok.Data;
import lombok.experimental.*;
import lombok.NoArgsConstructor;

/**
 * 库存警告封装类
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-14 21:52:39
 */
@Data
@Accessors(chain=true)
@AllArgsConstructor
@NoArgsConstructor
public class StockWarningVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -7605952923416404638L;

    @Schema(description = "库存警告数量")
    private Integer stockWarningNum;

    @Schema(description = "商品SKU列表")
    private PageResult<GoodsSkuVO> goodsSkuPage;
}

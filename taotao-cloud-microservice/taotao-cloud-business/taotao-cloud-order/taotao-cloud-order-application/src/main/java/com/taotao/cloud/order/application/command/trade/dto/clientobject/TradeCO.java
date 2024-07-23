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

package com.taotao.cloud.order.application.command.trade.dto.clientobject;

import com.taotao.cloud.order.application.model.vo.cart.CartVO;
import com.taotao.cloud.order.application.model.vo.cart.PriceDetailVO;
import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 整比交易对象
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-21 16:59:38
 */
@RecordBuilder
@Schema(description = "整比交易对象")
public record TradeCO(
        @Schema(description = "购物车列表") List<CartVO> cartList,
        @Schema(description = "购物车车计算后的总价") PriceDetailVO priceDetailVO)
        implements Serializable {

    @Serial
    private static final long serialVersionUID = -4563542542090139404L;
}

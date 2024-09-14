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

package com.taotao.cloud.order.application.command.trade.dto;

import com.taotao.boot.common.enums.ClientTypeEnum;
import com.taotao.cloud.order.application.model.dto.cart.StoreRemarkDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 交易参数 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "交易参数")
public class TradeAddCmd implements Serializable {

    @Serial
    private static final long serialVersionUID = -8383072817737513063L;

    @Schema(description = "购物车购买：CART/立即购买：BUY_NOW/拼团购买：PINTUAN / 积分购买：POINT")
    private String way;

    /**
     * @see ClientTypeEnum
     */
    @Schema(description = "客户端：H5/移动端 PC/PC端,WECHAT_MP/小程序端,APP/移动应用端")
    private String client;

    @Schema(description = "店铺备注")
    private List<StoreRemarkDTO> remark;

    @Schema(description = "是否为其他订单下的订单，如果是则为依赖订单的sn，否则为空")
    private String parentOrderSn;
}

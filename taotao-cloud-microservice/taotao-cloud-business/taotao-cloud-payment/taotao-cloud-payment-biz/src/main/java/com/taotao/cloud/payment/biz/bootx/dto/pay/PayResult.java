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

package com.taotao.cloud.payment.biz.bootx.dto.pay;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author xxm
 * @date 2020/12/9
 */
@Data
@Accessors(chain = true)
@Schema(title = "支付返回信息")
public class PayResult implements Serializable {

    private static final long serialVersionUID = 7729669194741851195L;

    @Schema(description = "是否是异步支付")
    private boolean asyncPayMode;

    @Schema(description = "异步支付通道")
    private Integer asyncPayChannel;

    /** 主支付记录 */
    @JsonIgnore private PaymentInfo payment;

    /**
     * @see TRADE_PROGRESS
     */
    @Schema(description = "支付状态")
    private int payStatus;

    @Schema(description = "异步支付参数")
    private AsyncPayInfo asyncPayInfo;
}

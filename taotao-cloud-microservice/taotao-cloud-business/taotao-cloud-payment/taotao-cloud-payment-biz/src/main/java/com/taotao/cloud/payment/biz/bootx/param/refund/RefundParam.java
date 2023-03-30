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

package com.taotao.cloud.payment.biz.bootx.param.refund;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 退款参数
 *
 * @author xxm
 * @date 2020/12/10
 */
@Data
@Accessors(chain = true)
@Schema(title = "退款参数")
public class RefundParam {

    @Schema(description = "支付id", hidden = true)
    @Deprecated
    @JsonIgnore
    private String paymentId;

    @Schema(description = "业务id")
    private String businessId;

    @Schema(description = "各通道退款参数")
    private List<RefundModeParam> refundModeParams;
}

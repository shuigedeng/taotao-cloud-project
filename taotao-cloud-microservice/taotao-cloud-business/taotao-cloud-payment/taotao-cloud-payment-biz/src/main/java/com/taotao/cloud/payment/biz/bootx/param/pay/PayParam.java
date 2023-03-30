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

package com.taotao.cloud.payment.biz.bootx.param.pay;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author xxm
 * @date 2020/12/9
 */
@Data
@Accessors(chain = true)
@Schema(title = "支付参数")
public class PayParam implements Serializable {
    private static final long serialVersionUID = 3895679513150533566L;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "业务ID")
    private String businessId;

    @Schema(description = "支付标题")
    private String title;

    @Schema(description = "支付描述")
    private String description;

    @Schema(description = "支付信息", required = true)
    private List<PayModeParam> payModeList;
}

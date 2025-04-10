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

package com.taotao.cloud.member.api.feign.request;

import com.taotao.cloud.member.api.enums.DepositServiceTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.*;
import lombok.Data;
import lombok.experimental.*;
import lombok.NoArgsConstructor;

/** 会员余额变动模型 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberWalletUpdateApiRequest {

    @Schema(description = "变动金额")
    private BigDecimal money;

    @Schema(description = "变动会员id")
    private Long memberId;

    @Schema(description = "日志详情")
    private String detail;

    /**
     * @see DepositServiceTypeEnum
     */
    @Schema(description = "变动业务原因")
    private String serviceType;
}

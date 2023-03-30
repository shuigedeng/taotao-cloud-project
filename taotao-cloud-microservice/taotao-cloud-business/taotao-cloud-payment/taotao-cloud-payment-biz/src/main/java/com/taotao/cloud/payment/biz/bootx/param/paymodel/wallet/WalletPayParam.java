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

package com.taotao.cloud.payment.biz.bootx.param.paymodel.wallet;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 钱包支付参数
 *
 * @author xxm
 * @date 2020/12/8
 */
@Data
@Accessors(chain = true)
@Schema(title = "钱包支付参数")
public class WalletPayParam implements Serializable {

    private static final long serialVersionUID = 3255160458016870367L;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "操作人")
    private Long operatorId;

    /**
     * @see WalletCode
     */
    @Schema(description = "操作源 1系统 2管理员 3用户")
    private Integer operationSource;
}

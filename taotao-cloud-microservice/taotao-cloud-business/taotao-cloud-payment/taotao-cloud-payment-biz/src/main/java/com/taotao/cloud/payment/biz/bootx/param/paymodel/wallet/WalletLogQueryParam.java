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
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author xxm
 * @date 2020/12/8
 */
@Data
@Accessors(chain = true)
@Schema(title = "钱包日志查询参数")
public class WalletLogQueryParam implements Serializable {

    private static final long serialVersionUID = -4046664021959786637L;

    @Schema(description = "钱包ID (与userId至少存在一个)")
    private Long walletId;

    @Schema(description = "用户ID (钱包至少存在一个)")
    private Long userId;

    @Schema(description = "开始日期")
    private LocalDateTime startDate;

    @Schema(description = "结束日期")
    private LocalDateTime endDate;

    @Schema(description = "日志类型，不传则查询全部")
    private List<Integer> type;
}

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

package com.taotao.cloud.store.api.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.taotao.cloud.store.api.enums.BillStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 结算单VO */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "结算单VO")
public class BillListVO {

    @Schema(description = "账单ID")
    private String id;

    @Schema(description = "账单号")
    private String sn;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @Schema(description = "结算开始时间")
    private LocalDateTime startTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @Schema(description = "结算结束时间")
    private LocalDateTime endTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @Schema(description = "出账时间")
    private LocalDateTime createTime;

    /**
     * @see BillStatusEnum
     */
    @Schema(description = "状态：OUT(已出账),RECON(已对账),PASS(已审核),PAY(已付款)")
    private String billStatus;

    @Schema(description = "店铺名称")
    private String storeName;

    @Schema(description = "最终结算金额")
    private BigDecimal billPrice;
}

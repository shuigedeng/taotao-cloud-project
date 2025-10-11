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

package com.taotao.cloud.order.biz.model.page.order;

import com.taotao.boot.common.model.request.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.time.LocalDateTime;
import lombok.*;
import lombok.Data;
import lombok.experimental.*;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 店铺流水查询DTO
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 09:19:50
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain=true)
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "店铺流水查询DTO")
public class StoreFlowPageQuery extends PageQuery {

    @Serial
    private static final long serialVersionUID = 8808470688518188146L;

    @Schema(description = "类型")
    private String type;

    @Schema(description = "售后编号")
    private String refundSn;

    @Schema(description = "售后编号")
    private String orderSn;

    @Schema(description = "过滤只看分销订单")
    private Boolean justDistribution;

    @Schema(description = "结算单")
    private BillDTO bill;

    @Data
    public static class BillDTO {
        private LocalDateTime startTime;

        private LocalDateTime endTime;

        private Long storeId;
    }
}

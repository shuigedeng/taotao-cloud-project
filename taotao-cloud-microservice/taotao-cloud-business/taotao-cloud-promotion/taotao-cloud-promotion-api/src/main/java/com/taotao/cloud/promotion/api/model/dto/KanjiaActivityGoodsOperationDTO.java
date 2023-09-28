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

package com.taotao.cloud.promotion.api.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 砍价活动商品操作DTO */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KanjiaActivityGoodsOperationDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -1378599087650538592L;

    @Min(message = "活动开始时间不能为空", value = 0)
    @Schema(description = "活动开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @Min(message = "活动结束时间不能为空", value = 0)
    @Schema(description = "活动结束时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    @Schema(description = "砍价活动商品列表")
    List<KanjiaActivityGoodsDTO> promotionGoodsList;
}

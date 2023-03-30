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

package com.taotao.cloud.store.api.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 模版详细配置
 *
 * @since 2018-08-22 15:10:51
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "模版详细配置")
public class FreightTemplateChildDTO {

    private static final long serialVersionUID = -4143478496868965214L;

    private Long freightTemplateId;

    private BigDecimal firstCompany;

    private BigDecimal firstPrice;

    private BigDecimal continuedCompany;

    private BigDecimal continuedPrice;

    private String area;

    private String areaId;

    // ***************************************************************************

    @NotEmpty(message = "计价方式不能为空")
    @Schema(description = "计价方式：按件、按重量", allowableValues = "WEIGHT, NUM")
    private String pricingMethod;
}

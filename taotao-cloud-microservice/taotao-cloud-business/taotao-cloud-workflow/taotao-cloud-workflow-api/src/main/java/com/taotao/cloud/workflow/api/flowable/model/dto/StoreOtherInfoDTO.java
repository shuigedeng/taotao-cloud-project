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

package com.taotao.cloud.workflow.api.flowable.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 店铺入驻其他信息
 *
 * @since 2020/12/7 16:16
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "店铺入驻其他信息")
public class StoreOtherInfoDTO {

    @Size(min = 2, max = 200)
    @NotBlank(message = "店铺名称不能为空")
    @Schema(description = "店铺名称")
    private String storeName;

    @Schema(description = "店铺logo")
    private String storeLogo;

    @Size(min = 6, max = 200)
    @NotBlank(message = "店铺简介不能为空")
    @Schema(description = "店铺简介")
    private String storeDesc;

    @Schema(description = "经纬度")
    @NotEmpty
    private String storeCenter;

    @NotBlank(message = "店铺经营类目不能为空")
    @Schema(description = "店铺经营类目")
    private String goodsManagementCategory;

    @NotBlank(message = "地址不能为空")
    @Schema(description = "地址名称， '，'分割")
    private String storeAddressPath;

    @NotBlank(message = "地址ID不能为空")
    @Schema(description = "地址id，'，'分割 ")
    private String storeAddressIdPath;

    @NotBlank(message = "地址详情")
    @Schema(description = "地址详情")
    private String storeAddressDetail;
}
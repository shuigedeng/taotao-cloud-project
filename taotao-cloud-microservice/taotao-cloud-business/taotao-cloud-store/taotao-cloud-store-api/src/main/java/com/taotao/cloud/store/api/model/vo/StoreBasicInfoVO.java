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

import com.taotao.cloud.store.api.enums.StoreStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 店铺基本信息DTO */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "店铺基本信息DTO")
public class StoreBasicInfoVO {

    @Schema(description = "店铺ID")
    private String storeId;

    @Schema(description = "店铺名称")
    private String storeName;

    /**
     * @see StoreStatusEnum
     */
    @Schema(description = "店铺状态")
    private String storeDisable;

    @Schema(description = "地址名称， '，'分割")
    private String companyAddressPath;

    @Schema(description = "店铺logo")
    private String storeLogo;

    @Schema(description = "店铺简介")
    private String storeDesc;

    @Schema(description = "PC端页面")
    private String pcPageData;

    @Schema(description = "移动端页面")
    private String mobilePageData;

    @Schema(description = "是否自营")
    private String selfOperated;

    @Schema(description = "商品数量")
    private Integer goodsNum;

    @Schema(description = "收藏数量")
    private Integer collectionNum;

    @Schema(description = "腾讯云智服唯一标识")
    private String yzfSign;

    @Schema(description = "腾讯云智服小程序唯一标识")
    private String yzfMpSign;

    @Schema(description = "udesk标识")
    private String merchantEuid;
}

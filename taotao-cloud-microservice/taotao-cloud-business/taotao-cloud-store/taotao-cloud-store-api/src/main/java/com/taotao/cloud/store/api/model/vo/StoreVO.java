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
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.*;
import lombok.NoArgsConstructor;

/** 店铺VO */
@Data
@Accessors(chain=true)
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "店铺VO")
public class StoreVO {
    private Long id;

    private String nickname;

    private Long memberId;

    private String memberName;

    private String storeName;

    private LocalDateTime storeEndTime;

    /**
     * @see StoreStatusEnum
     */
    private String storeDisable;

    private Boolean selfOperated;

    private String storeLogo;

    private String storeCenter;

    private String storeDesc;

    private String storeAddressPath;

    private String storeAddressIdPath;

    private String storeAddressDetail;

    private BigDecimal descriptionScore;

    private BigDecimal serviceScore;

    private BigDecimal deliveryScore;

    private Integer goodsNum;

    private Integer collectionNum;

    private String yzfSign;

    private String yzfMpSign;

    private String merchantEuid;
}

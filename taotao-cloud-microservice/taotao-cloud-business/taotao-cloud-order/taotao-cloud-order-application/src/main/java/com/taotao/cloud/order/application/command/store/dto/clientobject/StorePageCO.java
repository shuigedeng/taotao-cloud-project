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

package com.taotao.cloud.order.application.command.store.dto.clientobject;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 会员收货地址DTO
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-11 14:55:28
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "会员收货地址DTO")
public class StorePageCO implements Serializable {

    @Serial
    private static final long serialVersionUID = -7605952923416404638L;

    @Schema(description = "会员ID")
    private String memberId;

    @Schema(description = "收货人姓名")
    private String name;

    @Schema(description = "手机号码")
    private String mobile;

    @Schema(description = "地址名称，逗号分割")
    private String consigneeAddressPath;

    @Schema(description = "地址id,逗号分割")
    private String consigneeAddressIdPath;

    @Schema(description = "省")
    private String province;

    @Schema(description = "市")
    private String city;

    @Schema(description = "区县")
    private String area;

    @Schema(description = "省code")
    private String provinceCode;

    @Schema(description = "市code")
    private String cityCode;

    @Schema(description = "区县code")
    private String areaCode;

    @Schema(description = "街道地址")
    private String address;

    @Schema(description = "详细地址")
    private String detail;

    @Schema(description = "是否为默认收货地址")
    private Boolean defaulted;

    @Schema(description = "地址别名")
    private String alias;

    @Schema(description = "经度")
    private String lon;

    @Schema(description = "纬度")
    private String lat;

    @Schema(description = "邮政编码")
    private String postalCode;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "最后修改时间")
    private LocalDateTime lastModifiedTime;
}

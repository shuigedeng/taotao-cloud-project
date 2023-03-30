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

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 店铺其他信息 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "店铺其他信息")
public class StoreOtherVO {

    @Schema(description = "公司名称")
    private String companyName;

    @Schema(description = "公司地址")
    private String companyAddress;

    @Schema(description = "公司地址地区")
    private String companyAddressPath;

    @Schema(description = "营业执照电子版")
    private String licencePhoto;

    @Schema(description = "法定经营范围")
    private String scope;

    @Schema(description = "员工总数")
    private Integer employeeNum;
}

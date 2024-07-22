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

package com.taotao.cloud.sys.api.feign.response.setting;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.Data;

/** 基础设置 */
@Data
public class BaseSettingApiResponse implements Serializable {

    private static final long serialVersionUID = -3138023944444671722L;

    @Schema(description = "站点名称")
    private String siteName;

    @Schema(description = "icp")
    private String icp;

    @Schema(description = "后端logo")
    private String domainLogo;

    @Schema(description = "后端icon")
    private String domainIcon;

    @Schema(description = "买家端logo")
    private String buyerSideLogo;

    @Schema(description = "买家端icon")
    private String buyerSideIcon;

    @Schema(description = "商家端logo")
    private String storeSideLogo;

    @Schema(description = "商家端icon")
    private String storeSideIcon;

    @Schema(description = "站点地址")
    private String staticPageAddress;

    @Schema(description = "wap站点地址")
    private String staticPageWapAddress;
}

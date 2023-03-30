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

package com.taotao.cloud.workflow.biz.common.model.app;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/** */
@Data
public class AppMenuModel {
    @Schema(description = "扩展字段")
    private String propertyJson;

    @Schema(description = "菜单编码")
    private String enCode;

    @Schema(description = "菜单名称")
    private String fullName;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "主键id")
    private String id;

    @Schema(description = "链接地址")
    private String urlAddress;
}

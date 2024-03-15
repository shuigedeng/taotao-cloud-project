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

package com.taotao.cloud.customer.facade.model.co.menu;

import com.taotao.cloud.sys.adapter.model.co.menu.MenuMetaVO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 菜单
 *
 * @author shuigedeng
 * @since 2020/5/14 10:44
 */
@Data
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "菜单VO")
public class MenuVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -5853343562172855421L;

    @Schema(description = "菜单名称")
    private String name;

    @Schema(description = "菜单路径")
    private String path;

    @Schema(description = "菜单redirect")
    private String redirect;

    @Schema(description = "菜单组件名称")
    private String component;

    @Schema(description = "菜单alwaysShow")
    private Boolean alwaysShow;

    @Schema(description = "菜单meta")
    private MenuMetaVO meta;

    @Schema(description = "菜单children")
    private List<MenuVO> children;
}

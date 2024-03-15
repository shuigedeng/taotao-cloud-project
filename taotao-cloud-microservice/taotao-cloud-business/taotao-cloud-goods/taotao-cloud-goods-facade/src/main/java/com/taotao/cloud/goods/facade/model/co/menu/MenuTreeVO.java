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

package com.taotao.cloud.goods.facade.model.co.menu;

import com.taotao.cloud.common.tree.MapperNode;
import com.taotao.cloud.goods.adapter.model.co.menu.MenuMetaVO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 树形菜单列表
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/10/21 11:09
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "树形菜单列表")
public class MenuTreeVO extends MapperNode implements Serializable {

    @Serial
    private static final long serialVersionUID = -5853343562172855421L;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "菜单名称")
    private String name;

    @Schema(description = "权限标识")
    private String perms;

    @Schema(description = "spread")
    private Boolean spread;

    @Schema(description = "前端path / 即跳转路由")
    private String path;

    @Schema(description = "是否缓存页面: 0:否 1:是 (默认值0)")
    private Boolean keepAlive;

    @Schema(description = "菜单类型 1：目录 2：菜单 3：按钮")
    private Integer type;

    @Schema(description = "菜单标签")
    private String label;

    private String component;

    private Boolean hidden;

    private String redirect;

    private Boolean alwaysShow;

    private Boolean target;

    private String typeName;

    private LocalDateTime createTime;

    private MenuMetaVO meta;
}

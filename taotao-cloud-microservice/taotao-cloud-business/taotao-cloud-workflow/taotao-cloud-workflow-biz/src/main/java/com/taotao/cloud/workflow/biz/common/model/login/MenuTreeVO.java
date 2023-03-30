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

package com.taotao.cloud.workflow.biz.common.model.login;

import java.util.List;
import lombok.Data;

/** */
@Data
public class MenuTreeVO {
    @Schema(description = "主键")
    private String id;

    @Schema(description = "名称")
    private String fullName;

    @Schema(description = "菜单编码")
    private String enCode;

    @Schema(description = "父主键")
    private String parentId;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "是否有下级菜单")
    private Boolean hasChildren;

    @Schema(description = "菜单地址")
    private String urlAddress;

    @Schema(description = "链接目标")
    private String linkTarget;

    @Schema(description = "下级菜单列表")
    private List<MenuTreeVO> children;

    @Schema(description = "菜单分类【1-类别、2-页面】")
    private Integer type;

    private String propertyJson;
    private Long sortCode;
}

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

package com.taotao.cloud.workflow.biz.common.util.treeutil;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/** 需要实现树的类可以继承该类，手写set方法，在设定本身属性值时同时设置该类中的相关属性 */
@Data
public class TreeModel<T> {
    @Schema(description = "主键")
    private String id;

    @Schema(description = "名称")
    private String fullName;

    @Schema(description = "父主键")
    private String parentId;

    @Schema(description = "是否有下级菜单")
    private Boolean hasChildren = true;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "下级菜单列表")
    private List<TreeModel<T>> children = new ArrayList<>();
}

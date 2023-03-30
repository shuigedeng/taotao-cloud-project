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

import java.util.Map;
import lombok.Data;

/** */
@Data
public class TreeListModel {
    /** 主键 */
    private String id;
    /** 名称 */
    private String text;
    /** 节点 */
    private String parentId;
    /** 表示此节点是否展开 */
    private Boolean expanded;
    /** 表示是否加载完成 */
    private Boolean loaded;
    /** 表示此数据是否为叶子节点 */
    private Boolean isLeaf;
    /** 表示此数据在哪一级 */
    private Integer level;
    /** 存储对象 */
    private Map<String, Object> ht;
}

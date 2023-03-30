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

package com.taotao.cloud.workflow.biz.common.model.visiual;

import lombok.Data;

/** */
@Data
public class ColumnDataModel {
    private String searchList;
    private String columnList;
    private String defaultColumnList;
    private String sortList;
    private Integer type;
    private String defaultSidx;
    private String sort;
    private Boolean hasPage;
    private Integer pageSize;
    private String treeTitle;
    private String treeDataSource;
    private String treeDictionary;
    private String treeRelation;
    private String treePropsUrl;
    private String treePropsValue;
    private String treePropsChildren;
    private String treePropsLabel;
    private String groupField;
    private String btnsList;
    private String columnBtnsList;
    /** 自定义按钮区 */
    private String customBtnsList;
    /** 列表权限 */
    private Boolean useColumnPermission;
    /** 表单权限 */
    private Boolean useFormPermission;
    /** 按钮权限 */
    private Boolean useBtnPermission;
    /** 数据权限 */
    private Boolean useDataPermission;
}

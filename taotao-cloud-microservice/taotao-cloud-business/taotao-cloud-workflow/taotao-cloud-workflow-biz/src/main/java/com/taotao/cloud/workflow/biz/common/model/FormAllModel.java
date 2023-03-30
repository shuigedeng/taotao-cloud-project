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

package com.taotao.cloud.workflow.biz.common.model;

import lombok.Data;

/** 解析引擎 */
@Data
public class FormAllModel {
    /** 所有模板的标签 row(栅格)、card(卡片)、table(子表)、mast(主表)、mastTable(主表)、groupTitle(分组标题)* */
    private String workflowKey;
    /** 是否是结束标签 0.不是 1.是* */
    private String isEnd = "0";
    /** 主表数据* */
    private FormColumnModel formColumnModel;
    /** 子表的数据* */
    private FormColumnTableModel childList;
    /** 栅格和卡片等数据* */
    private FormModel formModel;
    /** 主表中有子表数据* */
    private FormMastTableModel formMastTableModel;
}

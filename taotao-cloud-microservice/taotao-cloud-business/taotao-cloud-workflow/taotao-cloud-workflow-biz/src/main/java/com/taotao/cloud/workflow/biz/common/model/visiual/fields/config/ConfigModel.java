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

package com.taotao.cloud.workflow.biz.common.model.visiual.fields.config;

import com.taotao.cloud.workflow.biz.common.model.visiual.fields.FieLdsModel;
import java.util.List;
import lombok.Data;

/** */
@Data
public class ConfigModel {
    private String label;
    private String labelWidth;
    private Boolean showLabel;
    private Boolean changeTag;
    private Boolean border;
    private String tag;
    private String tagIcon;
    private boolean required = false;
    private String layout;
    private String dataType;
    private Integer span = 24;
    private String workflowKey;
    private String dictionaryType;
    private Integer formId;
    private Long renderKey;
    private Integer columnWidth;
    private List<RegListModel> regList;
    private Object defaultValue;
    private String active;
    /** app静态数据 */
    private String options;
    /** 判断defaultValue类型 */
    private String valueType;

    private String propsUrl;
    private String optionType;
    private ConfigPropsModel props;
    /** 子表添加字段 */
    private Boolean showTitle;

    private String tableName;
    private List<FieLdsModel> children;

    /** 多端显示 */
    private String visibility = "[\"app\",\"pc\"]";

    /** 单据规则使用 */
    private String rule;

    /** 验证规则触发方式 */
    private String trigger = "blur";
    /** 隐藏 */
    private Boolean noShow = false;
    /** app代码生成器 */
    private int childNum;

    private String model;

    /** 代码生成器多端显示 */
    private boolean app = true;

    private boolean pc = true;
}

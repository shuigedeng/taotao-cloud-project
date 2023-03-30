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

import com.taotao.cloud.workflow.biz.common.model.visiual.fields.FieLdsModel;
import com.taotao.cloud.workflow.biz.common.model.visiual.fields.config.ConfigModel;
import com.taotao.cloud.workflow.biz.common.model.visiual.fields.slot.SlotModel;
import java.util.List;
import lombok.Data;

/** 解析引擎 */
@Data
public class FormModel {

    /** 卡片 */
    private String shadow;

    private String header;

    /** 栅格 */
    private Integer span;

    /** 标签页 */
    private String title;

    private String name;
    private String model;
    private Boolean accordion;

    /** 标签页 */
    private String tabPosition;

    private String type;

    /** 折叠、标签公用 */
    private String active;

    /** 判断折叠、标签是否最外层 0.不是 1.是* */
    private String outermost;

    /** 折叠、标签公用的子节点 */
    private List<FieLdsModel> children;

    /** 分组标题 */
    private String content;
    /** 分割线 */
    private SlotModel slot;
    /** 文本 */
    private String textStyle;

    private String style;
    private ConfigModel config;

    /** 分组标签、分割线公用 */
    private String contentposition;

    /** 按钮 */
    private String align;

    private String buttonText;

    /** app代码生成器 */
    private int childNum;

    /** 二维码条形码 */
    private String dataType = "";

    private String relationField;

    private String visibility;
}

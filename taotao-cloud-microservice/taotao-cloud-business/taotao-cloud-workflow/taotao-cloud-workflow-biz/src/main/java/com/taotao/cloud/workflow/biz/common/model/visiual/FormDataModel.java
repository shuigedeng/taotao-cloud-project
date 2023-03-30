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

import com.taotao.cloud.workflow.biz.common.model.visiual.fields.FieLdsModel;
import lombok.Data;

/** */
@Data
public class FormDataModel {
    /** 模块 */
    private String areasName;
    /** 功能名称 */
    private String className;
    /** 后端目录 */
    private String serviceDirectory;
    /** 所属模块 */
    private String module;
    /** 子表名称集合 */
    private String subClassName;

    private String formRef;
    private String formModel;
    private String size;
    private String labelPosition;
    private Integer labelWidth;
    private String formRules;
    private String drawerWidth;
    private Integer gutter;
    private Boolean disabled;
    private String span;
    private Boolean formBtns;
    private Integer idGlobal;
    private String fields;
    private String popupType;
    private String fullScreenWidth;
    private String formStyle;
    private String generalWidth;
    private Boolean hasCancelBtn;
    private String cancelButtonText;
    private Boolean hasConfirmBtn;
    private String confirmButtonText;
    private Boolean hasPrintBtn;
    private String printButtonText;

    private String printId;

    private FieLdsModel children;
}

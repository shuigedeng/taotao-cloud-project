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

package com.taotao.cloud.workflow.biz.flowable.bpm.service.definition.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Bpm 表单的 Field 表单项 Response DTO 字段的定义，可见 https://github.com/JakHuang/form-generator/issues/46 文档
 *
 * @author 芋道源码
 */
@Data
public class BpmFormFieldRespDTO {

    /** 表单标题 */
    private String label;
    /** 表单字段的属性名，可自定义 */
    @JsonProperty(value = "vModel")
    private String vModel;
}

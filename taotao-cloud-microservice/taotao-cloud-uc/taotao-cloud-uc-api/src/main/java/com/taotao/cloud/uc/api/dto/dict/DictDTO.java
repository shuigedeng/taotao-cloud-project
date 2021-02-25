/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.uc.api.dto.dict;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 添加字典实体对象
 *
 * @author dengtao
 * @since 2020/9/30 08:49
 * @version 1.0.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "添加字典对象DTO")
public class DictDTO implements Serializable {

    private static final long serialVersionUID = -7605952923416404638L;

    @ApiModelProperty(value = "字典名称", required = true)
    @NotBlank(message = "字典名称不能为空")
    @Size(max = 10, message = "字典名称不能超过10个字符")
    private String dictName;

    @ApiModelProperty(value = "字典编码", required = true)
    @NotBlank(message = "字典编码不能为空")
    @Size(max = 10, message = "字典编码不能超过10个字符")
    private String dictCode;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "排序值")
    private Integer dictSort;

    @ApiModelProperty(value = "备注信息")
    private String remark;
}

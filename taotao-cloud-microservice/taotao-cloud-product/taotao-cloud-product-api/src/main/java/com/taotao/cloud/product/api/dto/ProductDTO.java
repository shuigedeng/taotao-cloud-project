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
package com.taotao.cloud.product.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author dengtao
 * @date 2020/10/22 12:29
 * @since v1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "商品对象DTO")
public class ProductDTO implements Serializable {

    private static final long serialVersionUID = 5126530068827085130L;

    @ApiModelProperty(value = "商品名称")
    private String name;

    @ApiModelProperty(value = "供应商id")
    private Long supplierId;

    @ApiModelProperty(value = "图片id")
    private Long picId;

    @ApiModelProperty(value = "视频id")
    private Long videoId;

    @ApiModelProperty(value = "商品详情图片id")
    private Long detailPicId;

    @ApiModelProperty(value = "商品第一张图片id")
    private Long firstPicId;

    @ApiModelProperty(value = "商品海报id")
    private Long posterPicId;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "商品状态")
    private Integer status;
}

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

package com.taotao.cloud.stock.api.model.dto;

import java.io.Serializable;

// @ApiModel(value = "商品对象DTO")
public class ProductDTO implements Serializable {

    private static final long serialVersionUID = 5126530068827085130L;

    //    @Schema(description =  "商品名称")
    private String name;

    //    @Schema(description =  "供应商id")
    private Long supplierId;

    //    @Schema(description =  "图片id")
    private Long picId;

    //    @Schema(description =  "视频id")
    private Long videoId;

    //    @Schema(description =  "商品详情图片id")
    private Long detailPicId;

    //    @Schema(description =  "商品第一张图片id")
    private Long firstPicId;

    //    @Schema(description =  "商品海报id")
    private Long posterPicId;

    //    @Schema(description =  "备注")
    private String remark;

    //    @Schema(description =  "商品状态")
    private Integer status;
}

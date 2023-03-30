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

package com.taotao.cloud.workflow.biz.common.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

/** */
@Data
public class Pagination extends Page {
    @Schema(description = "每页条数", example = "20")
    private long pageSize = 20;

    @Schema(description = "排序类型")
    private String sort = "desc";

    @Schema(description = "排序列")
    private String sidx = "";

    @Schema(description = "当前页数", example = "1")
    private long currentPage = 1;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private long total;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private long records;

    public <T> List<T> setData(List<T> data, long records) {
        this.total = records;
        return data;
    }
}

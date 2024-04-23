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

package com.taotao.cloud.goods.application.command.goods.dto.clientobject;

import com.taotao.cloud.goods.biz.model.dto.GoodsParamsDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/** 商品关联参数的VO */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsParamsCO extends GoodsParamsDTO {

    @Serial
    private static final long serialVersionUID = -4904700751774005326L;

    @Schema(description = "1 输入项   2 选择项")
    private Integer paramType;

    @Schema(description = " 选择项的内容获取值，使用optionList")
    private String options;

    @Schema(description = "是否必填是  1    否   0")
    private Integer required;
    // @Schema(description = "参数组id")
    // private String groupId;
    @Schema(description = "是否可索引  1 可以   0不可以")
    private Integer isIndex;

    private String[] optionList;

    public void setOptionList(String[] optionList) {
        this.optionList = optionList;
    }

    public String[] getOptionList() {
        if (options != null) {
            return options.replaceAll("\r|\n", "").split(",");
        }
        return optionList;
    }
}

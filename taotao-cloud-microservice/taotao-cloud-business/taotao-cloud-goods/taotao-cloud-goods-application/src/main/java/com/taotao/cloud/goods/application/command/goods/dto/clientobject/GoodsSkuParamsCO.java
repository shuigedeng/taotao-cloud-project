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
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 商品VO
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-14 21:32:40
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "商品VO")
public class GoodsSkuParamsCO extends GoodsCO {

    @Serial
    private static final long serialVersionUID = 6377623919990713567L;

    @Schema(description = "分类名称")
    private List<String> categoryName;

    @Schema(description = "商品参数")
    private List<GoodsParamsDTO> goodsParamsDTOList;

    @Schema(description = "商品图片")
    private List<String> goodsGalleryList;

    @Schema(description = "sku列表")
    private List<GoodsSkuSpecGalleryCO> skuList;
}

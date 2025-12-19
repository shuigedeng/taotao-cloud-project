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

package com.taotao.cloud.goods.biz.elasticsearch.pojo;

import com.taotao.boot.common.utils.lang.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.experimental.*;

/**
 * EsGoodsSearchDTO
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
@Data
public class EsGoodsSearchDTO {

    @Schema(description = "关键字")
    private String keyword;

    @Schema(description = "分类")
    private String categoryId;

    @Schema(description = "品牌,可以多选 品牌Id@品牌Id@品牌Id")
    private String brandId;

    @Schema(description = "是否为推荐商品")
    private Boolean recommend;

    @Schema(description = "价格", example = "10_30")
    private String price;

    @Schema(description = "属性:参数名_参数值@参数名_参数值", example = "屏幕类型_LED@屏幕尺寸_15英寸")
    private String prop;

    @Schema(description = "规格项列表")
    private List<String> nameIds;

    @Schema(description = "卖家id，搜索店铺商品的时候使用")
    private String storeId;

    @Schema(description = "商家分组id，搜索店铺商品的时候使用")
    private String storeCatId;

    @Schema(hidden = true)
    private Map<String, List<String>> notShowCol = new HashMap<>();

    @Schema(description = "当前商品skuId,根据当前浏览的商品信息来给用户推荐可能喜欢的商品")
    private String currentGoodsId;

    // 过滤搜索关键字
    public String getKeyword() {
        if (StringUtils.isNotEmpty(keyword)) {
            // RegularUtil.replace(this.keyword);
        }
        return keyword;
    }
}

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

package com.taotao.cloud.goods.biz.model.page;

import com.taotao.boot.common.model.request.PageQuery;
import com.taotao.cloud.goods.api.enums.GoodsAuthEnum;
import com.taotao.cloud.goods.api.enums.GoodsStatusEnum;
import com.taotao.cloud.goods.api.enums.GoodsTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import lombok.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 商品查询参数
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:33:15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class GoodsPageQuery extends PageQuery {

    @Serial
    private static final long serialVersionUID = 2544015852728566887L;

    @Schema(description = "商品编号")
    private Long goodsId;

    @Schema(description = "商品名称")
    private String goodsName;

    @Schema(description = "商品编号")
    private String id;

    @Schema(description = "商家ID")
    private Long storeId;

    @Schema(description = "卖家名字")
    private String storeName;

    @Schema(description = "价格,可以为范围，如10_1000")
    private String price;

    @Schema(description = "分类path")
    private String categoryPath;

    @Schema(description = "店铺分类id")
    private String storeCategoryPath;

    @Schema(description = "是否自营")
    private Boolean selfOperated;

    /**
     * @see GoodsStatusEnum
     */
    @Schema(description = "上下架状态")
    private String marketEnable;

    /**
     * @see GoodsAuthEnum
     */
    @Schema(description = "审核状态")
    private String authFlag;

    @Schema(description = "库存数量")
    private Integer leQuantity;

    @Schema(description = "库存数量")
    private Integer geQuantity;

    @Schema(description = "是否为推荐商品")
    private Boolean recommend;

    /**
     * @see GoodsTypeEnum
     */
    @Schema(description = "商品类型")
    private String goodsType;
}

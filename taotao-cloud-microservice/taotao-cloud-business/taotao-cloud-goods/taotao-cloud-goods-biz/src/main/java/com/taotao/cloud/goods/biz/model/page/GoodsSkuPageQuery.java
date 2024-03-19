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

import java.io.Serial;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 规格商品查询条件
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:33:23
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GoodsSkuPageQuery extends GoodsPageQuery {

    @Serial
    private static final long serialVersionUID = -6235885068610635045L;

    // @Schema(description = "商品id")
    // private String goodsId;

    // @Override
    // public <T> QueryWrapper<T> queryWrapper() {
    // 	QueryWrapper<T> queryWrapper = super.queryWrapper();
    // 	queryWrapper.eq(StringUtils.isNotEmpty(goodsId), "goods_id", goodsId);
    // 	return queryWrapper;
    // }
}

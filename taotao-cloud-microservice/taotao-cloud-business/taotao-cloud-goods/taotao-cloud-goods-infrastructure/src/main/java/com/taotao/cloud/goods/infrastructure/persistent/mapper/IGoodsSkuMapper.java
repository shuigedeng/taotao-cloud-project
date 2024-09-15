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

package com.taotao.cloud.goods.infrastructure.persistent.mapper;

import com.taotao.cloud.goods.infrastructure.persistent.po.GoodsSkuPO;
import com.taotao.boot.web.base.mapper.BaseSuperMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * 规格项数据处理层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:57:22
 */
@Repository
public interface IGoodsSkuMapper extends BaseSuperMapper<GoodsSkuPO, Long> {

    /**
     * 根据商品id获取全部skuId的集合
     *
     * @param goodsId goodsId
     * @return {@link List }<{@link String }>
     * @since 2022-04-27 16:57:22
     */
    @Select("""
		SELECT id
		FROM tt_goods_sku
		WHERE goods_id = #{goodsId}
		""")
    List<String> getGoodsSkuIdByGoodsId(@Param(value = "goodsId") Long goodsId);
}

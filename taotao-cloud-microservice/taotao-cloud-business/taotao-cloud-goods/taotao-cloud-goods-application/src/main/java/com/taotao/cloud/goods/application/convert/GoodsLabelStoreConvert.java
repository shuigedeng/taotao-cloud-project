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

package com.taotao.cloud.goods.application.convert;

import com.taotao.cloud.goods.biz.model.dto.StoreGoodsLabelDTO;
import com.taotao.cloud.goods.biz.model.vo.StoreGoodsLabelInfoVO;
import com.taotao.cloud.goods.biz.model.entity.StoreGoodsLabel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * IGoodsLabelStoreMapStruct
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:58:16
 */
@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GoodsLabelStoreConvert {

    /** 实例 */
    GoodsLabelStoreConvert INSTANCE = Mappers.getMapper(GoodsLabelStoreConvert.class);

    /**
     * 商店商品标签存储货物标签信息签证官
     *
     * @param storeGoodsLabel 商店商品标签
     * @return {@link StoreGoodsLabelInfoVO }
     * @since 2022-04-27 16:58:17
     */
    StoreGoodsLabelInfoVO convert(StoreGoodsLabel storeGoodsLabel);

    /**
     * 商店商品标签dtoto商店商品标签
     *
     * @param storeGoodsLabelDTO 商店商品标签dto
     * @return {@link StoreGoodsLabel }
     * @since 2022-04-27 16:58:17
     */
    StoreGoodsLabel convert(StoreGoodsLabelDTO storeGoodsLabelDTO);
}

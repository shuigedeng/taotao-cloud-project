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
package com.taotao.cloud.goods.biz.mapstruct;

import com.taotao.cloud.goods.api.dto.DraftGoodsSkuParamsDTO;
import com.taotao.cloud.goods.biz.entity.DraftGoods;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * IParametersMapStruct
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:58:13
 */
@Mapper(
	unmappedSourcePolicy = ReportingPolicy.IGNORE,
	unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IDraftGoodsMapStruct {

	/**
	 * 实例
	 */
	IDraftGoodsMapStruct INSTANCE = Mappers.getMapper(IDraftGoodsMapStruct.class);

	/**
	 * 商品dtoto草案起草产品
	 *
	 * @param draftGoodsSkuParamsDTO 商品dto草案
	 * @return {@link DraftGoods }
	 * @since 2022-04-27 16:58:13
	 */
	DraftGoods draftGoodsDTOToDraftGoods(DraftGoodsSkuParamsDTO draftGoodsSkuParamsDTO);


}

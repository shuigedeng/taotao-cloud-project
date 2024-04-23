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

import com.taotao.cloud.goods.biz.model.dto.BrandDTO;
import com.taotao.cloud.goods.biz.model.vo.BrandVO;
import com.taotao.cloud.goods.biz.model.entity.Brand;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * BrandMapStruct
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:57:55
 */
@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BrandConvert {

	/**
	 * 实例
	 */
	BrandConvert INSTANCE = Mappers.getMapper(BrandConvert.class);

	/**
	 * 品牌,品牌签证官
	 *
	 * @param brand 品牌
	 * @return {@link BrandVO }
	 * @since 2022-04-27 16:57:56
	 */
	BrandVO convert(Brand brand);

	/**
	 * 品牌品牌vos
	 *
	 * @param brands 品牌
	 * @return {@link List }<{@link BrandVO }>
	 * @since 2022-04-27 16:57:56
	 */
	List<BrandVO> convert(List<Brand> brands);

	/**
	 * 品牌dtoto品牌
	 *
	 * @param brandDTO 品牌dto
	 * @return {@link Brand }
	 * @since 2022-04-27 16:57:56
	 */
	Brand convert(BrandDTO brandDTO);
}

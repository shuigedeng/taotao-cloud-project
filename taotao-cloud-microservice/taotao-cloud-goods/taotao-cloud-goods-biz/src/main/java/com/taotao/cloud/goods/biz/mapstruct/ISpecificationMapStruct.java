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

import com.taotao.cloud.goods.api.web.dto.SpecificationDTO;
import com.taotao.cloud.goods.api.web.vo.SpecificationVO;
import com.taotao.cloud.goods.biz.model.entity.Specification;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * ISpecificationMapStruct
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:58:30
 */
@Mapper(
	unmappedSourcePolicy = ReportingPolicy.IGNORE,
	unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ISpecificationMapStruct {

	/**
	 * 实例
	 */
	ISpecificationMapStruct INSTANCE = Mappers.getMapper(ISpecificationMapStruct.class);

	/**
	 * 规范来规范vos
	 *
	 * @param specifications 规范
	 * @return {@link List }<{@link SpecificationVO }>
	 * @since 2022-04-27 16:58:30
	 */
	List<SpecificationVO> specificationsToSpecificationVOs(List<Specification> specifications);

	/**
	 * 规范dtoto规范
	 *
	 * @param specificationDTO 规范dto
	 * @return {@link Specification }
	 * @since 2022-04-27 16:58:30
	 */
	Specification specificationDTOToSpecification(SpecificationDTO specificationDTO);



}

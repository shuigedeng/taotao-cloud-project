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

package com.taotao.cloud.goods.biz.model.convert;

import com.taotao.cloud.goods.biz.model.dto.SpecificationDTO;
import com.taotao.cloud.goods.biz.model.vo.SpecificationVO;
import com.taotao.cloud.goods.biz.model.entity.Specification;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * ISpecificationMapStruct
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:58:30
 */
@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SpecificationConvert {

    /** 实例 */
    SpecificationConvert INSTANCE = Mappers.getMapper(SpecificationConvert.class);

    /**
     * 规范来规范vos
     *
     * @param specifications 规范
     * @return {@link List }<{@link SpecificationVO }>
     * @since 2022-04-27 16:58:30
     */
    List<SpecificationVO> convert(List<Specification> specifications);

    SpecificationVO convert(Specification specification);

    /**
     * 规范dtoto规范
     *
     * @param specificationDTO 规范dto
     * @return {@link Specification }
     * @since 2022-04-27 16:58:30
     */
    Specification convert(SpecificationDTO specificationDTO);
}

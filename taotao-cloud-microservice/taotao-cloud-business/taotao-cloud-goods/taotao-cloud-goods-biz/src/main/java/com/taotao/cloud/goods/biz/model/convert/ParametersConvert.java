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

import com.taotao.cloud.goods.biz.model.dto.ParametersDTO;
import com.taotao.cloud.goods.biz.model.vo.ParametersVO;
import com.taotao.cloud.goods.biz.model.entity.Parameters;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * IParametersMapStruct
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:58:27
 */
@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ParametersConvert {

    /** 实例 */
    ParametersConvert INSTANCE = Mappers.getMapper(ParametersConvert.class);

    /**
     * 参数参数vos
     *
     * @param parameters 参数
     * @return {@link List }<{@link ParametersVO }>
     * @since 2022-04-27 16:58:27
     */
    List<ParametersVO> convert(List<Parameters> parameters);

    /**
     * 参数dtoto参数
     *
     * @param parametersDTO 参数dto
     * @return {@link Parameters }
     * @since 2022-04-27 16:58:27
     */
    Parameters convert(ParametersDTO parametersDTO);
}

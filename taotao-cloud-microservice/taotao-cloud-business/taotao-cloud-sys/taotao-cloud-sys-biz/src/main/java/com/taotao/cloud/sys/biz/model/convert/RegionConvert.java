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

package com.taotao.cloud.sys.biz.model.convert;

import com.taotao.cloud.sys.biz.model.vo.region.RegionTreeVO;
import com.taotao.cloud.sys.biz.model.entity.region.Region;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * iregion地图结构
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 13:39:46
 */
@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RegionConvert {

    /** 实例 */
    RegionConvert INSTANCE = Mappers.getMapper(RegionConvert.class);

    /**
     * 区域列表给签证官
     *
     * @param regionList 区域列表
     * @return {@link List }<{@link RegionTreeVO }>
     * @since 2022-04-28 13:39:46
     */
    List<RegionTreeVO> convertTree(List<Region> regionList);
}

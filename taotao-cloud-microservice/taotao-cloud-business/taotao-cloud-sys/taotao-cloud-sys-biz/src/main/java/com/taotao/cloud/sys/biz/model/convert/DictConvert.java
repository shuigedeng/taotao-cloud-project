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

import com.taotao.cloud.sys.api.feign.response.FeignDictResponse;
import com.taotao.cloud.sys.biz.model.dto.dict.DictSaveDTO;
import com.taotao.cloud.sys.biz.model.entity.dict.Dict;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * idict地图结构
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 13:39:24
 */
@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DictConvert {

    /** 实例 */
    DictConvert INSTANCE = Mappers.getMapper(DictConvert.class);

    FeignDictResponse convert(Dict dict);

    /// **
    // * DictDTO转SysDict
    // *
    // * @param dictSaveDTO dictDTO
    // * @return com.taotao.cloud.sys.biz.entity.SysDict
    // * @since 2020/11/11 14:52
    // */
    // Dict dictDTOToSysDict(DictSaveDTO dictSaveDTO);

    /// **
    // * list -> SysUser转UserVO
    // *
    // * @param dictList userList
    // * @return java.util.List<com.taotao.cloud.sys.api.vo.user.UserVO>
    //
    // * @since 2020/11/11 15:00
    // */
    // List<DictQueryVO> sysDictToDictVO(List<Dict> dictList);
    //
    /// **
    // * sysDict转UserVO
    // *
    // * @param sysDict sysDict
    // * @return com.taotao.cloud.sys.api.vo.user.UserVO
    //
    // * @since 2020/11/11 14:47
    // */
    // DictQueryVO sysDictToDictVO(Dict sysDict);

    /**
     * 复制dict sys dictdto
     *
     * @param dictSaveDTO dict拯救dto
     * @param dict dict
     * @since 2022-04-28 13:39:25
     */
    void copy(DictSaveDTO dictSaveDTO, @MappingTarget Dict dict);
}

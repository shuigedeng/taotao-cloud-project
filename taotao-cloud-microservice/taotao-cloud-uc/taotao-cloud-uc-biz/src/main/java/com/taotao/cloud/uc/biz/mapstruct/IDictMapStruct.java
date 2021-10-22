/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.uc.biz.mapstruct;

import com.taotao.cloud.uc.api.dto.dict.DictSaveDTO;
import com.taotao.cloud.uc.api.vo.dict.DictQueryVO;
import com.taotao.cloud.uc.biz.entity.SysDict;
import java.util.List;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 
 * @version 1.0.0
 * @since 2020/11/11 14:42
 */
@Mapper(builder = @Builder(disableBuilder = true),
	unmappedSourcePolicy = ReportingPolicy.IGNORE,
	unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IDictMapStruct {

	IDictMapStruct INSTANCE = Mappers.getMapper(IDictMapStruct.class);

	/**
	 * DictDTO转SysDict
	 *
	 * @param dictSaveDTO dictDTO
	 * @return com.taotao.cloud.uc.biz.entity.SysDict
	 * @since 2020/11/11 14:52
	 */
	SysDict dictDTOToSysDict(DictSaveDTO dictSaveDTO);

	/**
	 * list -> SysUser转UserVO
	 *
	 * @param dictList userList
	 * @return java.util.List<com.taotao.cloud.uc.api.vo.user.UserVO>
	 
	 * @since 2020/11/11 15:00
	 */
	List<DictQueryVO> sysDictToDictVO(List<SysDict> dictList);

	/**
	 * sysDict转UserVO
	 *
	 * @param sysDict sysDict
	 * @return com.taotao.cloud.uc.api.vo.user.UserVO
	 
	 * @since 2020/11/11 14:47
	 */
	DictQueryVO sysDictToDictVO(SysDict sysDict);

	/**
	 * 拷贝 DictDTO 到SysUser
	 *
	 * @param dictSaveDTO dictDTO
	 * @param dict    dict
	 
	 * @since 2020/11/11 16:59
	 */
	void copyDictDtoToSysDict(DictSaveDTO dictSaveDTO, @MappingTarget SysDict dict);
}

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
package com.taotao.cloud.sys.biz.mapstruct;

import com.taotao.cloud.sys.api.vo.logistics.LogisticsVO;
import com.taotao.cloud.sys.biz.entity.config.LogisticsConfig;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author shuigedeng
 * @since 2020/11/11 14:42
 * @version 2022.03
 */
@Mapper(builder = @Builder(disableBuilder = true),
	unmappedSourcePolicy = ReportingPolicy.IGNORE,
	unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LogisticsMapStruct {

	LogisticsMapStruct INSTANCE = Mappers.getMapper(LogisticsMapStruct.class);

	/**
	 * file转FileVO
	 *
	 * @param file file
	 * @return com.taotao.cloud.sys.api.vo.user.UserVO
	 * @author shuigedeng
	 * @since 2020/11/11 14:47
	 * @version 2022.03
	 */
	LogisticsVO logisticsToFileVO(LogisticsConfig logisticsConfig);

	// /**
	//  * SysUser转AddUserVO
	//  *
	//  * @param sysUser sysUser
	//  * @return com.taotao.cloud.sys.api.vo.user.AddUserVO
	//  * @author shuigedeng
	//  * @since 2020/11/11 16:59
	//  * @version 2022.03
	//  */
	// AddUserVO sysUserToAddUserVO(SysUser sysUser);
	//
	// /**
	//  * list -> SysUser转UserVO
	//  *
	//  * @param userList userList
	//  * @return java.util.List<com.taotao.cloud.sys.api.vo.user.UserVO>
	//  * @author shuigedeng
	//  * @since 2020/11/11 15:00
	//  * @version 2022.03
	//  */
	// List<UserVO> sysUserToUserVO(List<SysUser> userList);
	//
	// /**
	//  * UserDTO转SysUser
	//  *
	//  * @param userDTO userDTO
	//  * @return com.taotao.cloud.sys.biz.entity.SysUser
	//  * @author shuigedeng
	//  * @since 2020/11/11 14:52
	//  * @version 2022.03
	//  */
	// SysUser userDtoToSysUser(UserDTO userDTO);
	//
	// /**
	//  * 拷贝 UserDTO 到SysUser
	//  *
	//  * @param userDTO userDTO
	//  * @param user    user
	//  * @return void
	//  * @author shuigedeng
	//  * @since 2020/11/11 16:59
	//  * @version 2022.03
	//  */
	// void copyUserDtoToSysUser(UserDTO userDTO, @MappingTarget SysUser user);
}

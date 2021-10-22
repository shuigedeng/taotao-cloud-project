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

import com.taotao.cloud.uc.api.dto.user.UserQueryDTO;
import com.taotao.cloud.uc.biz.entity.SysUser;
import com.taotao.cloud.uc.api.vo.user.UserQueryVO;
import com.taotao.cloud.uc.api.vo.user.UserRegisterVO;
import java.util.List;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * UserMapper
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-15 17:46:37
 */
@Mapper(builder = @Builder(disableBuilder = true),
	unmappedSourcePolicy = ReportingPolicy.IGNORE,
	unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IUserMapStruct {

	IUserMapStruct INSTANCE = Mappers.getMapper(IUserMapStruct.class);

	/**
	 * SysUser转UserQueryVO
	 *
	 * @param sysUser sysUser
	 * @return {@link UserQueryVO }
	 * @author shuigedeng
	 * @since 2021-10-15 17:47:46
	 */
	UserQueryVO sysUserToUserQueryVO(SysUser sysUser);

	/**
	 * SysUser转UserRegisterVO
	 *
	 * @param sysUser sysUser
	 * @return {@link UserRegisterVO }
	 * @author shuigedeng
	 * @since 2021-10-15 17:51:49
	 */
	UserRegisterVO sysUserToAddUserVO(SysUser sysUser);

	/**
	 * list -> SysUser转UserVO
	 *
	 * @param userList userList
	 * @return {@link List&lt;com.taotao.cloud.uc.api.vo.user.UserQueryVO&gt; }
	 * @author shuigedeng
	 * @since 2021-10-15 17:52:04
	 */
	List<UserQueryVO> sysUserToUserQueryVO(List<SysUser> userList);

	/**
	 * copyUserDtoToSysUser
	 *
	 * @param userQueryDTO userQueryDTO
	 * @param user         user
	 * @author shuigedeng
	 * @since 2021-10-15 17:53:08
	 */
	void copyUserDtoToSysUser(UserQueryDTO userQueryDTO, @MappingTarget SysUser user);
}

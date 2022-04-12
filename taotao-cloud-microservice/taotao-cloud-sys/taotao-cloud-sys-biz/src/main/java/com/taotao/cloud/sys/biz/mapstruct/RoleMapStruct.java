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

import com.taotao.cloud.sys.api.bo.role.RoleBO;
import com.taotao.cloud.sys.api.dto.role.RoleQueryDTO;
import com.taotao.cloud.sys.biz.entity.system.Role;
import com.taotao.cloud.sys.api.vo.role.RoleQueryVO;
import com.taotao.cloud.sys.api.vo.user.UserQueryVO;
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
public interface RoleMapStruct {

	RoleMapStruct INSTANCE = Mappers.getMapper(RoleMapStruct.class);

	List<RoleQueryVO> bosToVos(List<RoleBO> bos);

	RoleQueryVO boToVo(RoleBO bo);

	RoleBO roleToBo(Role role);

	List<RoleBO> rolesToBos(List<Role> roles);

	RoleQueryVO sysUserToUserQueryVO(Role sysRole);

	List<RoleQueryVO> sysUserToUserQueryVO(List<Role> roleList);

	void copyUserDtoToSysUser(RoleQueryDTO roleQueryDTO, @MappingTarget Role sysRole);
}

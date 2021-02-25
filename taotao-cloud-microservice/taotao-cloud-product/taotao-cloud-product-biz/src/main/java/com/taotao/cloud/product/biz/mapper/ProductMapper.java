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
package com.taotao.cloud.product.biz.mapper;

import com.taotao.cloud.product.api.vo.ProductVO;
import com.taotao.cloud.product.biz.entity.Product;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author dengtao
 * @date 2020/11/11 14:42
 * @since v1.0
 */
@Mapper(builder = @Builder(disableBuilder = true),
	unmappedSourcePolicy = ReportingPolicy.IGNORE,
	unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

	ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

	/**
	 * product转ProductVO
	 *
	 * @param product product
	 * @return com.taotao.cloud.uc.api.vo.user.UserVO
	 * @author dengtao
	 * @date 2020/11/11 14:47
	 * @since v1.0
	 */
	ProductVO productToProductVO(Product product);

	// /**
	//  * SysUser转AddUserVO
	//  *
	//  * @param sysUser sysUser
	//  * @return com.taotao.cloud.uc.api.vo.user.AddUserVO
	//  * @author dengtao
	//  * @date 2020/11/11 16:59
	//  * @since v1.0
	//  */
	// AddUserVO sysUserToAddUserVO(SysUser sysUser);
	//
	// /**
	//  * list -> SysUser转UserVO
	//  *
	//  * @param userList userList
	//  * @return java.util.List<com.taotao.cloud.uc.api.vo.user.UserVO>
	//  * @author dengtao
	//  * @date 2020/11/11 15:00
	//  * @since v1.0
	//  */
	// List<UserVO> sysUserToUserVO(List<SysUser> userList);
	//
	// /**
	//  * UserDTO转SysUser
	//  *
	//  * @param userDTO userDTO
	//  * @return com.taotao.cloud.uc.biz.entity.SysUser
	//  * @author dengtao
	//  * @date 2020/11/11 14:52
	//  * @since v1.0
	//  */
	// SysUser userDtoToSysUser(UserDTO userDTO);
	//
	// /**
	//  * 拷贝 UserDTO 到SysUser
	//  *
	//  * @param userDTO userDTO
	//  * @param user    user
	//  * @return void
	//  * @author dengtao
	//  * @date 2020/11/11 16:59
	//  * @since v1.0
	//  */
	// void copyUserDtoToSysUser(UserDTO userDTO, @MappingTarget SysUser user);
}

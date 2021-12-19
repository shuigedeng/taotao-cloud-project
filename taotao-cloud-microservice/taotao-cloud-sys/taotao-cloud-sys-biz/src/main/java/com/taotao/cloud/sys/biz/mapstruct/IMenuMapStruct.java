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

import com.taotao.cloud.sys.api.bo.menu.MenuBO;
import com.taotao.cloud.sys.api.bo.menu.MenuQueryBO;
import com.taotao.cloud.sys.api.dto.menu.MenuSaveDTO;
import com.taotao.cloud.sys.api.vo.menu.MenuQueryVO;
import com.taotao.cloud.sys.biz.entity.Menu;
import java.util.List;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/11/11 16:58
 */
@Mapper(builder = @Builder(disableBuilder = true),
	unmappedSourcePolicy = ReportingPolicy.IGNORE,
	unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IMenuMapStruct {

	IMenuMapStruct INSTANCE = Mappers.getMapper(IMenuMapStruct.class);

	List<MenuQueryVO> menuBosToVos(List<MenuBO> bos);

	MenuQueryVO menuBoToVo(MenuBO bo);

	MenuBO menuToBo(Menu menu);

	List<MenuBO> menusToBos(List<Menu> menus);

	Menu dtoToMenu(MenuSaveDTO menuSaveDTO);

	List<MenuQueryBO> entitysToQueryBOs(List<Menu> menus);

	/**
	 * SysMenu转MenuVO
	 *
	 * @param sysMenu sysMenu
	 * @return com.taotao.cloud.sys.api.vo.menu.MenuVO
	 * @author shuigedeng
	 * @version 1.0.0
	 * @since 2020/11/11 17:25
	 */
	MenuQueryVO menuDtoMenuVo(Menu sysMenu);

	/**
	 * list -> SysMenu转MenuVO
	 *
	 * @param menuList userList
	 * @return java.util.List<com.taotao.cloud.sys.api.vo.user.UserVO>
	 * @author shuigedeng
	 * @version 1.0.0
	 * @since 2020/11/11 15:00
	 */
	List<MenuQueryVO> menuToMenuVo(List<Menu> menuList);

	/**
	 * 拷贝 UserDTO 到SysUser
	 *
	 * @param menuSaveDTO menuDTO
	 * @param sysMenu     sysMenu
	 * @return void
	 * @author shuigedeng
	 * @version 1.0.0
	 * @since 2020/11/11 16:59
	 */
	void copyMenuDtoToMenu(MenuSaveDTO menuSaveDTO, @MappingTarget Menu menu);

}

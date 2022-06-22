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
package com.taotao.cloud.sys.biz.mapstruct;

import com.taotao.cloud.sys.api.dubbo.request.MenuQueryRequest;
import com.taotao.cloud.sys.api.dubbo.response.MenuBO;
import com.taotao.cloud.sys.api.vo.menu.MenuQueryVO;
import com.taotao.cloud.sys.api.vo.menu.MenuTreeVO;
import com.taotao.cloud.sys.biz.model.entity.system.Menu;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * imenu地图结构
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 13:39:41
 */
@Mapper(
	unmappedSourcePolicy = ReportingPolicy.IGNORE,
	unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IMenuMapStruct {

	/**
	 * 实例
	 */
	IMenuMapStruct INSTANCE = Mappers.getMapper(IMenuMapStruct.class);

	/**
	 * 菜单列表树vo列表
	 *
	 * @param menuList 菜单列表
	 * @return {@link List }<{@link MenuTreeVO }>
	 * @since 2022-04-28 13:39:41
	 */
	List<MenuTreeVO> menuListToTreeVoList(List<Menu> menuList);

	/**
	 * 实体查询bos
	 *
	 * @param menus 菜单
	 * @return {@link List }<{@link MenuQueryRequest }>
	 * @since 2022-04-28 13:39:41
	 */
	List<MenuQueryRequest> entitysToQueryBOs(List<Menu> menus);

	/**
	 * 菜单bos
	 *
	 * @param menus 菜单
	 * @return {@link List }<{@link MenuBO }>
	 * @since 2022-04-28 13:39:41
	 */
	List<MenuBO> menusToBos(List<Menu> menus);

	/**
	 * 菜单bos vos
	 *
	 * @param bos bos
	 * @return {@link List }<{@link MenuQueryVO }>
	 * @since 2022-04-28 13:39:41
	 */
	List<MenuQueryVO> menuBosToVos(List<MenuBO> bos);

}

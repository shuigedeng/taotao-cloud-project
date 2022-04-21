/*
 * Copyright 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
import com.taotao.cloud.sys.api.vo.menu.MenuQueryVO;
import com.taotao.cloud.sys.api.vo.menu.MenuTreeVO;
import com.taotao.cloud.sys.biz.entity.system.Menu;
import java.util.List;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/11/11 16:58
 */
@Mapper(builder = @Builder(disableBuilder = true),
	unmappedSourcePolicy = ReportingPolicy.IGNORE,
	unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MenuMapStruct {

	MenuMapStruct INSTANCE = Mappers.getMapper(MenuMapStruct.class);

	List<MenuTreeVO> menuListToTreeVoList(List<Menu> menuList);

	List<MenuQueryBO> entitysToQueryBOs(List<Menu> menus);

	List<MenuBO> menusToBos(List<Menu> menus);

	List<MenuQueryVO> menuBosToVos(List<MenuBO> bos);

}

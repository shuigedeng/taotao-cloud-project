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
package com.taotao.cloud.sys.biz.service.impl.dubbo;

import com.taotao.cloud.sys.api.dubbo.IDubboMenuService;
import com.taotao.cloud.sys.api.dubbo.request.MenuQueryRequest;
import com.taotao.cloud.sys.biz.mapper.IMenuMapper;
import com.taotao.cloud.sys.biz.mapstruct.IMenuMapStruct;
import com.taotao.cloud.sys.biz.model.entity.system.Menu;
import com.taotao.cloud.sys.biz.model.entity.system.QMenu;
import com.taotao.cloud.sys.biz.repository.cls.MenuRepository;
import com.taotao.cloud.sys.biz.repository.inf.IMenuRepository;
import com.taotao.cloud.sys.biz.service.IRoleService;
import com.taotao.cloud.web.base.service.BaseSuperServiceImpl;
import java.util.List;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * MenuServiceImpl
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 20:41:23
 */
@Service
@DubboService(interfaceClass = IDubboMenuService.class, validation = "true")
public class DubboMenuServiceImpl extends
	BaseSuperServiceImpl<IMenuMapper, Menu, MenuRepository, IMenuRepository, Long>
	implements IDubboMenuService {

	@Autowired
	private IRoleService sysRoleService;

	private final static QMenu MENU = QMenu.menu;

	@Override
	public List<MenuQueryRequest> queryAllById(Long id) {
		List<Menu> all = ir().findAll();
		return IMenuMapStruct.INSTANCE.entitysToQueryBOs(all);
	}


}

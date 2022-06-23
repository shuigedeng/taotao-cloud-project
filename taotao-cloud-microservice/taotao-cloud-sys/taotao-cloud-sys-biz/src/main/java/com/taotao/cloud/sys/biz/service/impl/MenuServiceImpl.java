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
package com.taotao.cloud.sys.biz.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.sys.api.dubbo.request.MenuQueryRequest;
import com.taotao.cloud.sys.api.dubbo.response.MenuBO;
import com.taotao.cloud.sys.api.dubbo.response.RoleBO;
import com.taotao.cloud.sys.api.dubbo.IDubboMenuService;
import com.taotao.cloud.sys.api.web.vo.menu.MenuQueryVO;
import com.taotao.cloud.sys.api.web.vo.menu.MenuTreeVO;
import com.taotao.cloud.sys.biz.model.entity.system.Menu;
import com.taotao.cloud.sys.biz.mapper.IMenuMapper;
import com.taotao.cloud.sys.biz.mapstruct.IMenuMapStruct;
import com.taotao.cloud.sys.biz.model.entity.system.QMenu;
import com.taotao.cloud.sys.biz.repository.cls.MenuRepository;
import com.taotao.cloud.sys.biz.repository.inf.IMenuRepository;
import com.taotao.cloud.sys.biz.service.IMenuService;
import com.taotao.cloud.sys.biz.service.IRoleService;
import com.taotao.cloud.sys.biz.utils.TreeUtil;
import com.taotao.cloud.web.base.service.BaseSuperServiceImpl;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * MenuServiceImpl
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 20:41:23
 */
@Service
@DubboService(interfaceClass = IDubboMenuService.class)
public class MenuServiceImpl extends
	BaseSuperServiceImpl<IMenuMapper, Menu, MenuRepository, IMenuRepository, Long>
	implements IDubboMenuService, IMenuService {

	@Autowired
	private IRoleService sysRoleService;

	//private final IFeignOrderItemService IFeignOrderItemService;
	//private final IFeignOrderService IFeignOrderService;
	//
	//@DubboReference(validation = "true")
	//private IDubboOrderService dubboOrderService;
	//
	//public MenuServiceImpl(IRoleService sysRoleService,
	//	IFeignOrderItemService IFeignOrderItemService,
	//	IFeignOrderService IFeignOrderService) {
	//	this.IFeignOrderItemService = IFeignOrderItemService;
	//	this.IFeignOrderService = IFeignOrderService;
	//	this.sysRoleService = sysRoleService;
	//}

	private final static QMenu MENU = QMenu.menu;

	@Override
	public List<MenuBO> findMenuByIdList(List<Long> idList) {
		List<Menu> menus = cr().findAllById(idList);
		return IMenuMapStruct.INSTANCE.menusToBos(menus);
	}

	@Override
	public List<MenuBO> findAllMenus() {
		List<Menu> menus = ir().findAll();
		return IMenuMapStruct.INSTANCE.menusToBos(menus);
	}

	@Override
	public List<MenuQueryRequest> findAllById(Long id) {
		List<Menu> menus = ir().findAll();
		return IMenuMapStruct.INSTANCE.entitysToQueryBOs(menus);
	}

	@Override
	public List<MenuBO> findMenuByRoleIds(Set<Long> roleIds) {
		List<Menu> menus = im().findMenuByRoleIds(roleIds);
		return IMenuMapStruct.INSTANCE.menusToBos(menus)
			.stream()
			.sorted(Comparator.comparing(MenuBO::id))
			.toList();
	}

	@Override
	public List<MenuBO> findMenuByCodes(Set<String> codes) {
		List<RoleBO> sysRoles = sysRoleService.findRoleByCodes(codes);
		if (CollUtil.isEmpty(sysRoles)) {
			throw new BusinessException("未查询到角色信息");
		}
		List<Long> roleIds = sysRoles.stream().map(RoleBO::id).toList();
		return findMenuByRoleIds(new HashSet<>(roleIds));
	}

	@Override
	public List<MenuBO> findMenuByParentId(Long parentId) {
		List<Long> pidList = new ArrayList<>();
		pidList.add(parentId);
		List<Long> sumList = new ArrayList<>();
		List<Long> allChildrenIdList = recursion(pidList, sumList);
		return findMenuByIdList(allChildrenIdList);
	}

	/**
	 * 根据parentId递归查询
	 *
	 * @param pidList 初始化的父级ID
	 * @param sumList 保存的全部ID
	 * @return {@link List&lt;java.lang.Long&gt; }
	 * @since 2021-10-09 20:41:41
	 */
	public List<Long> recursion(List<Long> pidList, List<Long> sumList) {
		List<Long> sonIdList = im().selectIdList(pidList);
		if (sonIdList.size() == 0) {
			return sumList;
		}
		sumList.addAll(sonIdList);
		return recursion(sonIdList, sumList);
	}

	@Override
	public List<MenuTreeVO> findMenuTree(boolean lazy, Long parentId) {
		if (!lazy) {
			List<MenuBO> bos = findAllMenus();
			return TreeUtil.buildTree(bos, CommonConstant.MENU_TREE_ROOT_ID);
		}

		Long parent = parentId == null ? CommonConstant.MENU_TREE_ROOT_ID : parentId;
		List<MenuBO> bos = findMenuByParentId(parent);
		return TreeUtil.buildTree(bos, parent);
	}

	@Override
	public List<MenuTreeVO> findCurrentUserMenuTree(List<MenuQueryVO> vos,
		Long parentId) {
		//List<MenuTreeVO> menuTreeList = vos.stream()
		//	.filter(vo -> MenuTypeEnum.DIR.getCode() == vo.type())
		//	.map(e -> MenuTreeVO.builder()
		//		.id(e.id())
		//		.name(e.name())
		//		.title(e.name())
		//		.key(e.id())
		//		.value(e.id())
		//		// 此处还需要设置其他属性
		//		.build())
		//	.sorted(Comparator.comparingInt(MenuTreeVO::getSort))
		//	.collect(Collectors.toList());

		//Long parent = parentId == null ? CommonConstant.MENU_TREE_ROOT_ID : parentId;
		//return TreeUtil.build(menuTreeList, parent);
		return null;
		//return ForestNodeMerger.merge(TreeUtil.buildTree(menus));
	}

	@Override
	public List<MenuQueryRequest> queryAllById(Long id) {
		List<Menu> all = ir().findAll();
		return IMenuMapStruct.INSTANCE.entitysToQueryBOs(all);
	}

	//@Override
	//@Transactional(rollbackFor = Exception.class)
	//@GlobalTransactional(name = "testSeata", rollbackFor = Exception.class)
	//public Boolean testSeata() {
	//	//try {
	//	//LogUtil.info("1.添加菜单信息");
	//	//Menu sysMenu = Menu.builder()
	//	//	.name("菜单三")
	//	//	.type((byte) 1)
	//	//	.parentId(0L)
	//	//	.sortNum(2)
	//	//	.build();
	//	//saveMenu(sysMenu);
	//
	//	//String traceId = TraceContext.traceId();
	//	//LogUtil.info("skywalking traceid ===> {0}", traceId);
	//	//
	//	//LogUtil.info("1.远程添加订单信息");
	//	//OrderDTO orderDTO = OrderDTO.builder()
	//	//	.memberId(2L)
	//	//	.code("33332")
	//	//	.amount(BigDecimal.ZERO)
	//	//	.mainStatus(1)
	//	//	.childStatus(1)
	//	//	.receiverName("shuigedeng")
	//	//	.receiverPhone("15730445330")
	//	//	.receiverAddressJson("sjdlasjdfljsldf")
	//	//	.build();
	//
	//	//Result<OrderVO> orderVOResult = remoteOrderService.saveOrder(orderDTO);
	//	//if(orderVOResult.getCode() != 200){
	//	//	throw new BusinessException("创建订单失败");
	//	//}
	//	//LogUtil.info("OrderVO ===> {0}", orderVOResult);
	//
	//	//OrderVO orderVO = iOrderInfoService.saveOrder(orderDTO);
	//	//LogUtil.info("OrderVO ====> {}", orderVO);
	//
	//	//} catch (Exception e) {
	//	//	try {
	//	//		GlobalTransactionContext.reload(RootContext.getXID()).rollback();
	//	//	} catch (TransactionException ex) {
	//	//		ex.printStackTrace();
	//	//	}
	//	//}
	//
	//	//Menu MenuById = findMenuById(37L);
	//	//LogUtil.info("MenuById ======> ", MenuById);
	//	//
	//	//OrderVO orderInfoByCode = iOrderInfoService.findOrderInfoByCode("33333");
	//	//LogUtil.info("OrderVO ====> {}", orderInfoByCode);
	//	//
	//	//Result<OrderVO> orderInfoByCode1 = remoteOrderService.findOrderInfoByCode("33333");
	//	//LogUtil.info("OrderVO ====> {}", orderInfoByCode1);
	//
	//	return true;
	//}

	//@Async
	//@Override
	//public Future<Boolean> testAsync() {
	//	long start = sY.currentTimeMillis();
	//
	//	try {
	//		//Menu MenuById = findMenuById(51L);
	//		//LogUtil.info("MenuById ======> ", MenuById);
	//
	//		Thread.sleep(3000);
	//	} catch (InterruptedException e) {
	//		e.printStackTrace();
	//	}
	//
	//	LogUtil.info("MenuById *********************************");
	//
	//	long end = tem.currentTimeMillis();
	//
	//	LogUtil.info(Thread.currentThread().getName() + "======" + (end - start));
	//	return AsyncResult.forValue(true);
	//}


}

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
package com.taotao.cloud.uc.biz.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.enums.ResourceTypeEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.order.api.feign.IFeignOrderItemService;
import com.taotao.cloud.order.api.feign.IFeignOrderService;
import com.taotao.cloud.order.api.dubbo.IDubboOrderService;
import com.taotao.cloud.uc.api.bo.resource.ResourceBO;
import com.taotao.cloud.uc.api.bo.role.RoleBO;
import com.taotao.cloud.uc.api.dubbo.IDubboResourceService;
import com.taotao.cloud.uc.api.vo.resource.ResourceQueryBO;
import com.taotao.cloud.uc.api.vo.resource.ResourceQueryVO;
import com.taotao.cloud.uc.api.vo.resource.ResourceTreeVO;
import com.taotao.cloud.uc.biz.entity.QSysResource;
import com.taotao.cloud.uc.biz.entity.SysResource;
import com.taotao.cloud.uc.biz.entity.SysRole;
import com.taotao.cloud.uc.biz.mapper.ISysResourceMapper;
import com.taotao.cloud.uc.biz.mapstruct.IResourceMapStruct;
import com.taotao.cloud.uc.biz.repository.inf.ISysResourceRepository;
import com.taotao.cloud.uc.biz.repository.cls.SysResourceRepository;
import com.taotao.cloud.uc.api.dubbo.IDubboRoleService;
import com.taotao.cloud.uc.biz.service.ISysResourceService;
import com.taotao.cloud.uc.biz.service.ISysRoleService;
import com.taotao.cloud.uc.biz.utils.TreeUtil;
import com.taotao.cloud.web.base.service.BaseSuperServiceImpl;
import io.seata.spring.annotation.GlobalTransactional;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * SysResourceServiceImpl
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 20:41:23
 */
@Service
@DubboService(interfaceClass = IDubboResourceService.class)
public class SysResourceServiceImpl extends
	BaseSuperServiceImpl<ISysResourceMapper, SysResource, SysResourceRepository, ISysResourceRepository, Long>
	implements IDubboResourceService, ISysResourceService<SysResource, Long> {

	private final ISysRoleService<SysRole, Long> sysRoleService;

	private final IFeignOrderItemService IFeignOrderItemService;
	private final IFeignOrderService IFeignOrderService;

	@DubboReference
	private IDubboOrderService dubboOrderService;

	public SysResourceServiceImpl(ISysRoleService<SysRole, Long> sysRoleService,
		IFeignOrderItemService IFeignOrderItemService,
		IFeignOrderService IFeignOrderService) {
		this.IFeignOrderItemService = IFeignOrderItemService;
		this.IFeignOrderService = IFeignOrderService;
		this.sysRoleService = sysRoleService;
	}

	private final static QSysResource SYS_RESOURCE = QSysResource.sysResource;

	@Override
	public List<ResourceBO> findResourceByIdList(List<Long> idList) {

		List<SysResource> resources = cr().findResourceByIdList(idList);
		return IResourceMapStruct.INSTANCE.resourcesToBos(resources);
	}

	@Override
	public List<ResourceBO> findAllResources() {
		List<SysResource> resources = ir().findAll();
		return IResourceMapStruct.INSTANCE.resourcesToBos(resources);
	}

	@Override
	public List<ResourceQueryBO> findAllById(Long id) {
		List<SysResource> resources = ir().findAll();
		return IResourceMapStruct.INSTANCE.entitysToQueryBOs(resources);
	}

	@Override
	public List<ResourceBO> findResourceByRoleIds(Set<Long> roleIds) {
		List<SysResource> resources = cr().findResourceByRoleIds(roleIds);
		return IResourceMapStruct.INSTANCE.resourcesToBos(resources)
			.stream()
			.sorted(Comparator.comparing(ResourceBO::id))
			.toList();
	}

	@Override
	public List<ResourceBO> findResourceByCodes(Set<String> codes) {
		List<RoleBO> sysRoles = sysRoleService.findRoleByCodes(codes);
		if (CollUtil.isEmpty(sysRoles)) {
			throw new BusinessException("未查询到角色信息");
		}
		List<Long> roleIds = sysRoles.stream().map(RoleBO::id).toList();
		return findResourceByRoleIds(new HashSet<>(roleIds));
	}

	@Override
	public List<ResourceBO> findResourceByParentId(Long parentId) {
		List<Long> pidList = new ArrayList<>();
		pidList.add(parentId);
		List<Long> sumList = new ArrayList<>();
		List<Long> allChildrenIdList = recursion(pidList, sumList);
		return findResourceByIdList(allChildrenIdList);
	}

	/**
	 * 根据parentId递归查询
	 *
	 * @param pidList 初始化的父级ID
	 * @param sumList 保存的全部ID
	 * @return {@link List&lt;java.lang.Long&gt; }
	 * @author shuigedeng
	 * @since 2021-10-09 20:41:41
	 */
	public List<Long> recursion(List<Long> pidList, List<Long> sumList) {
		List<Long> sonIdList = cr().selectIdList(pidList);
		if (sonIdList.size() == 0) {
			return sumList;
		}
		sumList.addAll(sonIdList);
		return recursion(sonIdList, sumList);
	}

	@Override
	public List<ResourceTreeVO> findResourceTree(boolean lazy, Long parentId) {
		if (!lazy) {
			List<ResourceBO> resources = findAllResources();
			return TreeUtil.buildTree(resources, CommonConstant.RESOURCE_TREE_ROOT_ID);
		}

		Long parent = parentId == null ? CommonConstant.RESOURCE_TREE_ROOT_ID : parentId;
		List<ResourceBO> resources = findResourceByParentId(parent);
		return TreeUtil.buildTree(resources, parent);
	}

	@Override
	public List<ResourceTreeVO> findCurrentUserResourceTree(List<ResourceQueryVO> resourceVOList,
		Long parentId) {
		List<ResourceTreeVO> menuTreeList = resourceVOList.stream()
			.filter(vo -> ResourceTypeEnum.LEFT_MENU.getCode() == vo.type())
			.map(ResourceTreeVO::new).sorted(Comparator.comparingInt(ResourceTreeVO::getSort))
			.collect(Collectors.toList());
		Long parent = parentId == null ? CommonConstant.RESOURCE_TREE_ROOT_ID : parentId;
		return TreeUtil.build(menuTreeList, parent);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	@GlobalTransactional(name = "testSeata", rollbackFor = Exception.class)
	public Boolean testSeata() {
		//try {
		//LogUtil.info("1.添加资源信息");
		//SysResource sysResource = SysResource.builder()
		//	.name("资源三")
		//	.type((byte) 1)
		//	.parentId(0L)
		//	.sortNum(2)
		//	.build();
		//saveResource(sysResource);

		//String traceId = TraceContext.traceId();
		//LogUtil.info("skywalking traceid ===> {0}", traceId);
		//
		//LogUtil.info("1.远程添加订单信息");
		//OrderDTO orderDTO = OrderDTO.builder()
		//	.memberId(2L)
		//	.code("33332")
		//	.amount(BigDecimal.ZERO)
		//	.mainStatus(1)
		//	.childStatus(1)
		//	.receiverName("shuigedeng")
		//	.receiverPhone("15730445330")
		//	.receiverAddressJson("sjdlasjdfljsldf")
		//	.build();

		//Result<OrderVO> orderVOResult = remoteOrderService.saveOrder(orderDTO);
		//if(orderVOResult.getCode() != 200){
		//	throw new BusinessException("创建订单失败");
		//}
		//LogUtil.info("OrderVO ===> {0}", orderVOResult);

		//OrderVO orderVO = iOrderInfoService.saveOrder(orderDTO);
		//LogUtil.info("OrderVO ====> {}", orderVO);

		//} catch (Exception e) {
		//	try {
		//		GlobalTransactionContext.reload(RootContext.getXID()).rollback();
		//	} catch (TransactionException ex) {
		//		ex.printStackTrace();
		//	}
		//}

		//SysResource resourceById = findResourceById(37L);
		//LogUtil.info("resourceById ======> ", resourceById);
		//
		//OrderVO orderInfoByCode = iOrderInfoService.findOrderInfoByCode("33333");
		//LogUtil.info("OrderVO ====> {}", orderInfoByCode);
		//
		//Result<OrderVO> orderInfoByCode1 = remoteOrderService.findOrderInfoByCode("33333");
		//LogUtil.info("OrderVO ====> {}", orderInfoByCode1);

		return true;
	}

	@Async
	@Override
	public Future<Boolean> testAsync() {
		long start = System.currentTimeMillis();

		try {
			//SysResource resourceById = findResourceById(51L);
			//LogUtil.info("resourceById ======> ", resourceById);

			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		LogUtil.info("resourceById *********************************");

		long end = System.currentTimeMillis();

		LogUtil.info(Thread.currentThread().getName() + "======" + (end - start));
		return AsyncResult.forValue(true);
	}

	@Override
	public ResourceQueryBO queryAllId(Long id) {
		return null;
	}
}

package com.taotao.cloud.uc.biz.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.enums.ResourceTypeEnum;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.order.api.dto.OrderDTO;
import com.taotao.cloud.order.api.feign.RemoteOrderItemService;
import com.taotao.cloud.order.api.feign.RemoteOrderService;
import com.taotao.cloud.order.api.service.IOrderInfoService;
import com.taotao.cloud.order.api.vo.OrderVO;
import com.taotao.cloud.uc.api.query.resource.ResourcePageQuery;
import com.taotao.cloud.uc.api.vo.resource.ResourceTree;
import com.taotao.cloud.uc.api.vo.resource.ResourceVO;
import com.taotao.cloud.uc.biz.entity.QSysResource;
import com.taotao.cloud.uc.biz.entity.SysResource;
import com.taotao.cloud.uc.biz.entity.SysRole;
import com.taotao.cloud.uc.biz.repository.SysResourceRepository;
import com.taotao.cloud.uc.biz.service.ISysResourceService;
import com.taotao.cloud.uc.biz.service.ISysRoleService;
import com.taotao.cloud.uc.biz.utils.TreeUtil;
import com.zaxxer.hikari.HikariDataSource;
import io.seata.spring.annotation.GlobalTransactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import net.logstash.logback.appender.LogstashTcpSocketAppender;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//import com.taotao.cloud.order.api.dto.OrderDTO;
//import com.taotao.cloud.order.api.feign.RemoteOrderService;
//import com.taotao.cloud.order.api.vo.OrderVO;
//import com.taotao.cloud.product.api.dto.ProductDTO;
//import com.taotao.cloud.product.api.feign.RemoteProductService;

/**
 * 资源表服务实现类
 *
 * @author shuigedeng
 * @since 2020-10-16 16:23:05
 * @since 1.0
 */
@Service
@DubboService
public class SysResourceServiceImpl implements ISysResourceService {

	private final SysResourceRepository sysResourceRepository;
	private final ISysRoleService sysRoleService;
	private final RemoteOrderItemService remoteOrderItemService;
	private final RemoteOrderService remoteOrderService;

	@DubboReference
	private IOrderInfoService iOrderInfoService;

	public SysResourceServiceImpl(
		SysResourceRepository sysResourceRepository,
		ISysRoleService sysRoleService,
		RemoteOrderItemService remoteOrderItemService,
		RemoteOrderService remoteOrderService) {
		this.sysResourceRepository = sysResourceRepository;
		this.sysRoleService = sysRoleService;
		this.remoteOrderItemService = remoteOrderItemService;
		this.remoteOrderService = remoteOrderService;
	}

	private final static QSysResource SYS_RESOURCE = QSysResource.sysResource;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public SysResource saveResource(SysResource resource) {
		String name = resource.getName();
		Boolean isExists = existsByName(name);
		if (isExists) {
			throw new BusinessException(ResultEnum.RESOURCE_NAME_EXISTS_ERROR);
		}
		return sysResourceRepository.saveAndFlush(resource);
	}

	@Override
	public Boolean deleteResource(Long id) {
		sysResourceRepository.deleteById(id);
		// 需要级联删除 子资源

		//
		return true;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public SysResource updateResource(SysResource sysResource) {
		return sysResourceRepository.saveAndFlush(sysResource);
	}

	@Override
	public SysResource findResourceById(Long id) {
		Optional<SysResource> optionalSysResource = sysResourceRepository.findById(id);
		return optionalSysResource.orElseThrow(
			() -> new BusinessException(ResultEnum.RESOURCE_NOT_EXIST));
	}

	@Override
	public SysResource findResourceByName(String name) {
		Optional<SysResource> optionalSysResource = sysResourceRepository.findResourceByName(name);
		return optionalSysResource.orElseThrow(
			() -> new BusinessException(ResultEnum.RESOURCE_NOT_EXIST));
	}

	@Override
	public Boolean existsById(Long id) {
		BooleanExpression predicate = SYS_RESOURCE.id.eq(id);
		return sysResourceRepository.exists(predicate);
	}

	@Override
	public Boolean existsByName(String name) {
		BooleanExpression predicate = SYS_RESOURCE.name.eq(name);
		return sysResourceRepository.exists(predicate);
	}


	@Override
	public List<SysResource> findResourceByIdList(List<Long> idList) {
		return sysResourceRepository.findResourceByIdList(idList);
	}

	@Override
	public Page<SysResource> findResourcePage(Pageable pageable, ResourcePageQuery resourceQuery) {
		BooleanExpression expression = SYS_RESOURCE.delFlag.eq(false);
		OrderSpecifier<LocalDateTime> desc = SYS_RESOURCE.createTime.desc();
		return sysResourceRepository.findAll(expression, pageable, desc);
	}

	@Override
	public List<SysResource> findAllResources() {
		return sysResourceRepository.findAll();
	}

	@Override
	public List<SysResource> findResourceByRoleIds(Set<Long> roleIds) {
		List<SysResource> sysResources = sysResourceRepository.findResourceByRoleIds(roleIds);
		return sysResources.stream().collect(Collectors.collectingAndThen(
			Collectors.toCollection(
				() -> new TreeSet<>(Comparator.comparing(SysResource::getId))), ArrayList::new));
	}

	@Override
	public List<SysResource> findResourceByCodes(Set<String> codes) {
		List<SysRole> sysRoles = sysRoleService.findRoleByCodes(codes);
		if (CollUtil.isEmpty(sysRoles)) {
			throw new BusinessException("未查询到角色信息");
		}
		List<Long> roleIds = sysRoles.stream().map(SysRole::getId).collect(Collectors.toList());
		return findResourceByRoleIds(new HashSet<>(roleIds));
	}

	@Override
	public List<SysResource> findResourceByParentId(Long parentId) {
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
	 * @return java.util.List<java.lang.Long>
	 * @author shuigedeng
	 * @version 1.0.0
	 * @since 2020/11/11 16:48
	 */
	public List<Long> recursion(List<Long> pidList, List<Long> sumList) {
		List<Long> sonIdList = sysResourceRepository.selectIdList(pidList);
		if (sonIdList.size() == 0) {
			return sumList;
		}
		sumList.addAll(sonIdList);
		return recursion(sonIdList, sumList);
	}

	@Override
	public List<ResourceTree> findResourceTree(boolean lazy, Long parentId) {
		if (!lazy) {
			List<SysResource> resources = findAllResources();
			return TreeUtil.buildTree(resources, CommonConstant.RESOURCE_TREE_ROOT_ID);
		}

		Long parent = parentId == null ? CommonConstant.RESOURCE_TREE_ROOT_ID : parentId;
		List<SysResource> resources = findResourceByParentId(parent);
		return TreeUtil.buildTree(resources, parent);
	}

	@Override
	public List<ResourceTree> findCurrentUserResourceTree(List<ResourceVO> resourceVOList,
		Long parentId) {
		List<ResourceTree> menuTreeList = resourceVOList.stream()
			.filter(vo -> ResourceTypeEnum.LEFT_MENU.getCode() == vo.getType())
			.map(ResourceTree::new).sorted(Comparator.comparingInt(ResourceTree::getSort))
			.collect(Collectors.toList());
		Long parent = parentId == null ? CommonConstant.RESOURCE_TREE_ROOT_ID : parentId;
		return TreeUtil.build(menuTreeList, parent);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	@GlobalTransactional(name = "testSeata", rollbackFor = Exception.class)
	public Boolean testSeata() {
		LogstashTcpSocketAppender appender = new LogstashTcpSocketAppender();
		appender.getWriteBufferSize()


		//try {
		LogUtil.info("1.添加资源信息");
		SysResource sysResource = SysResource.builder()
			.name("资源三")
			.type((byte) 1)
			.parentId(0L)
			.sortNum(2)
			.build();
		saveResource(sysResource);

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
			SysResource resourceById = findResourceById(51L);
			LogUtil.info("resourceById ======> ", resourceById);

			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		long end = System.currentTimeMillis();

		LogUtil.info(Thread.currentThread().getName() + "======" + (end - start));
		return AsyncResult.forValue(true);
	}
}

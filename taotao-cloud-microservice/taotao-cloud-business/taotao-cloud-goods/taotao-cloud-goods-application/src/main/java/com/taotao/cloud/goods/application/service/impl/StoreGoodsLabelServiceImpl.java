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

package com.taotao.cloud.goods.application.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.taotao.boot.cache.redis.repository.RedisRepository;
import com.taotao.boot.common.enums.ResultEnum;
import com.taotao.boot.common.exception.BusinessException;
import com.taotao.cloud.goods.application.command.store.dto.clientobject.StoreGoodsLabelCO;
import com.taotao.cloud.goods.application.service.IStoreGoodsLabelService;
import com.taotao.cloud.goods.infrastructure.persistent.mapper.IStoreGoodsLabelMapper;
import com.taotao.cloud.goods.infrastructure.persistent.po.StoreGoodsLabelPO;
import com.taotao.cloud.goods.infrastructure.persistent.repository.cls.StoreGoodsLabelRepository;
import com.taotao.cloud.goods.infrastructure.persistent.repository.inf.IStoreGoodsLabelRepository;
import com.taotao.boot.security.spring.utils.SecurityUtils;
import com.taotao.boot.webagg.service.impl.BaseSuperServiceImpl;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 店铺商品分类业务层实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:02:58
 */
@Service
public class StoreGoodsLabelServiceImpl
	extends
	BaseSuperServiceImpl<StoreGoodsLabelPO, Long, IStoreGoodsLabelMapper, StoreGoodsLabelRepository, IStoreGoodsLabelRepository>
	implements IStoreGoodsLabelService {

	/**
	 * 缓存
	 */
	@Autowired
	private RedisRepository redisRepository;

	@Override
	public List<StoreGoodsLabelCO> listByStoreId(Long storeId) {
		// 从缓存中获取店铺分类
		if (redisRepository.hasKey(CachePrefix.STORE_CATEGORY.getPrefix() + storeId)) {
			return (List<StoreGoodsLabelCO>) redisRepository.get(
				CachePrefix.STORE_CATEGORY.getPrefix() + storeId);
		}

		List<StoreGoodsLabelPO> list = list(storeId);
		List<StoreGoodsLabelCO> storeGoodsLabelCOList = new ArrayList<>();

		// 循环列表判断是否为顶级，如果为顶级获取下级数据
		list.stream().filter(storeGoodsLabel -> storeGoodsLabel.getLevel() == 0)
			.forEach(storeGoodsLabel -> {
				StoreGoodsLabelCO storeGoodsLabelCO = new StoreGoodsLabelCO(
					storeGoodsLabel.getId(),
					storeGoodsLabel.getLabelName(),
					storeGoodsLabel.getLevel(),
					storeGoodsLabel.getSortOrder());
				List<StoreGoodsLabelCO> storeGoodsLabelCOChildList = new ArrayList<>();
				list.stream()
					.filter(label -> label.getParentId().equals(storeGoodsLabel.getId()))
					.forEach(storeGoodsLabelChild -> storeGoodsLabelCOChildList.add(
						new StoreGoodsLabelCO(
							storeGoodsLabelChild.getId(),
							storeGoodsLabelChild.getLabelName(),
							storeGoodsLabelChild.getLevel(),
							storeGoodsLabelChild.getSortOrder())));
				storeGoodsLabelCO.setChildren(storeGoodsLabelCOChildList);
				storeGoodsLabelCOList.add(storeGoodsLabelCO);
			});

		// 调整店铺分类排序
		storeGoodsLabelCOList.sort(Comparator.comparing(StoreGoodsLabelCO::getSortOrder));

		if (!storeGoodsLabelCOList.isEmpty()) {
			redisRepository.set(CachePrefix.CATEGORY.getPrefix() + storeId + "tree",
				storeGoodsLabelCOList);
		}
		return storeGoodsLabelCOList;
	}

	/**
	 * 根据分类id集合获取所有店铺分类根据层级排序
	 *
	 * @param ids 商家ID
	 * @return 店铺分类列表
	 */
	@Override
	public List<StoreGoodsLabelPO> listByStoreIds(List<Long> ids) {
		return this.list(new LambdaQueryWrapper<StoreGoodsLabelPO>()
			.in(StoreGoodsLabelPO::getId, ids)
			.orderByAsc(StoreGoodsLabelPO::getLevel));
	}

	@Override
	public List<StoreGoodsLabelCO> listByStoreId(String storeId) {
		return List.of();
	}

	@Override
	public List<StoreGoodsLabelPO> listByStoreIds(List<String> ids) {
		return List.of();
	}

	@Override
	public List<Map<String, Object>> listMapsByStoreIds(List<String> ids, String columns) {
		return List.of();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean addStoreGoodsLabel(StoreGoodsLabelPO storeGoodsLabelPO) {
		// 获取当前登录商家账号
		SecurityUser tokenUser = SecurityUtils.getCurrentUser();
		storeGoodsLabelPO.setStoreId(tokenUser.getStoreId());
		// 保存店铺分类
		this.save(storeGoodsLabelPO);
		// 清除缓存
		removeCache(storeGoodsLabelPO.getStoreId());
		return true;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean editStoreGoodsLabel(StoreGoodsLabelPO storeGoodsLabelPO) {
		// 修改当前店铺的商品分类
		SecurityUser tokenUser = SecurityUtils.getCurrentUser();

		LambdaUpdateWrapper<StoreGoodsLabelPO> lambdaUpdateWrapper = Wrappers.lambdaUpdate();
		lambdaUpdateWrapper.eq(StoreGoodsLabelPO::getStoreId, tokenUser.getStoreId());
		lambdaUpdateWrapper.eq(StoreGoodsLabelPO::getId, storeGoodsLabelPO.getId());
		// 修改店铺分类
		this.update(storeGoodsLabelPO, lambdaUpdateWrapper);
		// 清除缓存
		removeCache(storeGoodsLabelPO.getStoreId());
		return true;
	}

	@Override
	public void removeStoreGoodsLabel(String storeLabelId) {

	}

	@Override
	public boolean removeStoreGoodsLabel(Long storeLabelId) {
		SecurityUser tokenUser = SecurityUtils.getCurrentUser();
		if (tokenUser == null || Objects.isNull(tokenUser.getStoreId())) {
			throw new BusinessException(ResultEnum.USER_NOT_LOGIN);
		}
		// 删除店铺分类
		this.removeById(storeLabelId);

		// 清除缓存
		removeCache(tokenUser.getStoreId());

		return true;
	}

	/**
	 * 获取店铺商品分类列表
	 *
	 * @param storeId 店铺ID
	 * @return 店铺商品分类列表
	 */
	private List<StoreGoodsLabelPO> list(Long storeId) {
		LambdaQueryWrapper<StoreGoodsLabelPO> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.eq(StoreGoodsLabelPO::getStoreId, storeId);
		queryWrapper.orderByDesc(StoreGoodsLabelPO::getSortOrder);
		return this.baseMapper.selectList(queryWrapper);
	}

	/**
	 * 清除缓存
	 */
	private void removeCache(Long storeId) {
		redisRepository.del(CachePrefix.STORE_CATEGORY.getPrefix() + storeId);
	}
}

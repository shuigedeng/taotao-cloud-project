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

package com.taotao.cloud.store.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.utils.bean.BeanUtils;
import com.taotao.cloud.security.springsecurity.utils.SecurityUtils;
import com.taotao.cloud.goods.api.feign.IFeignGoodsApi;
import com.taotao.cloud.member.api.feign.IFeignMemberApi;
import com.taotao.cloud.member.api.model.vo.MemberVO;
import com.taotao.cloud.store.api.enums.StoreStatusEnum;
import com.taotao.cloud.store.api.model.dto.*;
import com.taotao.cloud.store.api.model.query.StorePageQuery;
import com.taotao.cloud.store.api.model.vo.StoreVO;
import com.taotao.cloud.store.biz.mapper.StoreMapper;
import com.taotao.cloud.store.biz.model.entity.Store;
import com.taotao.cloud.store.biz.model.entity.StoreDetail;
import com.taotao.cloud.store.biz.service.IStoreDetailService;
import com.taotao.cloud.store.biz.service.IStoreService;
import com.taotao.cloud.store.biz.utils.QueryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 店铺业务层实现
 *
 * @since 2020-03-07 16:18:56
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class StoreServiceImpl extends ServiceImpl<StoreMapper, Store> implements IStoreService {

	/**
	 * 会员
	 */
	@Autowired
	private IFeignMemberApi memberApi;
	/**
	 * 商品
	 */
	@Autowired
	private IFeignGoodsApi goodsApi;
	/**
	 * 店铺详情
	 */
	@Autowired
	private IStoreDetailService storeDetailService;

	@Override
	public IPage<StoreVO> findByConditionPage(StorePageQuery storePageQuery) {
		return this.baseMapper.getStoreList(storePageQuery.buildMpPage(), QueryUtil.queryWrapper(storePageQuery));
	}

	@Override
	public StoreVO getStoreDetail() {
		Long storeId = SecurityUtils.getCurrentUser().getStoreId();
		StoreVO storeVO = this.baseMapper.getStoreDetail(storeId);
		storeVO.setNickname(SecurityUtils.getCurrentUser().getNickname());
		return storeVO;
	}

	@Override
	public Store add(AdminStoreApplyDTO adminStoreApplyDTO) {
		// 判断店铺名称是否存在
		QueryWrapper<Store> queryWrapper = Wrappers.query();
		queryWrapper.eq("store_name", adminStoreApplyDTO.getStoreName());
		if (this.getOne(queryWrapper) != null) {
			throw new BusinessException(ResultEnum.STORE_NAME_EXIST_ERROR);
		}

		MemberVO member = memberApi.getById(adminStoreApplyDTO.getMemberId());
		// 判断用户是否存在
		if (member == null) {
			throw new BusinessException(ResultEnum.USER_NOT_EXIST);
		}
		// 判断是否拥有店铺
		if (Boolean.TRUE.equals(member.getHaveStore())) {
			throw new BusinessException(ResultEnum.STORE_APPLY_DOUBLE_ERROR);
		}

		// 添加店铺
		Store store = new Store(member, adminStoreApplyDTO);
		this.save(store);

		// 判断是否存在店铺详情，如果没有则进行新建，如果存在则进行修改
		StoreDetail storeDetail = new StoreDetail(store, adminStoreApplyDTO);

		storeDetailService.save(storeDetail);

		// 设置会员-店铺信息
		// memberApi.update(member.getId(), store.getId());
		return store;
	}

	@Override
	public Store edit(StoreEditDTO storeEditDTO) {
		if (storeEditDTO != null) {
			// 判断店铺名是否唯一
			Store storeTmp = getOne(new QueryWrapper<Store>().eq("store_name", storeEditDTO.getStoreName()));
			if (storeTmp != null && !storeTmp.getId().equals(storeEditDTO.getStoreId())) {
				throw new BusinessException(ResultEnum.STORE_NAME_EXIST_ERROR);
			}
			// 修改店铺详细信息
			updateStoreDetail(storeEditDTO);
			// 修改店铺信息
			return updateStore(storeEditDTO);
		} else {
			throw new BusinessException(ResultEnum.STORE_NOT_EXIST);
		}
	}

	/**
	 * 修改店铺基本信息
	 *
	 * @param storeEditDTO 修改店铺信息
	 */
	private Store updateStore(StoreEditDTO storeEditDTO) {
		Store store = this.getById(storeEditDTO.getStoreId());
		if (store != null) {
			BeanUtils.copyProperties(storeEditDTO, store);
			// store.setId(storeEditDTO.getStoreId());
			boolean result = this.updateById(store);
			if (result) {
				storeDetailService.updateStoreGoodsInfo(store);
			}
		}
		return store;
	}

	/**
	 * 修改店铺详细细腻
	 *
	 * @param storeEditDTO 修改店铺信息
	 */
	private void updateStoreDetail(StoreEditDTO storeEditDTO) {
		StoreDetail storeDetail = new StoreDetail();
		BeanUtils.copyProperties(storeEditDTO, storeDetail);
		storeDetailService.update(
			storeDetail, new QueryWrapper<StoreDetail>().eq("store_id", storeEditDTO.getStoreId()));
	}

	@Override
	public boolean audit(String id, Integer passed) {
		Store store = this.getById(id);
		if (store == null) {
			throw new BusinessException(ResultEnum.STORE_NOT_EXIST);
		}
		if (passed == 0) {
			store.setStoreDisable(StoreStatusEnum.OPEN.value());
			// 修改会员 表示已有店铺
			MemberVO member = memberApi.getById(store.getMemberId());
			member.setHaveStore(true);
			member.setStoreId(id);
			memberApi.updateById(member);
			// 设定商家的结算日
			storeDetailService.update(new LambdaUpdateWrapper<StoreDetail>()
				.eq(StoreDetail::getStoreId, id)
				.set(StoreDetail::getSettlementDay, new DateTime()));
		} else {
			store.setStoreDisable(StoreStatusEnum.REFUSED.value());
		}

		return this.updateById(store);
	}

	@Override
	public boolean disable(String id) {
		Store store = this.getById(id);
		if (store != null) {
			store.setStoreDisable(StoreStatusEnum.CLOSED.value());

			// 下架所有此店铺商品
			goodsApi.underStoreGoods(id);
			return this.updateById(store);
		}

		throw new BusinessException(ResultEnum.STORE_NOT_EXIST);
	}

	@Override
	public boolean enable(String id) {
		Store store = this.getById(id);
		if (store != null) {
			store.setStoreDisable(StoreStatusEnum.OPEN.value());
			return this.updateById(store);
		}
		throw new BusinessException(ResultEnum.STORE_NOT_EXIST);
	}

	@Override
	public boolean applyFirstStep(StoreCompanyDTO storeCompanyDTO) {
		// 获取当前操作的店铺
		Store store = getStoreByMember();

		// 店铺为空，则新增店铺
		if (store == null) {
			// AuthUser authUser = Objects.requireNonNull(UserContext.getCurrentUser());
			// Member member = memberApi.getById(authUser.getId());
			// // 根据会员创建店铺
			// store = new Store(member);
			BeanUtil.copyProperties(storeCompanyDTO, store);
			this.save(store);
			StoreDetail storeDetail = new StoreDetail();
			storeDetail.setStoreId(store.getId());
			BeanUtil.copyProperties(storeCompanyDTO, storeDetail);
			return storeDetailService.save(storeDetail);
		} else {

			// 校验迪纳普状态
			checkStoreStatus(store);
			// 复制参数 修改已存在店铺
			BeanUtil.copyProperties(storeCompanyDTO, store);
			this.updateById(store);
			// 判断是否存在店铺详情，如果没有则进行新建，如果存在则进行修改
			// StoreDetail storeDetail = storeDetailService.getStoreDetail(store.getId());
			StoreDetail storeDetail = null;
			// 如果店铺详情为空，则new ，否则复制对象，然后保存即可。
			if (storeDetail == null) {
				storeDetail = new StoreDetail();
				storeDetail.setStoreId(store.getId());
				BeanUtil.copyProperties(storeCompanyDTO, storeDetail);
				return storeDetailService.save(storeDetail);
			} else {
				BeanUtil.copyProperties(storeCompanyDTO, storeDetail);
				return storeDetailService.updateById(storeDetail);
			}
		}
	}

	@Override
	public boolean applySecondStep(StoreBankDTO storeBankDTO) {
		// 获取当前操作的店铺
		Store store = getStoreByMember();
		// StoreDetail storeDetail = storeDetailService.getStoreDetail(store.getId());
		StoreDetail storeDetail = null;
		// 设置店铺的银行信息
		BeanUtils.copyProperties(storeBankDTO, storeDetail);
		return storeDetailService.updateById(storeDetail);
	}

	@Override
	public boolean applyThirdStep(StoreOtherInfoDTO storeOtherInfoDTO) {
		// 获取当前操作的店铺
		Store store = getStoreByMember();
		BeanUtils.copyProperties(storeOtherInfoDTO, store);
		this.updateById(store);

		// StoreDetail storeDetail = storeDetailService.getStoreDetail(store.getId());
		StoreDetail storeDetail = null;
		// 设置店铺的其他信息
		BeanUtils.copyProperties(storeOtherInfoDTO, storeDetail);
		// 设置店铺经营范围
		storeDetail.setGoodsManagementCategory(storeOtherInfoDTO.getGoodsManagementCategory());
		// 最后一步申请，给予店铺设置库存预警默认值
		storeDetail.setStockWarning(10);
		// 修改店铺详细信息
		storeDetailService.updateById(storeDetail);
		// 设置店铺名称,修改店铺信息
		store.setStoreName(storeOtherInfoDTO.getStoreName());
		store.setStoreDisable(StoreStatusEnum.APPLYING.name());
		store.setStoreCenter(storeOtherInfoDTO.getStoreCenter());
		store.setStoreDesc(storeOtherInfoDTO.getStoreDesc());
		store.setStoreLogo(storeOtherInfoDTO.getStoreLogo());
		return this.updateById(store);
	}

	@Override
	public void updateStoreGoodsNum(Long storeId) {
		// 获取店铺已上架已审核通过商品数量
		long goodsNum = goodsApi.countStoreGoodsNum(storeId);
		// 修改店铺商品数量
		this.update(new LambdaUpdateWrapper<Store>()
			.set(Store::getGoodsNum, goodsNum)
			.eq(Store::getId, storeId));
	}

	@Override
	public void updateStoreCollectionNum(CollectionDTO collectionDTO) {
		baseMapper.updateCollection(collectionDTO.getId(), collectionDTO.getNum());
	}

	/**
	 * 获取当前登录操作的店铺
	 *
	 * @return 店铺信息
	 */
	private Store getStoreByMember() {
		LambdaQueryWrapper<Store> lambdaQueryWrapper = new LambdaQueryWrapper<>();
		if (SecurityUtils.getCurrentUserWithNull() != null) {
			lambdaQueryWrapper.eq(Store::getMemberId, SecurityUtils.getUserId());
		}
		return this.getOne(lambdaQueryWrapper, false);
	}

	/**
	 * 申请店铺时 对店铺状态进行校验判定
	 *
	 * @param store 店铺
	 */
	private void checkStoreStatus(Store store) {

		// 如果店铺状态为申请中或者已申请，则正常走流程，否则抛出异常
		if (store.getStoreDisable().equals(StoreStatusEnum.APPLY.name())
			|| store.getStoreDisable().equals(StoreStatusEnum.APPLYING.name())) {
			return;
		} else {
			// throw new ServiceException(ResultCode.STORE_STATUS_ERROR);
		}
	}
}

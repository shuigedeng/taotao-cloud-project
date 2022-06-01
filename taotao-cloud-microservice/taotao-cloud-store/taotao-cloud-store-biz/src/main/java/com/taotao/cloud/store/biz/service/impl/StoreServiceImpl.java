package com.taotao.cloud.store.biz.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.utils.common.SecurityUtil;
import com.taotao.cloud.common.utils.lang.BeanUtil;
import com.taotao.cloud.goods.api.feign.IFeignGoodsService;
import com.taotao.cloud.member.api.feign.IFeignMemberService;
import com.taotao.cloud.member.api.vo.MemberVO;
import com.taotao.cloud.store.api.dto.AdminStoreApplyDTO;
import com.taotao.cloud.store.api.dto.CollectionDTO;
import com.taotao.cloud.store.api.dto.StoreBankDTO;
import com.taotao.cloud.store.api.dto.StoreCompanyDTO;
import com.taotao.cloud.store.api.dto.StoreEditDTO;
import com.taotao.cloud.store.api.dto.StoreOtherInfoDTO;
import com.taotao.cloud.store.api.enums.StoreStatusEnum;
import com.taotao.cloud.store.api.query.StorePageQuery;
import com.taotao.cloud.store.api.vo.StoreVO;
import com.taotao.cloud.store.biz.entity.Store;
import com.taotao.cloud.store.biz.entity.StoreDetail;
import com.taotao.cloud.store.biz.mapper.StoreMapper;
import com.taotao.cloud.store.biz.service.StoreDetailService;
import com.taotao.cloud.store.biz.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 店铺业务层实现
 *
 * @since 2020-03-07 16:18:56
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class StoreServiceImpl extends ServiceImpl<StoreMapper, Store> implements StoreService {

	/**
	 * 会员
	 */
	@Autowired
	private IFeignMemberService memberService;
	/**
	 * 商品
	 */
	@Autowired
	private IFeignGoodsService goodsService;
	/**
	 * 店铺详情
	 */
	@Autowired
	private StoreDetailService storeDetailService;

	@Override
	public IPage<StoreVO> findByConditionPage(StorePageQuery storePageQuery) {
		return this.baseMapper.getStoreList(storePageQuery.buildMpPage(), storePageQuery.queryWrapper());
	}

	@Override
	public StoreVO getStoreDetail() {
		Long storeId = SecurityUtil.getCurrentUser().getStoreId();
		StoreVO storeVO = this.baseMapper.getStoreDetail(storeId);
		storeVO.setNickname(SecurityUtil.getCurrentUser().getNickname());
		return storeVO;
	}

	@Override
	public Store add(AdminStoreApplyDTO adminStoreApplyDTO) {
		//判断店铺名称是否存在
		QueryWrapper<Store> queryWrapper = Wrappers.query();
		queryWrapper.eq("store_name", adminStoreApplyDTO.getStoreName());
		if (this.getOne(queryWrapper) != null) {
			throw new BusinessException(ResultEnum.STORE_NAME_EXIST_ERROR);
		}

		MemberVO member = memberService.getById(adminStoreApplyDTO.getMemberId());
		//判断用户是否存在
		if (member == null) {
			throw new BusinessException(ResultEnum.USER_NOT_EXIST);
		}
		//判断是否拥有店铺
		if (Boolean.TRUE.equals(member.getHaveStore())) {
			throw new BusinessException(ResultEnum.STORE_APPLY_DOUBLE_ERROR);
		}

		//添加店铺
		Store store = new Store(member, adminStoreApplyDTO);
		this.save(store);

		//判断是否存在店铺详情，如果没有则进行新建，如果存在则进行修改
		StoreDetail storeDetail = new StoreDetail(store, adminStoreApplyDTO);

		storeDetailService.save(storeDetail);

		//设置会员-店铺信息
		memberService.update(member.getId(), store.getId());
		return store;

	}

	@Override
	public Store edit(StoreEditDTO storeEditDTO) {
		if (storeEditDTO != null) {
			//判断店铺名是否唯一
			Store storeTmp = getOne(new QueryWrapper<Store>().eq("store_name", storeEditDTO.getStoreName()));
			if (storeTmp != null && !storeTmp.getId().equals(storeEditDTO.getStoreId())) {
				throw new BusinessException(ResultEnum.STORE_NAME_EXIST_ERROR);
			}
			//修改店铺详细信息
			updateStoreDetail(storeEditDTO);
			//修改店铺信息
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
			BeanUtil.copyProperties(storeEditDTO, store);
			store.setId(storeEditDTO.getStoreId());
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
		BeanUtil.copyProperties(storeEditDTO, storeDetail);
		storeDetailService.update(storeDetail, new QueryWrapper<StoreDetail>().eq("store_id", storeEditDTO.getStoreId()));
	}

	@Override
	public boolean audit(String id, Integer passed) {
		Store store = this.getById(id);
		if (store == null) {
			throw new BusinessException(ResultEnum.STORE_NOT_EXIST);
		}
		if (passed == 0) {
			store.setStoreDisable(StoreStatusEnum.OPEN.value());
			//修改会员 表示已有店铺
			MemberVO member = memberService.getById(store.getMemberId());
			member.setHaveStore(true);
			member.setStoreId(id);
			memberService.updateById(member);
			//设定商家的结算日
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

			//下架所有此店铺商品
			goodsService.underStoreGoods(id);
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
		//获取当前操作的店铺
		Store store = getStoreByMember();
		//如果没有申请过店铺，新增店铺
		if (!Optional.ofNullable(store).isPresent()) {
			Long userId = SecurityUtil.getCurrentUser().getUserId();
			MemberVO member = memberService.getById(userId);
			store = new Store(member);
			BeanUtil.copyProperties(storeCompanyDTO, store);
			this.save(store);
			StoreDetail storeDetail = new StoreDetail();
			storeDetail.setStoreId(store.getId());
			BeanUtil.copyProperties(storeCompanyDTO, storeDetail);
			return storeDetailService.save(storeDetail);
		} else {
			BeanUtil.copyProperties(storeCompanyDTO, store);
			this.updateById(store);
			//判断是否存在店铺详情，如果没有则进行新建，如果存在则进行修改
			StoreDetail storeDetail = storeDetailService.getStoreDetail(store.getId());
			BeanUtil.copyProperties(storeCompanyDTO, storeDetail);
			return storeDetailService.updateById(storeDetail);
		}
	}

	@Override
	public boolean applySecondStep(StoreBankDTO storeBankDTO) {
		//获取当前操作的店铺
		Store store = getStoreByMember();
		StoreDetail storeDetail = storeDetailService.getStoreDetail(store.getId());
		//设置店铺的银行信息
		BeanUtil.copyProperties(storeBankDTO, storeDetail);
		return storeDetailService.updateById(storeDetail);
	}

	@Override
	public boolean applyThirdStep(StoreOtherInfoDTO storeOtherInfoDTO) {
		//获取当前操作的店铺
		Store store = getStoreByMember();
		BeanUtil.copyProperties(storeOtherInfoDTO, store);
		this.updateById(store);

		StoreDetail storeDetail = storeDetailService.getStoreDetail(store.getId());
		//设置店铺的其他信息
		BeanUtil.copyProperties(storeOtherInfoDTO, storeDetail);
		//设置店铺经营范围
		storeDetail.setGoodsManagementCategory(storeOtherInfoDTO.getGoodsManagementCategory());
		//最后一步申请，给予店铺设置库存预警默认值
		storeDetail.setStockWarning(10);
		//修改店铺详细信息
		storeDetailService.updateById(storeDetail);
		//设置店铺名称,修改店铺信息
		store.setStoreName(storeOtherInfoDTO.getStoreName());
		store.setStoreDisable(StoreStatusEnum.APPLYING.name());
		store.setStoreCenter(storeOtherInfoDTO.getStoreCenter());
		store.setStoreDesc(storeOtherInfoDTO.getStoreDesc());
		store.setStoreLogo(storeOtherInfoDTO.getStoreLogo());
		return this.updateById(store);
	}

	@Override
	public void updateStoreGoodsNum(Long storeId) {
		//获取店铺已上架已审核通过商品数量
		long goodsNum = goodsService.countStoreGoodsNum(storeId);
		//修改店铺商品数量
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
		if (SecurityUtil.getCurrentUserWithNull() != null) {
			lambdaQueryWrapper.eq(Store::getMemberId, SecurityUtil.getUserId());
		}
		return this.getOne(lambdaQueryWrapper, false);
	}

}

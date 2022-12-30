package com.taotao.cloud.member.biz.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.model.PageQuery;
import com.taotao.cloud.common.utils.common.SecurityUtils;
import com.taotao.cloud.member.api.model.vo.GoodsCollectionVO;
import com.taotao.cloud.member.biz.mapper.IGoodsCollectionMapper;
import com.taotao.cloud.member.biz.model.entity.MemberGoodsCollection;
import com.taotao.cloud.member.biz.service.business.IMemberGoodsCollectionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 会员收藏业务层实现
 *
 * @since 2020/11/18 2:25 下午
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MemberGoodsCollectionServiceImpl extends
	ServiceImpl<IGoodsCollectionMapper, MemberGoodsCollection> implements IMemberGoodsCollectionService {

	@Override
	public IPage<GoodsCollectionVO> goodsCollection(PageQuery PageQuery) {
		QueryWrapper<GoodsCollectionVO> queryWrapper = Wrappers.query();
		queryWrapper.eq("gc.member_id", SecurityUtils.getUserId());
		queryWrapper.groupBy("gc.id");
		queryWrapper.orderByDesc("gc.create_time");
		return this.baseMapper.goodsCollectionVOList(
			PageQuery.buildMpPage(), queryWrapper);
	}

	@Override
	public Boolean isCollection(Long skuId) {
		LambdaQueryWrapper<MemberGoodsCollection> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.eq(MemberGoodsCollection::getMemberId, SecurityUtils.getUserId());
		queryWrapper.eq(skuId != null, MemberGoodsCollection::getSkuId, skuId);
		return Optional.ofNullable(this.getOne(queryWrapper)).isPresent();
	}

	@Override
	public Boolean addGoodsCollection(Long skuId) {
		MemberGoodsCollection memberGoodsCollection = this.getOne(
			new LambdaUpdateWrapper<MemberGoodsCollection>()
				.eq(MemberGoodsCollection::getMemberId, SecurityUtils.getUserId())
				.eq(MemberGoodsCollection::getSkuId, skuId));

		if (memberGoodsCollection == null) {
			memberGoodsCollection = new MemberGoodsCollection(SecurityUtils.getUserId(), skuId);
			return this.save(memberGoodsCollection);
		}
		throw new BusinessException("用户不存在");
	}

	@Override
	public Boolean deleteGoodsCollection(Long skuId) {
		LambdaQueryWrapper<MemberGoodsCollection> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.eq(MemberGoodsCollection::getMemberId, SecurityUtils.getUserId());
		queryWrapper.eq(skuId != null, MemberGoodsCollection::getSkuId, skuId);
		return this.remove(queryWrapper);
	}

	@Override
	public Boolean deleteGoodsCollection(List<Long> goodsIds) {
		LambdaQueryWrapper<MemberGoodsCollection> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.in(MemberGoodsCollection::getSkuId, goodsIds);
		return this.remove(queryWrapper);
	}

	@Override
	public Boolean deleteSkuCollection(List<Long> skuIds) {
		LambdaQueryWrapper<MemberGoodsCollection> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.in(MemberGoodsCollection::getSkuId, skuIds);
		return this.remove(queryWrapper);
	}
}

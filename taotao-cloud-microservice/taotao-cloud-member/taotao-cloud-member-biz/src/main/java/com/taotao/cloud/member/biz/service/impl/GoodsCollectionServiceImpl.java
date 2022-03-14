package com.taotao.cloud.member.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.model.PageModel;
import com.taotao.cloud.common.model.PageParam;
import com.taotao.cloud.common.utils.common.SecurityUtil;
import com.taotao.cloud.member.api.vo.GoodsCollectionVO;
import com.taotao.cloud.member.biz.entity.MemberGoodsCollection;
import com.taotao.cloud.member.biz.mapper.GoodsCollectionMapper;
import com.taotao.cloud.member.biz.service.GoodsCollectionService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 会员收藏业务层实现
 *
 * @since 2020/11/18 2:25 下午
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class GoodsCollectionServiceImpl extends
	ServiceImpl<GoodsCollectionMapper, MemberGoodsCollection> implements
	GoodsCollectionService {

	@Override
	public PageModel<GoodsCollectionVO> goodsCollection(PageParam pageParam) {
		QueryWrapper<GoodsCollectionVO> queryWrapper = Wrappers.query();
		queryWrapper.eq("gc.member_id", SecurityUtil.getUserId());
		queryWrapper.groupBy("gc.id");
		queryWrapper.orderByDesc("gc.create_time");
		IPage<GoodsCollectionVO> goodsCollectionPage = this.baseMapper.goodsCollectionVOList(
			pageParam.buildMpPage(), queryWrapper);
		return PageModel.convertMybatisPage(goodsCollectionPage, GoodsCollectionVO.class);
	}

	@Override
	public boolean isCollection(String skuId) {
		LambdaQueryWrapper<MemberGoodsCollection> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.eq(MemberGoodsCollection::getMemberId, SecurityUtil.getUserId());
		queryWrapper.eq(skuId != null, MemberGoodsCollection::getSkuId, skuId);
		return Optional.ofNullable(this.getOne(queryWrapper)).isPresent();
	}

	@Override
	public boolean addGoodsCollection(String skuId) {
		MemberGoodsCollection memberGoodsCollection = this.getOne(
			new LambdaUpdateWrapper<MemberGoodsCollection>()
				.eq(MemberGoodsCollection::getMemberId, SecurityUtil.getUserId())
				.eq(MemberGoodsCollection::getSkuId, skuId));

		if (memberGoodsCollection == null) {
			memberGoodsCollection = new MemberGoodsCollection(SecurityUtil.getUserId(), skuId);
			return this.save(memberGoodsCollection);
		}
		throw new BusinessException("用户不存在");
	}

	@Override
	public boolean deleteGoodsCollection(String skuId) {
		LambdaQueryWrapper<MemberGoodsCollection> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.eq(MemberGoodsCollection::getMemberId, SecurityUtil.getUserId());
		queryWrapper.eq(skuId != null, MemberGoodsCollection::getSkuId, skuId);
		return this.remove(queryWrapper);
	}

	@Override
	public boolean deleteGoodsCollection(List<String> goodsIds) {
		LambdaQueryWrapper<MemberGoodsCollection> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.in(MemberGoodsCollection::getSkuId, goodsIds);
		return this.remove(queryWrapper);
	}

	@Override
	public boolean deleteSkuCollection(List<String> skuIds) {
		LambdaQueryWrapper<MemberGoodsCollection> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.in(MemberGoodsCollection::getSkuId, skuIds);
		return this.remove(queryWrapper);
	}
}

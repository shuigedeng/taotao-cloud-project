package com.taotao.cloud.member.biz.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.common.model.PageQuery;
import com.taotao.cloud.common.utils.common.SecurityUtils;
import com.taotao.cloud.goods.api.feign.IFeignEsGoodsIndexApi;
import com.taotao.cloud.goods.api.model.vo.EsGoodsIndexVO;
import com.taotao.cloud.member.biz.mapper.IFootprintMapper;
import com.taotao.cloud.member.biz.model.entity.MemberBrowse;
import com.taotao.cloud.member.biz.service.business.IMemberBrowseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 会员浏览历史业务层实现
 *
 * @since 2020/11/18 10:46 上午
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MemberBrowseServiceImpl extends ServiceImpl<IFootprintMapper, MemberBrowse> implements
	IMemberBrowseService {

	/**
	 * es商品业务层
	 */
	@Autowired
	private IFeignEsGoodsIndexApi feignEsGoodsIndexApi;

	@Override
	public MemberBrowse saveFootprint(MemberBrowse memberBrowse) {
		LambdaQueryWrapper<MemberBrowse> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.eq(MemberBrowse::getMemberId, memberBrowse.getMemberId());
		queryWrapper.eq(MemberBrowse::getGoodsId, memberBrowse.getGoodsId());
		//如果已存在某商品记录，则更新其修改时间
		//如果不存在则添加记录
		List<MemberBrowse> oldPrints = list(queryWrapper);
		if (oldPrints != null && !oldPrints.isEmpty()) {
			MemberBrowse oldPrint = oldPrints.get(0);
			oldPrint.setSkuId(memberBrowse.getSkuId());
			this.updateById(oldPrint);
			return oldPrint;
		} else {
			memberBrowse.setCreateTime(LocalDateTime.now());
			this.save(memberBrowse);
			//删除超过100条后的记录
			this.baseMapper.deleteLastFootPrint(memberBrowse.getMemberId());
			return memberBrowse;
		}
	}

	@Override
	public Boolean clean() {
		LambdaQueryWrapper<MemberBrowse> lambdaQueryWrapper = Wrappers.lambdaQuery();
		lambdaQueryWrapper.eq(MemberBrowse::getMemberId, SecurityUtils.getUserId());
		return this.remove(lambdaQueryWrapper);
	}

	@Override
	public Boolean deleteByIds(List<Long> ids) {
		LambdaQueryWrapper<MemberBrowse> lambdaQueryWrapper = Wrappers.lambdaQuery();
		lambdaQueryWrapper.eq(MemberBrowse::getMemberId, SecurityUtils.getUserId());
		lambdaQueryWrapper.in(MemberBrowse::getGoodsId, ids);
		this.remove(lambdaQueryWrapper);
		return true;
	}

	@Override
	public List<EsGoodsIndexVO> footPrintPage(PageQuery PageQuery) {
		LambdaQueryWrapper<MemberBrowse> lambdaQueryWrapper = Wrappers.lambdaQuery();
		lambdaQueryWrapper.eq(MemberBrowse::getMemberId, SecurityUtils.getUserId());
		lambdaQueryWrapper.eq(MemberBrowse::getDelFlag, false);
		lambdaQueryWrapper.orderByDesc(MemberBrowse::getUpdateTime);
		List<String> skuIdList = this.baseMapper.footprintSkuIdList(PageQuery.buildMpPage(), lambdaQueryWrapper);
		if (!skuIdList.isEmpty()) {
			List<EsGoodsIndexVO> list = feignEsGoodsIndexApi.getEsGoodsBySkuIds(skuIdList);
			//去除为空的商品数据
			list.removeIf(Objects::isNull);
			return list;
		}
		return Collections.emptyList();
	}

	@Override
	public Long getFootprintNum() {
		LambdaQueryWrapper<MemberBrowse> lambdaQueryWrapper = Wrappers.lambdaQuery();
		lambdaQueryWrapper.eq(MemberBrowse::getMemberId, SecurityUtils.getUserId());
		lambdaQueryWrapper.eq(MemberBrowse::getDelFlag, false);
		return this.count(lambdaQueryWrapper);
	}
}

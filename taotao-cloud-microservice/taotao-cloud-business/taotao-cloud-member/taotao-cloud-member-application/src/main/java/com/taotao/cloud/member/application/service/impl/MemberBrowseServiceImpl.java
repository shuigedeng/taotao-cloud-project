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

package com.taotao.cloud.member.application.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.boot.common.model.PageQuery;
import com.taotao.cloud.member.application.service.IMemberBrowseService;
import com.taotao.cloud.member.infrastructure.persistent.mapper.IFootprintMapper;
import com.taotao.cloud.member.infrastructure.persistent.po.MemberBrowsePO;
import com.taotao.cloud.security.springsecurity.utils.SecurityUtils;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 会员浏览历史业务层实现
 *
 * @since 2020/11/18 10:46 上午
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MemberBrowseServiceImpl extends ServiceImpl<IFootprintMapper, MemberBrowsePO>
	implements IMemberBrowseService {

	/**
	 * es商品业务层
	 */
	@Autowired
	private IFeignEsGoodsIndexApi esGoodsIndexApi;

	@Override
	public MemberBrowsePO saveFootprint(MemberBrowsePO memberBrowsePO) {
		LambdaQueryWrapper<MemberBrowsePO> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.eq(MemberBrowsePO::getMemberId, memberBrowsePO.getMemberId());
		queryWrapper.eq(MemberBrowsePO::getGoodsId, memberBrowsePO.getGoodsId());
		// 如果已存在某商品记录，则更新其修改时间
		// 如果不存在则添加记录
		List<MemberBrowsePO> oldPrints = list(queryWrapper);
		if (oldPrints != null && !oldPrints.isEmpty()) {
			MemberBrowsePO oldPrint = oldPrints.get(0);
			oldPrint.setSkuId(memberBrowsePO.getSkuId());
			this.updateById(oldPrint);
			return oldPrint;
		}
		else {
			memberBrowsePO.setCreateTime(LocalDateTime.now());
			this.save(memberBrowsePO);
			// 删除超过100条后的记录
			this.baseMapper.deleteLastFootPrint(memberBrowsePO.getMemberId());
			return memberBrowsePO;
		}
	}

	@Override
	public Boolean clean() {
		LambdaQueryWrapper<MemberBrowsePO> lambdaQueryWrapper = Wrappers.lambdaQuery();
		lambdaQueryWrapper.eq(MemberBrowsePO::getMemberId, SecurityUtils.getUserId());
		return this.remove(lambdaQueryWrapper);
	}

	@Override
	public Boolean deleteByIds(List<Long> ids) {
		LambdaQueryWrapper<MemberBrowsePO> lambdaQueryWrapper = Wrappers.lambdaQuery();
		lambdaQueryWrapper.eq(MemberBrowsePO::getMemberId, SecurityUtils.getUserId());
		lambdaQueryWrapper.in(MemberBrowsePO::getGoodsId, ids);
		this.remove(lambdaQueryWrapper);
		return true;
	}

	@Override
	public List<EsGoodsIndexVO> footPrintPage(PageQuery PageQuery) {
		LambdaQueryWrapper<MemberBrowsePO> lambdaQueryWrapper = Wrappers.lambdaQuery();
		lambdaQueryWrapper.eq(MemberBrowsePO::getMemberId, SecurityUtils.getUserId());
		lambdaQueryWrapper.eq(MemberBrowsePO::getDelFlag, false);
		lambdaQueryWrapper.orderByDesc(MemberBrowsePO::getUpdateTime);
		List<String> skuIdList = this.baseMapper.footprintSkuIdList(PageQuery.buildMpPage(),
			lambdaQueryWrapper);
		if (!skuIdList.isEmpty()) {
			List<EsGoodsIndexVO> list = esGoodsIndexApi.getEsGoodsBySkuIds(skuIdList);
			// 去除为空的商品数据
			list.removeIf(Objects::isNull);
			return list;
		}
		return Collections.emptyList();
	}

	@Override
	public Long getFootprintNum() {
		LambdaQueryWrapper<MemberBrowsePO> lambdaQueryWrapper = Wrappers.lambdaQuery();
		lambdaQueryWrapper.eq(MemberBrowsePO::getMemberId, SecurityUtils.getUserId());
		lambdaQueryWrapper.eq(MemberBrowsePO::getDelFlag, false);
		return this.count(lambdaQueryWrapper);
	}
}

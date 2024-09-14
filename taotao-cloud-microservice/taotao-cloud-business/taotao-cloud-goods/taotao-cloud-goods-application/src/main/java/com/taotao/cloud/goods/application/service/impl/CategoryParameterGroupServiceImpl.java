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
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.taotao.boot.common.enums.ResultEnum;
import com.taotao.boot.common.exception.BusinessException;
import com.taotao.cloud.goods.application.service.ICategoryParameterGroupService;
import com.taotao.cloud.goods.application.service.IGoodsService;
import com.taotao.cloud.goods.application.service.IParametersService;
import com.taotao.cloud.goods.infrastructure.persistent.mapper.ICategoryParameterGroupMapper;
import com.taotao.cloud.goods.infrastructure.persistent.po.CategoryParameterGroupPO;
import com.taotao.cloud.goods.infrastructure.persistent.repository.cls.CategoryParameterGroupRepository;
import com.taotao.cloud.goods.infrastructure.persistent.repository.inf.ICategoryParameterGroupRepository;
import com.taotao.cloud.web.base.service.impl.BaseSuperServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分类绑定参数组接口实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:02:04
 */
@AllArgsConstructor
@Service
public class CategoryParameterGroupServiceImpl extends BaseSuperServiceImpl<
	CategoryParameterGroupPO,
	Long,
	ICategoryParameterGroupMapper,
	CategoryParameterGroupRepository,
	ICategoryParameterGroupRepository>
	implements ICategoryParameterGroupService {

	/**
	 * 商品参数服务
	 */
	private final IParametersService parametersService;
	/**
	 * 商品服务
	 */
	private final IGoodsService goodsService;

	@Override
	public List<ParameterGroupCO> getCategoryParams(Long categoryId) {
		// 根据id查询参数组
		List<CategoryParameterGroupPO> groups = this.getCategoryGroup(categoryId);
		// 查询参数
		List<Parameters> params = parametersService.queryParametersByCategoryId(categoryId);
		// 组合参数vo
		return convertParamList(groups, params);
	}

	@Override
	public List<CategoryParameterGroupPO> getCategoryGroup(Long categoryId) {
		QueryWrapper<CategoryParameterGroupPO> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("category_id", categoryId);
		return this.list(queryWrapper);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateCategoryGroup(CategoryParameterGroupPO categoryParameterGroupPO) {
		CategoryParameterGroupPO origin = this.getById(categoryParameterGroupPO.getId());
		if (origin == null) {
			throw new BusinessException(ResultEnum.CATEGORY_PARAMETER_NOT_EXIST);
		}

		LambdaQueryWrapper<Goods> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.select(Goods::getId, Goods::getParams);
		queryWrapper.like(Goods::getParams, origin.getId());
		List<Map<String, Object>> goodsList = this.goodsService.listMaps(queryWrapper);

		for (Map<String, Object> goods : goodsList) {
			String params = (String) goods.get("params");
			List<GoodsParamsDTO> goodsParamsDTOS = JSONUtil.toList(params, GoodsParamsDTO.class);
			List<GoodsParamsDTO> goodsParamsDTOList = goodsParamsDTOS.stream()
				.filter(i -> i.getGroupId() != null && i.getGroupId().equals(origin.getId()))
				.toList();
			for (GoodsParamsDTO goodsParamsDTO : goodsParamsDTOList) {
				goodsParamsDTO.setGroupName(categoryParameterGroupPO.getGroupName());
			}

			this.goodsService.updateGoodsParams(
				Long.valueOf(goods.get("id").toString()), JSONUtil.toJsonStr(goodsParamsDTOS));
		}

		return this.updateById(categoryParameterGroupPO);
	}

	@Override
	public boolean deleteByCategoryId(Long categoryId) {
		return this.baseMapper.delete(new LambdaUpdateWrapper<CategoryParameterGroupPO>()
			.eq(CategoryParameterGroupPO::getCategoryId, categoryId))
			> 0;
	}

	/**
	 * 拼装参数组和参数的返回值
	 *
	 * @param groupList 参数组list
	 * @param paramList 商品参数list
	 * @return 参数组和参数的返回值
	 */
	public List<ParameterGroupCO> convertParamList(List<CategoryParameterGroupPO> groupList, List<Parameters> paramList) {
		Map<Long, List<Parameters>> map = new HashMap<>(paramList.size());
		for (Parameters param : paramList) {
			List<Parameters> list = map.get(param.getGroupId());
			if (list == null) {
				list = new ArrayList<>();
			}
			list.add(param);
			map.put(param.getGroupId(), list);
		}

		List<ParameterGroupCO> resList = new ArrayList<>();
		for (CategoryParameterGroupPO group : groupList) {
			ParameterGroupCO groupVo = new ParameterGroupCO();
			groupVo.setGroupId(group.getId());
			groupVo.setGroupName(group.getGroupName());
			groupVo.setParams(
				map.get(group.getId()) == null
					? new ArrayList<>()
					: ParametersConvert.INSTANCE.convert(map.get(group.getId())));
			resList.add(groupVo);
		}
		return resList;
	}
}

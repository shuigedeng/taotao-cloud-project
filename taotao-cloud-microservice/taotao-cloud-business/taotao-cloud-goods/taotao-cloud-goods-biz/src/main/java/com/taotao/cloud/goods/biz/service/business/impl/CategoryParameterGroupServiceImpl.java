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

package com.taotao.cloud.goods.biz.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.taotao.boot.common.enums.ResultEnum;
import com.taotao.boot.common.exception.BusinessException;
import com.taotao.cloud.goods.biz.model.dto.GoodsParamsDTO;
import com.taotao.cloud.goods.biz.model.vo.ParameterGroupVO;
import com.taotao.cloud.goods.biz.mapper.ICategoryParameterGroupMapper;
import com.taotao.cloud.goods.biz.model.convert.ParametersConvert;
import com.taotao.cloud.goods.biz.model.entity.CategoryParameterGroup;
import com.taotao.cloud.goods.biz.model.entity.Goods;
import com.taotao.cloud.goods.biz.model.entity.Parameters;
import com.taotao.cloud.goods.biz.repository.CategoryParameterGroupRepository;
import com.taotao.cloud.goods.biz.repository.ICategoryParameterGroupRepository;
import com.taotao.cloud.goods.biz.service.business.ICategoryParameterGroupService;
import com.taotao.cloud.goods.biz.service.business.IGoodsService;
import com.taotao.cloud.goods.biz.service.business.IParametersService;
import com.taotao.boot.webagg.service.impl.BaseSuperServiceImpl;
import lombok.*;
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
	CategoryParameterGroup,
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
	public List<ParameterGroupVO> getCategoryParams(Long categoryId) {
		// 根据id查询参数组
		List<CategoryParameterGroup> groups = this.getCategoryGroup(categoryId);
		// 查询参数
		List<Parameters> params = parametersService.queryParametersByCategoryId(categoryId);
		// 组合参数vo
		return convertParamList(groups, params);
	}

	@Override
	public List<CategoryParameterGroup> getCategoryGroup(Long categoryId) {
		QueryWrapper<CategoryParameterGroup> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("category_id", categoryId);
		return this.list(queryWrapper);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateCategoryGroup(CategoryParameterGroup categoryParameterGroup) {
		CategoryParameterGroup origin = this.getById(categoryParameterGroup.getId());
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
				goodsParamsDTO.setGroupName(categoryParameterGroup.getGroupName());
			}

			this.goodsService.updateGoodsParams(
				Long.valueOf(goods.get("id").toString()), JSONUtil.toJsonStr(goodsParamsDTOS));
		}

		return this.updateById(categoryParameterGroup);
	}

	@Override
	public boolean deleteByCategoryId(Long categoryId) {
		return this.baseMapper.delete(new LambdaUpdateWrapper<CategoryParameterGroup>()
			.eq(CategoryParameterGroup::getCategoryId, categoryId))
			> 0;
	}

	/**
	 * 拼装参数组和参数的返回值
	 *
	 * @param groupList 参数组list
	 * @param paramList 商品参数list
	 * @return 参数组和参数的返回值
	 */
	public List<ParameterGroupVO> convertParamList(List<CategoryParameterGroup> groupList, List<Parameters> paramList) {
		Map<Long, List<Parameters>> map = new HashMap<>(paramList.size());
		for (Parameters param : paramList) {
			List<Parameters> list = map.get(param.getGroupId());
			if (list == null) {
				list = new ArrayList<>();
			}
			list.add(param);
			map.put(param.getGroupId(), list);
		}

		List<ParameterGroupVO> resList = new ArrayList<>();
		for (CategoryParameterGroup group : groupList) {
			ParameterGroupVO groupVo = new ParameterGroupVO();
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

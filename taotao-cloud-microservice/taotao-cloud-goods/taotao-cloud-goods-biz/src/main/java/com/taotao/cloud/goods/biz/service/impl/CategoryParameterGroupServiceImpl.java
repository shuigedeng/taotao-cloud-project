package com.taotao.cloud.goods.biz.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.goods.api.dto.GoodsParamsDTO;
import com.taotao.cloud.goods.api.vo.ParameterGroupVO;
import com.taotao.cloud.goods.biz.entity.CategoryParameterGroup;
import com.taotao.cloud.goods.biz.entity.Goods;
import com.taotao.cloud.goods.biz.entity.Parameters;
import com.taotao.cloud.goods.biz.mapper.CategoryParameterGroupMapper;
import com.taotao.cloud.goods.biz.service.CategoryParameterGroupService;
import com.taotao.cloud.goods.biz.service.GoodsService;
import com.taotao.cloud.goods.biz.service.ParametersService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 分类绑定参数组接口实现
 */
@Service
public class CategoryParameterGroupServiceImpl extends
	ServiceImpl<CategoryParameterGroupMapper, CategoryParameterGroup> implements
	CategoryParameterGroupService {

	/**
	 * 商品参数
	 */
	@Autowired
	private ParametersService parametersService;

	@Autowired
	private GoodsService goodsService;

	@Override
	public List<ParameterGroupVO> getCategoryParams(String categoryId) {
		//根据id查询参数组
		List<CategoryParameterGroup> groups = this.getCategoryGroup(categoryId);
		//查询参数
		List<Parameters> params = parametersService.list(
			new QueryWrapper<Parameters>().eq("category_id", categoryId));
		//组合参数vo
		return convertParamList(groups, params);
	}

	@Override
	public List<CategoryParameterGroup> getCategoryGroup(String categoryId) {
		return this.list(new QueryWrapper<CategoryParameterGroup>().eq("category_id", categoryId));
	}

	/**
	 * 更新分类参数组绑定信息
	 *
	 * @param categoryParameterGroup 分类参数组信息
	 * @return 是否成功
	 */
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
				.collect(Collectors.toList());
			for (GoodsParamsDTO goodsParamsDTO : goodsParamsDTOList) {
				goodsParamsDTO.setGroupName(categoryParameterGroup.getGroupName());
			}
			this.goodsService.updateGoodsParams(goods.get("id").toString(),
				JSONUtil.toJsonStr(goodsParamsDTOS));
		}

		return this.updateById(categoryParameterGroup);
	}

	@Override
	public void deleteByCategoryId(String categoryId) {
		this.baseMapper.delete(new LambdaUpdateWrapper<CategoryParameterGroup>().eq(
			CategoryParameterGroup::getCategoryId, categoryId));
	}

	/**
	 * 拼装参数组和参数的返回值
	 *
	 * @param groupList 参数组list
	 * @param paramList 商品参数list
	 * @return 参数组和参数的返回值
	 */
	public List<ParameterGroupVO> convertParamList(List<CategoryParameterGroup> groupList,
		List<Parameters> paramList) {
		Map<String, List<Parameters>> map = new HashMap<>(paramList.size());
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
				map.get(group.getId()) == null ? new ArrayList<>() : map.get(group.getId()));
			resList.add(groupVo);
		}
		return resList;
	}
}

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
import com.taotao.boot.common.enums.ResultEnum;
import com.taotao.boot.common.exception.BusinessException;
import com.taotao.cloud.goods.application.command.goods.dto.GoodsParamsAddCmd;
import com.taotao.cloud.goods.application.command.goods.dto.GoodsParamsItemAddCmd;
import com.taotao.cloud.goods.application.service.IGoodsService;
import com.taotao.cloud.goods.application.service.IParametersService;
import com.taotao.cloud.goods.infrastructure.persistent.mapper.IParametersMapper;
import com.taotao.cloud.goods.infrastructure.persistent.po.GoodsPO;
import com.taotao.cloud.goods.infrastructure.persistent.po.ParametersPO;
import com.taotao.cloud.goods.infrastructure.persistent.repository.cls.ParametersRepository;
import com.taotao.cloud.goods.infrastructure.persistent.repository.inf.IParametersRepository;
import com.taotao.cloud.mq.stream.framework.rocketmq.RocketmqSendCallbackBuilder;
import com.taotao.cloud.mq.stream.framework.rocketmq.tags.GoodsTagsEnum;
import com.taotao.cloud.mq.stream.properties.RocketmqCustomProperties;
import com.taotao.cloud.web.base.service.impl.BaseSuperServiceImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.dromara.hutool.core.convert.Convert;
import org.dromara.hutool.core.text.CharSequenceUtil;
import org.dromara.hutool.json.JSONUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 商品参数业务层实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:02:52
 */
@AllArgsConstructor
@Service
public class ParametersServiceImpl
	extends
	BaseSuperServiceImpl<ParametersPO, Long, IParametersMapper, ParametersRepository, IParametersRepository>
	implements IParametersService {

	/**
	 * 商品服务
	 */
	private final IGoodsService goodsService;

	private final RocketmqCustomProperties rocketmqCustomProperties;
	private final RocketMQTemplate rocketMQTemplate;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateParameter(ParametersPO parametersPO) {
		ParametersPO origin = this.getById(parametersPO.getId());
		if (origin == null) {
			throw new BusinessException(ResultEnum.CATEGORY_NOT_EXIST);
		}

		List<String> goodsIds = new ArrayList<>();
		LambdaQueryWrapper<GoodsPO> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.select(GoodsPO::getId, GoodsPO::getParams);
		queryWrapper.like(GoodsPO::getParams, parametersPO.getGroupId());
		List<Map<String, Object>> goodsList = this.goodsService.listMaps(queryWrapper);

		if (!goodsList.isEmpty()) {
			for (Map<String, Object> goods : goodsList) {
				String params = (String) goods.get("params");
				List<GoodsParamsAddCmd> goodsParamsAddCmds = JSONUtil.toList(params,
					GoodsParamsAddCmd.class);
				List<GoodsParamsAddCmd> goodsParamsAddCmdList = goodsParamsAddCmds.stream()
					.filter(i -> i.getGroupId() != null && i.getGroupId()
						.equals(parametersPO.getGroupId()))
					.toList();
				this.setGoodsItemDTOList(goodsParamsAddCmdList, parametersPO);
				this.goodsService.updateGoodsParams(
					Convert.toLong(goods.get("id")), JSONUtil.toJsonStr(goodsParamsAddCmds));
				goodsIds.add(goods.get("id").toString());
			}

			String destination =
				rocketmqCustomProperties.getGoodsTopic() + ":"
					+ GoodsTagsEnum.UPDATE_GOODS_INDEX.name();
			// 发送mq消息
			rocketMQTemplate.asyncSend(
				destination, JSONUtil.toJsonStr(goodsIds),
				RocketmqSendCallbackBuilder.commonCallback());
		}
		return this.updateById(parametersPO);
	}

	@Override
	public List<ParametersPO> queryParametersByCategoryId(Long categoryId) {
		QueryWrapper<ParametersPO> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("category_id", categoryId);
		return this.list(queryWrapper);
	}

	/**
	 * 更新商品参数信息
	 *
	 * @param goodsParamsAddCmdList 商品参数项列表
	 * @param parametersPO       参数信息
	 */
	private void setGoodsItemDTOList(List<GoodsParamsAddCmd> goodsParamsAddCmdList,
		ParametersPO parametersPO) {
		for (GoodsParamsAddCmd goodsParamsAddCmd : goodsParamsAddCmdList) {
			List<GoodsParamsItemAddCmd> goodsParamsItemAddCmdList = goodsParamsAddCmd.getGoodsParamsItemAddCmdList()
				.stream()
				.filter(i -> i.getParamId() != null && i.getParamId().equals(parametersPO.getId()))
				.toList();
			for (GoodsParamsItemAddCmd goodsParamsItemAddCmd : goodsParamsItemAddCmdList) {
				this.setGoodsItemDTO(goodsParamsItemAddCmd, parametersPO);
			}
		}
	}

	/**
	 * 更新商品参数详细信息
	 *
	 * @param goodsParamsItemAddCmd 商品参数项信息
	 * @param parametersPO       参数信息
	 */
	private void setGoodsItemDTO(GoodsParamsItemAddCmd goodsParamsItemAddCmd, ParametersPO parametersPO) {
		if (goodsParamsItemAddCmd.getParamId().equals(parametersPO.getId())) {
			goodsParamsItemAddCmd.setParamId(parametersPO.getId());
			goodsParamsItemAddCmd.setParamName(parametersPO.getParamName());
			goodsParamsItemAddCmd.setRequired(parametersPO.getRequired());
			goodsParamsItemAddCmd.setIsIndex(parametersPO.getIsIndex());
			goodsParamsItemAddCmd.setSort(parametersPO.getSort());
			if (CharSequenceUtil.isNotEmpty(parametersPO.getOptions())
				&& CharSequenceUtil.isNotEmpty(goodsParamsItemAddCmd.getParamValue())
				&& !parametersPO.getOptions().contains(goodsParamsItemAddCmd.getParamValue())) {
				if (parametersPO.getOptions().contains(",")) {
					goodsParamsItemAddCmd.setParamValue(parametersPO
						.getOptions()
						.substring(0, parametersPO.getOptions().indexOf(",")));
				}
				else {
					goodsParamsItemAddCmd.setParamValue(parametersPO.getOptions());
				}
			}
		}
	}
}

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
import com.taotao.boot.common.enums.ResultEnum;
import com.taotao.boot.common.exception.BusinessException;
import com.taotao.cloud.goods.biz.model.dto.GoodsParamsDTO;
import com.taotao.cloud.goods.biz.model.dto.GoodsParamsItemDTO;
import com.taotao.cloud.goods.biz.mapper.ParametersMapper;
import com.taotao.cloud.goods.biz.model.entity.Goods;
import com.taotao.cloud.goods.biz.model.entity.Parameters;
import com.taotao.cloud.goods.biz.repository.ParametersRepository;
import com.taotao.cloud.goods.biz.service.business.GoodsService;
import com.taotao.cloud.goods.biz.service.business.ParametersService;
import com.taotao.cloud.stream.framework.rocketmq.RocketmqSendCallbackBuilder;
import com.taotao.cloud.stream.framework.rocketmq.tags.GoodsTagsEnum;
import com.taotao.cloud.stream.properties.RocketmqCustomProperties;
import com.taotao.boot.webagg.service.impl.BaseSuperServiceImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.*;
import org.apache.rocketmq.spring.core.RocketMQTemplate;



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
        extends BaseSuperServiceImpl<Parameters, Long, ParametersMapper,  ParametersRepository, ParametersRepository>
        implements ParametersService {

    /** 商品服务 */
    private final GoodsService goodsService;

    private final RocketmqCustomProperties rocketmqCustomProperties;
    private final RocketMQTemplate rocketMQTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateParameter(Parameters parameters) {
        Parameters origin = this.getById(parameters.getId());
        if (origin == null) {
            throw new BusinessException(ResultEnum.CATEGORY_NOT_EXIST);
        }

        List<String> goodsIds = new ArrayList<>();
        LambdaQueryWrapper<Goods> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Goods::getId, Goods::getParams);
        queryWrapper.like(Goods::getParams, parameters.getGroupId());
        List<Map<String, Object>> goodsList = this.goodsService.listMaps(queryWrapper);

        if (!goodsList.isEmpty()) {
            for (Map<String, Object> goods : goodsList) {
                String params = (String) goods.get("params");
                List<GoodsParamsDTO> goodsParamsDTOS = JSONUtil.toList(params, GoodsParamsDTO.class);
                List<GoodsParamsDTO> goodsParamsDTOList = goodsParamsDTOS.stream()
                        .filter(i -> i.getGroupId() != null && i.getGroupId().equals(parameters.getGroupId()))
                        .toList();
                this.setGoodsItemDTOList(goodsParamsDTOList, parameters);
                this.goodsService.updateGoodsParams(
                        Convert.toLong(goods.get("id")), JSONUtil.toJsonStr(goodsParamsDTOS));
                goodsIds.add(goods.get("id").toString());
            }

            String destination =
                    rocketmqCustomProperties.getGoodsTopic() + ":" + GoodsTagsEnum.UPDATE_GOODS_INDEX.name();
            // 发送mq消息
            rocketMQTemplate.asyncSend(
                    destination, JSONUtil.toJsonStr(goodsIds), RocketmqSendCallbackBuilder.commonCallback());
        }
        return this.updateById(parameters);
    }

    @Override
    public List<Parameters> queryParametersByCategoryId(Long categoryId) {
        QueryWrapper<Parameters> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id", categoryId);
        return this.list(queryWrapper);
    }

    /**
     * 更新商品参数信息
     *
     * @param goodsParamsDTOList 商品参数项列表
     * @param parameters 参数信息
     */
    private void setGoodsItemDTOList(List<GoodsParamsDTO> goodsParamsDTOList, Parameters parameters) {
        for (GoodsParamsDTO goodsParamsDTO : goodsParamsDTOList) {
            List<GoodsParamsItemDTO> goodsParamsItemDTOList = goodsParamsDTO.getGoodsParamsItemDTOList().stream()
                    .filter(i -> i.getParamId() != null && i.getParamId().equals(parameters.getId()))
                    .toList();
            for (GoodsParamsItemDTO goodsParamsItemDTO : goodsParamsItemDTOList) {
                this.setGoodsItemDTO(goodsParamsItemDTO, parameters);
            }
        }
    }

    /**
     * 更新商品参数详细信息
     *
     * @param goodsParamsItemDTO 商品参数项信息
     * @param parameters 参数信息
     */
    private void setGoodsItemDTO(GoodsParamsItemDTO goodsParamsItemDTO, Parameters parameters) {
        if (goodsParamsItemDTO.getParamId().equals(parameters.getId())) {
            goodsParamsItemDTO.setParamId(parameters.getId());
            goodsParamsItemDTO.setParamName(parameters.getParamName());
            goodsParamsItemDTO.setRequired(parameters.getRequired());
            goodsParamsItemDTO.setIsIndex(parameters.getIsIndex());
            goodsParamsItemDTO.setSort(parameters.getSort());
            if (CharSequenceUtil.isNotEmpty(parameters.getOptions())
                    && CharSequenceUtil.isNotEmpty(goodsParamsItemDTO.getParamValue())
                    && !parameters.getOptions().contains(goodsParamsItemDTO.getParamValue())) {
                if (parameters.getOptions().contains(",")) {
                    goodsParamsItemDTO.setParamValue(parameters
                            .getOptions()
                            .substring(0, parameters.getOptions().indexOf(",")));
                } else {
                    goodsParamsItemDTO.setParamValue(parameters.getOptions());
                }
            }
        }
    }
}

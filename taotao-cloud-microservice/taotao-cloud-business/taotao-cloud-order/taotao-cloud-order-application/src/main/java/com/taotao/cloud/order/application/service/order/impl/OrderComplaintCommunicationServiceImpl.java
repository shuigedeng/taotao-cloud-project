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

package com.taotao.cloud.order.application.service.order.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.order.application.command.order.dto.OrderComplaintCommunicationPageQry;
import com.taotao.cloud.order.application.service.order.IOrderComplaintCommunicationService;
import com.taotao.cloud.order.infrastructure.persistent.mapper.order.IOrderComplainCommunicationMapper;
import com.taotao.cloud.order.infrastructure.persistent.po.order.OrderComplaintCommunicationPO;
import lombok.AllArgsConstructor;
import org.dromara.hutool.core.text.StrUtil;
import org.springframework.stereotype.Service;

/**
 * 交易投诉通信业务层实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:55:00
 */
@Service
@AllArgsConstructor
public class OrderComplaintCommunicationServiceImpl
        extends ServiceImpl<IOrderComplainCommunicationMapper, OrderComplaintCommunicationPO>
        implements IOrderComplaintCommunicationService {

    @Override
    public Boolean addCommunication(OrderComplaintCommunicationPO orderComplaintCommunicationPO) {
        return this.save(orderComplaintCommunicationPO);
    }

    @Override
    public IPage<OrderComplaintCommunicationPO> getCommunication(
		OrderComplaintCommunicationPageQry pageQuery) {
        LambdaQueryWrapper<OrderComplaintCommunicationPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(
                StrUtil.isNotEmpty(pageQuery.getComplainId()),
                OrderComplaintCommunicationPO::getComplainId,
                pageQuery.getComplainId());
        queryWrapper.eq(
                StrUtil.isNotEmpty(pageQuery.getOwner()), OrderComplaintCommunicationPO::getOwner, pageQuery.getOwner());
        queryWrapper.eq(
                StrUtil.isNotEmpty(pageQuery.getOwnerName()),
                OrderComplaintCommunicationPO::getOwnerName,
                pageQuery.getOwnerName());
        queryWrapper.eq(
                StrUtil.isNotEmpty(pageQuery.getOwnerId()),
                OrderComplaintCommunicationPO::getOwnerId,
                pageQuery.getOwnerId());

        return this.page(pageQuery.buildMpPage(), queryWrapper);
    }
}

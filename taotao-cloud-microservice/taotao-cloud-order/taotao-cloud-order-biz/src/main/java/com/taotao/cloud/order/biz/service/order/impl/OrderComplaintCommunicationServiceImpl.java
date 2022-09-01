package com.taotao.cloud.order.biz.service.order.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.order.api.model.query.order.OrderComplaintCommunicationPageQuery;
import com.taotao.cloud.order.biz.model.entity.order.OrderComplaintCommunication;
import com.taotao.cloud.order.biz.mapper.order.IOrderComplainCommunicationMapper;
import com.taotao.cloud.order.biz.service.order.IOrderComplaintCommunicationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 交易投诉通信业务层实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:55:00
 */
@AllArgsConstructor
@Service
public class OrderComplaintCommunicationServiceImpl extends ServiceImpl<IOrderComplainCommunicationMapper, OrderComplaintCommunication> implements
	IOrderComplaintCommunicationService {

	@Override
	public Boolean addCommunication(OrderComplaintCommunication orderComplaintCommunication) {
		return this.save(orderComplaintCommunication);
	}

	@Override
	public IPage<OrderComplaintCommunication> getCommunication(OrderComplaintCommunicationPageQuery orderComplaintCommunicationPageQuery) {
		LambdaQueryWrapper<OrderComplaintCommunication> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(StrUtil.isNotEmpty(orderComplaintCommunicationPageQuery.getComplainId()),
			OrderComplaintCommunication::getComplainId, orderComplaintCommunicationPageQuery.getComplainId());
		queryWrapper.eq(StrUtil.isNotEmpty(orderComplaintCommunicationPageQuery.getOwner()),
			OrderComplaintCommunication::getOwner, orderComplaintCommunicationPageQuery.getOwner());
		queryWrapper.eq(StrUtil.isNotEmpty(orderComplaintCommunicationPageQuery.getOwnerName()),
			OrderComplaintCommunication::getOwnerName, orderComplaintCommunicationPageQuery.getOwnerName());
		queryWrapper.eq(StrUtil.isNotEmpty(orderComplaintCommunicationPageQuery.getOwnerId()),
			OrderComplaintCommunication::getOwnerId, orderComplaintCommunicationPageQuery.getOwnerId());

		return this.page(orderComplaintCommunicationPageQuery.buildMpPage(), queryWrapper);
	}
}

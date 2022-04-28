package com.taotao.cloud.order.biz.service.order.impl;

import cn.hutool.core.util.PageUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.order.api.vo.order.OrderComplaintCommunicationSearchParams;
import com.taotao.cloud.order.biz.entity.order.OrderComplaintCommunication;
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
	public IPage<OrderComplaintCommunication> getCommunication(
		OrderComplaintCommunicationSearchParams searchParams, PageVO pageVO) {
		return this.page(PageUtil.initPage(pageVO), searchParams.lambdaQueryWrapper());
	}
}

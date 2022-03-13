package com.taotao.cloud.order.biz.service.order.impl;

import cn.hutool.core.util.PageUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.order.api.vo.order.OrderComplaintCommunicationSearchParams;
import com.taotao.cloud.order.api.vo.order.OrderComplaintCommunicationVO;
import com.taotao.cloud.order.biz.entity.order.OrderComplaintCommunication;
import com.taotao.cloud.order.biz.mapper.order.OrderComplainCommunicationMapper;
import com.taotao.cloud.order.biz.service.order.OrderComplaintCommunicationService;
import org.springframework.stereotype.Service;

/**
 * 交易投诉通信业务层实现
 **/
@Service
public class OrderComplaintCommunicationServiceImpl extends ServiceImpl<OrderComplainCommunicationMapper, OrderComplaintCommunication> implements
	OrderComplaintCommunicationService {

    @Override
    public boolean addCommunication(OrderComplaintCommunicationVO communicationVO) {
        return this.save(communicationVO);
    }

    @Override
    public IPage<OrderComplaintCommunication> getCommunication(
	    OrderComplaintCommunicationSearchParams searchParams, PageVO pageVO) {
        return this.page(PageUtil.initPage(pageVO), searchParams.lambdaQueryWrapper());
    }
}

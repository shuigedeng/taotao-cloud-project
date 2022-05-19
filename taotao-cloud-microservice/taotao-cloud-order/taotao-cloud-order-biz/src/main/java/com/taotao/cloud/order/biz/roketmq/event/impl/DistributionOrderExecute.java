package com.taotao.cloud.order.biz.roketmq.event.impl;

import cn.hutool.core.date.DateTime;
import com.taotao.cloud.distribution.api.enums.DistributionOrderStatusEnum;
import com.taotao.cloud.distribution.api.feign.IFeignDistributionOrderService;
import com.taotao.cloud.order.api.dto.order.OrderMessage;
import com.taotao.cloud.order.api.enums.trade.AfterSaleStatusEnum;
import com.taotao.cloud.order.biz.entity.aftersale.AfterSale;
import com.taotao.cloud.order.biz.roketmq.event.AfterSaleStatusChangeEvent;
import com.taotao.cloud.order.biz.roketmq.event.OrderStatusChangeEvent;
import com.taotao.cloud.web.timetask.EveryDayExecute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 分销订单入库
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-05-16 17:34:50
 */
@Service
public class DistributionOrderExecute implements OrderStatusChangeEvent, EveryDayExecute,
	AfterSaleStatusChangeEvent {

    /**
     * 分销订单
     */
    @Autowired
    private IFeignDistributionOrderService distributionOrderService;

    // /**
    //  * 分销订单持久层
    //  */
    // @Resource
    // private DistributionOrderMapper distributionOrderMapper;


    @Override
    public void orderChange(OrderMessage orderMessage) {
		switch (orderMessage.getNewStatus()) {
			//订单带校验/订单代发货，则记录分销信息
			case TAKE, UNDELIVERED -> {
				//记录分销订单
				distributionOrderService.calculationDistribution(orderMessage.getOrderSn());
			}
			case CANCELLED -> {
				//修改分销订单状态
				distributionOrderService.cancelOrder(orderMessage.getOrderSn());
			}
			default -> {
			}
		}
    }

    @Override
    public void execute() {
        //计算分销提佣
		distributionOrderService.rebate(DistributionOrderStatusEnum.WAIT_BILL.name(), new DateTime());
        //修改分销订单状态
        distributionOrderService.updateStatus();
    }

    @Override
    public void afterSaleStatusChange(AfterSale afterSale) {
        if (afterSale.getServiceStatus().equals(AfterSaleStatusEnum.COMPLETE.name())) {
            distributionOrderService.refundOrder(afterSale.getSn());
        }
    }

}

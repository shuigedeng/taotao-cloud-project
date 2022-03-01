//package com.taotao.cloud.stream.consumer.event.impl;
//
//import cn.hutool.core.date.DateTime;
//import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
//import com.taotao.cloud.stream.consumer.event.AfterSaleStatusChangeEvent;
//import com.taotao.cloud.stream.consumer.event.OrderStatusChangeEvent;
//import javax.annotation.Resource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
///**
// * 分销订单入库
// */
//@Service
//public class DistributionOrderExecute implements OrderStatusChangeEvent, EveryDayExecute,
//	AfterSaleStatusChangeEvent {
//
//    /**
//     * 分销订单
//     */
//    @Autowired
//    private DistributionOrderService distributionOrderService;
//    /**
//     * 分销订单持久层
//     */
//    @Resource
//    private DistributionOrderMapper distributionOrderMapper;
//
//
//    @Override
//    public void orderChange(OrderMessage orderMessage) {
//
//        switch (orderMessage.getNewStatus()) {
//            //订单带校验/订单代发货，则记录分销信息
//            case TAKE:
//            case UNDELIVERED: {
//                //记录分销订单
//                distributionOrderService.calculationDistribution(orderMessage.getOrderSn());
//                break;
//            }
//            case CANCELLED: {
//                //修改分销订单状态
//                distributionOrderService.cancelOrder(orderMessage.getOrderSn());
//                break;
//            }
//            default: {
//                break;
//            }
//        }
//    }
//
//    @Override
//    public void execute() {
//        //计算分销提佣
//        distributionOrderMapper.rebate(DistributionOrderStatusEnum.WAIT_BILL.name(), new DateTime());
//
//        //修改分销订单状态
//        distributionOrderService.update(new LambdaUpdateWrapper<DistributionOrder>()
//                .eq(DistributionOrder::getDistributionOrderStatus, DistributionOrderStatusEnum.WAIT_BILL.name())
//                .le(DistributionOrder::getSettleCycle, new DateTime())
//                .set(DistributionOrder::getDistributionOrderStatus, DistributionOrderStatusEnum.WAIT_CASH.name()));
//
//    }
//
//    @Override
//    public void afterSaleStatusChange(AfterSale afterSale) {
//        if (afterSale.getServiceStatus().equals(AfterSaleStatusEnum.COMPLETE.name())) {
//            distributionOrderService.refundOrder(afterSale.getSn());
//        }
//    }
//
//}

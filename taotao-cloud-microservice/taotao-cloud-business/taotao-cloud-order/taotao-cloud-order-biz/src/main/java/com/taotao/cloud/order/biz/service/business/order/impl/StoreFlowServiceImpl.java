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

package com.taotao.cloud.order.biz.service.business.order.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.boot.common.utils.bean.BeanUtils;
import com.taotao.boot.common.utils.id.IdGeneratorUtils;
import com.taotao.boot.common.utils.lang.StringUtils;
import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.boot.common.utils.number.CurrencyUtils;
import com.taotao.cloud.order.api.enums.order.FlowTypeEnum;
import com.taotao.cloud.order.api.enums.order.OrderPromotionTypeEnum;
import com.taotao.cloud.order.api.enums.order.PayStatusEnum;
import com.taotao.cloud.order.sys.model.page.distribution.DistributionPageQuery;
import com.taotao.cloud.order.sys.model.page.order.StoreFlowPageQuery;
import com.taotao.cloud.order.sys.model.page.store.StorePageQuery;
import com.taotao.cloud.order.biz.mapper.order.IStoreFlowMapper;
import com.taotao.cloud.order.biz.model.entity.aftersale.AfterSale;
import com.taotao.cloud.order.biz.model.entity.order.Order;
import com.taotao.cloud.order.biz.model.entity.order.OrderItem;
import com.taotao.cloud.order.biz.model.entity.order.StoreFlow;
import com.taotao.cloud.order.biz.service.business.order.IOrderItemService;
import com.taotao.cloud.order.biz.service.business.order.IOrderService;
import com.taotao.cloud.order.biz.service.business.order.IStoreFlowService;
import com.taotao.cloud.payment.api.feign.RefundLogApi;
import com.taotao.cloud.store.api.feign.IFeignBillApi;
import com.taotao.cloud.store.api.model.vo.BillVO;
import com.taotao.cloud.store.api.model.vo.StoreFlowPayDownloadVO;
import com.taotao.cloud.store.api.model.vo.StoreFlowRefundDownloadVO;
import java.util.List;
import lombok.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 商家订单流水业务层实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:55:17
 */
@AllArgsConstructor
@Service
@Transactional(rollbackFor = Exception.class)
public class StoreFlowServiceImpl extends ServiceImpl<IStoreFlowMapper, StoreFlow> implements IStoreFlowService {

    /** 订单 */
    private final IOrderService orderService;
    /** 订单货物 */
    private final IOrderItemService orderItemService;
    /** 退款日志 */
    private final RefundLogApi feignRefundLogApi;

    private final IFeignBillApi feignBillApi;

    @Override
    public void payOrder(String orderSn) {
        // 根据订单编号获取子订单列表
        List<OrderItem> orderItems = orderItemService.getByOrderSn(orderSn);
        // 根据订单编号获取订单数据
        Order order = orderService.getBySn(orderSn);

        // 如果查询到多条支付记录，打印日志
        if (order.getPayStatus().equals(PayStatusEnum.PAID.name())) {
            LogUtils.error("订单[{}]检测到重复付款，请处理", orderSn);
        }

        // 获取订单促销类型,如果为促销订单则获取促销商品并获取结算价
        String orderPromotionType = order.getOrderPromotionType();
        // 循环子订单记录流水
        for (OrderItem item : orderItems) {
            StoreFlow storeFlow = new StoreFlow();
            BeanUtils.copyProperties(item, storeFlow);

            // 入账
            storeFlow.setId(IdGeneratorUtils.getId());
            storeFlow.setFlowType(FlowTypeEnum.PAY.name());
            storeFlow.setSn(IdGeneratorUtils.createStr("SF"));
            storeFlow.setOrderSn(item.getOrderSn());
            storeFlow.setOrderItemSn(item.getSn());
            storeFlow.setStoreId(order.getStoreId());
            storeFlow.setStoreName(order.getStoreName());
            storeFlow.setMemberId(order.getMemberId());
            storeFlow.setMemberName(order.getMemberName());
            storeFlow.setGoodsName(item.getGoodsName());

            storeFlow.setOrderPromotionType(item.getPromotionType());

            // 计算平台佣金
            storeFlow.setFinalPrice(item.getPriceDetailDTO().getFlowPrice());
            storeFlow.setCommissionPrice(item.getPriceDetailDTO().getPlatFormCommission());
            storeFlow.setDistributionRebate(item.getPriceDetailDTO().getDistributionCommission());
            storeFlow.setBillPrice(item.getPriceDetailDTO().getBillPrice());
            // 兼容为空，以及普通订单操作
            if (StringUtils.isNotEmpty(orderPromotionType)) {
                if (orderPromotionType.equals(OrderPromotionTypeEnum.NORMAL.name())) {
                    // 普通订单操作
                }
                // 如果为砍价活动，填写砍价结算价
                else if (orderPromotionType.equals(OrderPromotionTypeEnum.KANJIA.name())) {
                    storeFlow.setKanjiaSettlementPrice(item.getPriceDetailDTO().getSettlementPrice());
                }
                // 如果为砍价活动，填写砍价结算价
                else if (orderPromotionType.equals(OrderPromotionTypeEnum.POINTS.name())) {
                    storeFlow.setPointSettlementPrice(item.getPriceDetailDTO().getSettlementPrice());
                }
            }
            // 添加支付方式
            storeFlow.setPaymentName(order.getPaymentMethod());
            // 添加第三方支付流水号
            storeFlow.setTransactionId(order.getReceivableNo());

            // 添加付款交易流水
            this.save(storeFlow);
        }
    }

    @Override
    public void refundOrder(AfterSale afterSale) {
        StoreFlow storeFlow = new StoreFlow();
        // 退款
        storeFlow.setFlowType(FlowTypeEnum.REFUND.name());
        storeFlow.setSn(IdGeneratorUtils.createStr("SF"));
        storeFlow.setRefundSn(afterSale.getSn());
        storeFlow.setOrderSn(afterSale.getOrderSn());
        storeFlow.setOrderItemSn(afterSale.getOrderItemSn());
        storeFlow.setStoreId(afterSale.getStoreId());
        storeFlow.setStoreName(afterSale.getStoreName());
        storeFlow.setMemberId(afterSale.getMemberId());
        storeFlow.setMemberName(afterSale.getMemberName());
        storeFlow.setGoodsId(afterSale.getGoodsId());
        storeFlow.setGoodsName(afterSale.getGoodsName());
        storeFlow.setSkuId(afterSale.getSkuId());
        storeFlow.setImage(afterSale.getGoodsImage());
        storeFlow.setSpecs(afterSale.getSpecs());

        // 获取付款信息
        StoreFlow payStoreFlow = this.getOne(new LambdaUpdateWrapper<StoreFlow>()
                .eq(StoreFlow::getOrderItemSn, afterSale.getOrderItemSn())
                .eq(StoreFlow::getFlowType, FlowTypeEnum.PAY));
        storeFlow.setNum(afterSale.getNum());
        storeFlow.setCategoryId(payStoreFlow.getCategoryId());
        // 佣金
        storeFlow.setCommissionPrice(CurrencyUtils.mul(
                CurrencyUtils.div(payStoreFlow.getCommissionPrice(), payStoreFlow.getNum()), afterSale.getNum()));
        // 分销佣金
        storeFlow.setDistributionRebate(CurrencyUtils.mul(
                CurrencyUtils.div(payStoreFlow.getDistributionRebate(), payStoreFlow.getNum()), afterSale.getNum()));
        // 流水金额
        storeFlow.setFinalPrice(afterSale.getActualRefundPrice());
        // 最终结算金额
        storeFlow.setBillPrice(CurrencyUtils.add(
                CurrencyUtils.add(storeFlow.getFinalPrice(), storeFlow.getDistributionRebate()),
                storeFlow.getCommissionPrice()));
        // 获取第三方支付流水号
        RefundLogVO refundLog = feignRefundLogApi.queryByAfterSaleSn(afterSale.getSn());
        storeFlow.setTransactionId(refundLog.getReceivableNo());
        storeFlow.setPaymentName(refundLog.getPaymentName());
        this.save(storeFlow);
    }

    @Override
    public IPage<StoreFlow> getStoreFlow(StoreFlowPageQuery storeFlowPageQuery) {
        return this.page(storeFlowPageQuery.buildMpPage(), generatorQueryWrapper(storeFlowPageQuery));
    }

    @Override
    public StoreFlow queryOne(StoreFlowPageQuery storeFlowQueryDTO) {
        return this.getOne(generatorQueryWrapper(storeFlowQueryDTO));
    }

    @Override
    public List<StoreFlowPayDownloadVO> getStoreFlowPayDownloadVO(StoreFlowPageQuery storeFlowQueryDTO) {
        return baseMapper.getStoreFlowPayDownloadVO(generatorQueryWrapper(storeFlowQueryDTO));
    }

    @Override
    public List<StoreFlowRefundDownloadVO> getStoreFlowRefundDownloadVO(StoreFlowPageQuery storeFlowQueryDTO) {
        return baseMapper.getStoreFlowRefundDownloadVO(generatorQueryWrapper(storeFlowQueryDTO));
    }

    @Override
    public IPage<StoreFlow> getStoreFlow(StorePageQuery storePageQuery) {
        BillVO bill = feignBillApi.getById(storePageQuery.getId());
        return this.getStoreFlow(StoreFlowPageQuery.builder()
                .type(type)
                .pageVO(pageVO)
                .bill(bill)
                .build());
    }

    @Override
    public IPage<StoreFlow> getDistributionFlow(DistributionPageQuery distributionPageQuery) {
        BillVO bill = feignBillApi.getById(distributionPageQuery.getId());
        return this.getStoreFlow(
                StoreFlowPageQuery.builder().pageVO(pageVO).bill(bill).build());
    }

    @Override
    public List<StoreFlow> listStoreFlow(StoreFlowPageQuery storeFlowQueryDTO) {
        return this.list(generatorQueryWrapper(storeFlowQueryDTO));
    }

    /**
     * 生成查询wrapper
     *
     * @param storeFlowPageQuery 搜索参数
     * @return 查询wrapper
     */
    private LambdaQueryWrapper<StoreFlow> generatorQueryWrapper(StoreFlowPageQuery storeFlowPageQuery) {
        LambdaQueryWrapper<StoreFlow> lambdaQueryWrapper = Wrappers.lambdaQuery();
        // 分销订单过滤是否判定
        lambdaQueryWrapper.isNotNull(
                storeFlowPageQuery.getJustDistribution() != null && storeFlowPageQuery.getJustDistribution(),
                StoreFlow::getDistributionRebate);

        // 流水类型判定
        lambdaQueryWrapper.eq(
                StringUtils.isNotEmpty(storeFlowPageQuery.getType()),
                StoreFlow::getFlowType,
                storeFlowPageQuery.getType());

        // 售后编号判定
        lambdaQueryWrapper.eq(
                StringUtils.isNotEmpty(storeFlowPageQuery.getRefundSn()),
                StoreFlow::getRefundSn,
                storeFlowPageQuery.getRefundSn());

        // 售后编号判定
        lambdaQueryWrapper.eq(
                StringUtils.isNotEmpty(storeFlowPageQuery.getOrderSn()),
                StoreFlow::getOrderSn,
                storeFlowPageQuery.getOrderSn());

        // 结算单非空，则校对结算单参数
        if (storeFlowPageQuery.getBill() != null) {
            StoreFlowPageQuery.BillDTO bill = storeFlowPageQuery.getBill();
            lambdaQueryWrapper.eq(StringUtils.isNotEmpty(bill.getStoreId()), StoreFlow::getStoreId, bill.getStoreId());
            lambdaQueryWrapper.between(
                    bill.getStartTime() != null && bill.getEndTime() != null,
                    StoreFlow::getCreateTime,
                    bill.getStartTime(),
                    bill.getEndTime());
        }
        return lambdaQueryWrapper;
    }
}

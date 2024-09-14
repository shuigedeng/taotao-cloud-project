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
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.boot.common.utils.bean.BeanUtils;
import com.taotao.boot.common.utils.common.IdGeneratorUtils;
import com.taotao.boot.common.utils.lang.StringUtils;
import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.boot.common.utils.number.CurrencyUtils;
import com.taotao.cloud.order.application.service.order.IOrderItemService;
import com.taotao.cloud.order.application.service.order.IOrderService;
import com.taotao.cloud.order.application.service.order.IStoreFlowService;
import com.taotao.cloud.order.infrastructure.persistent.mapper.order.IStoreFlowMapper;
import com.taotao.cloud.order.infrastructure.persistent.po.order.StoreFlowPO;
import java.util.List;
import lombok.AllArgsConstructor;
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
public class StoreFlowServiceImpl extends ServiceImpl<IStoreFlowMapper, StoreFlowPO> implements
	IStoreFlowService {

    /** 订单 */
    private final IOrderService orderService;
    /** 订单货物 */
    private final IOrderItemService orderItemService;
    /** 退款日志 */
    private final IFeignRefundLogApi feignRefundLogApi;

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
            StoreFlowPO storeFlowPO = new StoreFlowPO();
            BeanUtils.copyProperties(item, storeFlowPO);

            // 入账
            storeFlowPO.setId(IdGeneratorUtils.getId());
            storeFlowPO.setFlowType(FlowTypeEnum.PAY.name());
            storeFlowPO.setSn(IdGeneratorUtils.createStr("SF"));
            storeFlowPO.setOrderSn(item.getOrderSn());
            storeFlowPO.setOrderItemSn(item.getSn());
            storeFlowPO.setStoreId(order.getStoreId());
            storeFlowPO.setStoreName(order.getStoreName());
            storeFlowPO.setMemberId(order.getMemberId());
            storeFlowPO.setMemberName(order.getMemberName());
            storeFlowPO.setGoodsName(item.getGoodsName());

            storeFlowPO.setOrderPromotionType(item.getPromotionType());

            // 计算平台佣金
            storeFlowPO.setFinalPrice(item.getPriceDetailDTO().getFlowPrice());
            storeFlowPO.setCommissionPrice(item.getPriceDetailDTO().getPlatFormCommission());
            storeFlowPO.setDistributionRebate(item.getPriceDetailDTO().getDistributionCommission());
            storeFlowPO.setBillPrice(item.getPriceDetailDTO().getBillPrice());
            // 兼容为空，以及普通订单操作
            if (StringUtils.isNotEmpty(orderPromotionType)) {
                if (orderPromotionType.equals(OrderPromotionTypeEnum.NORMAL.name())) {
                    // 普通订单操作
                }
                // 如果为砍价活动，填写砍价结算价
                else if (orderPromotionType.equals(OrderPromotionTypeEnum.KANJIA.name())) {
                    storeFlowPO.setKanjiaSettlementPrice(item.getPriceDetailDTO().getSettlementPrice());
                }
                // 如果为砍价活动，填写砍价结算价
                else if (orderPromotionType.equals(OrderPromotionTypeEnum.POINTS.name())) {
                    storeFlowPO.setPointSettlementPrice(item.getPriceDetailDTO().getSettlementPrice());
                }
            }
            // 添加支付方式
            storeFlowPO.setPaymentName(order.getPaymentMethod());
            // 添加第三方支付流水号
            storeFlowPO.setTransactionId(order.getReceivableNo());

            // 添加付款交易流水
            this.save(storeFlowPO);
        }
    }

    @Override
    public void refundOrder(AfterSale afterSale) {
        StoreFlowPO storeFlowPO = new StoreFlowPO();
        // 退款
        storeFlowPO.setFlowType(FlowTypeEnum.REFUND.name());
        storeFlowPO.setSn(IdGeneratorUtils.createStr("SF"));
        storeFlowPO.setRefundSn(afterSale.getSn());
        storeFlowPO.setOrderSn(afterSale.getOrderSn());
        storeFlowPO.setOrderItemSn(afterSale.getOrderItemSn());
        storeFlowPO.setStoreId(afterSale.getStoreId());
        storeFlowPO.setStoreName(afterSale.getStoreName());
        storeFlowPO.setMemberId(afterSale.getMemberId());
        storeFlowPO.setMemberName(afterSale.getMemberName());
        storeFlowPO.setGoodsId(afterSale.getGoodsId());
        storeFlowPO.setGoodsName(afterSale.getGoodsName());
        storeFlowPO.setSkuId(afterSale.getSkuId());
        storeFlowPO.setImage(afterSale.getGoodsImage());
        storeFlowPO.setSpecs(afterSale.getSpecs());

        // 获取付款信息
        StoreFlowPO payStoreFlowPO = this.getOne(new LambdaUpdateWrapper<StoreFlowPO>()
                .eq(StoreFlowPO::getOrderItemSn, afterSale.getOrderItemSn())
                .eq(StoreFlowPO::getFlowType, FlowTypeEnum.PAY));
        storeFlowPO.setNum(afterSale.getNum());
        storeFlowPO.setCategoryId(payStoreFlowPO.getCategoryId());
        // 佣金
        storeFlowPO.setCommissionPrice(CurrencyUtils.mul(
                CurrencyUtils.div(payStoreFlowPO.getCommissionPrice(), payStoreFlowPO.getNum()), afterSale.getNum()));
        // 分销佣金
        storeFlowPO.setDistributionRebate(CurrencyUtils.mul(
                CurrencyUtils.div(payStoreFlowPO.getDistributionRebate(), payStoreFlowPO.getNum()), afterSale.getNum()));
        // 流水金额
        storeFlowPO.setFinalPrice(afterSale.getActualRefundPrice());
        // 最终结算金额
        storeFlowPO.setBillPrice(CurrencyUtils.add(
                CurrencyUtils.add(storeFlowPO.getFinalPrice(), storeFlowPO.getDistributionRebate()),
                storeFlowPO.getCommissionPrice()));
        // 获取第三方支付流水号
        RefundLogVO refundLog = feignRefundLogApi.queryByAfterSaleSn(afterSale.getSn());
        storeFlowPO.setTransactionId(refundLog.getReceivableNo());
        storeFlowPO.setPaymentName(refundLog.getPaymentName());
        this.save(storeFlowPO);
    }

    @Override
    public IPage<StoreFlowPO> getStoreFlow(StoreFlowPageQuery storeFlowPageQuery) {
        return this.page(storeFlowPageQuery.buildMpPage(), generatorQueryWrapper(storeFlowPageQuery));
    }

    @Override
    public StoreFlowPO queryOne(StoreFlowPageQuery storeFlowQueryDTO) {
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
    public IPage<StoreFlowPO> getStoreFlow(StorePageQuery storePageQuery) {
        BillVO bill = feignBillApi.getById(storePageQuery.getId());
        return this.getStoreFlow(StoreFlowPageQuery.builder()
                .type(type)
                .pageVO(pageVO)
                .bill(bill)
                .build());
    }

    @Override
    public IPage<StoreFlowPO> getDistributionFlow(DistributionPageQuery distributionPageQuery) {
        BillVO bill = feignBillApi.getById(distributionPageQuery.getId());
        return this.getStoreFlow(
                StoreFlowPageQuery.builder().pageVO(pageVO).bill(bill).build());
    }

    @Override
    public List<StoreFlowPO> listStoreFlow(StoreFlowPageQuery storeFlowQueryDTO) {
        return this.list(generatorQueryWrapper(storeFlowQueryDTO));
    }

    /**
     * 生成查询wrapper
     *
     * @param storeFlowPageQuery 搜索参数
     * @return 查询wrapper
     */
    private LambdaQueryWrapper<StoreFlowPO> generatorQueryWrapper(StoreFlowPageQuery storeFlowPageQuery) {
        LambdaQueryWrapper<StoreFlowPO> lambdaQueryWrapper = Wrappers.lambdaQuery();
        // 分销订单过滤是否判定
        lambdaQueryWrapper.isNotNull(
                storeFlowPageQuery.getJustDistribution() != null && storeFlowPageQuery.getJustDistribution(),
                StoreFlowPO::getDistributionRebate);

        // 流水类型判定
        lambdaQueryWrapper.eq(
                StringUtils.isNotEmpty(storeFlowPageQuery.getType()),
                StoreFlowPO::getFlowType,
                storeFlowPageQuery.getType());

        // 售后编号判定
        lambdaQueryWrapper.eq(
                StringUtils.isNotEmpty(storeFlowPageQuery.getRefundSn()),
                StoreFlowPO::getRefundSn,
                storeFlowPageQuery.getRefundSn());

        // 售后编号判定
        lambdaQueryWrapper.eq(
                StringUtils.isNotEmpty(storeFlowPageQuery.getOrderSn()),
                StoreFlowPO::getOrderSn,
                storeFlowPageQuery.getOrderSn());

        // 结算单非空，则校对结算单参数
        if (storeFlowPageQuery.getBill() != null) {
            StoreFlowPageQuery.BillDTO bill = storeFlowPageQuery.getBill();
            lambdaQueryWrapper.eq(StringUtils.isNotEmpty(bill.getStoreId()), StoreFlowPO::getStoreId, bill.getStoreId());
            lambdaQueryWrapper.between(
                    bill.getStartTime() != null && bill.getEndTime() != null,
                    StoreFlowPO::getCreateTime,
                    bill.getStartTime(),
                    bill.getEndTime());
        }
        return lambdaQueryWrapper;
    }
}

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

package com.taotao.cloud.distribution.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.boot.common.model.result.Result;
import com.taotao.boot.common.utils.number.CurrencyUtils;
import com.taotao.cloud.distribution.api.enums.DistributionOrderStatusEnum;
import com.taotao.cloud.distribution.api.model.query.DistributionOrderPageQuery;
import com.taotao.cloud.distribution.biz.mapper.DistributionOrderMapper;
import com.taotao.cloud.distribution.biz.model.entity.Distribution;
import com.taotao.cloud.distribution.biz.model.entity.DistributionOrder;
import com.taotao.cloud.distribution.biz.service.DistributionOrderService;
import com.taotao.cloud.distribution.biz.service.DistributionService;
import com.taotao.cloud.order.api.enums.order.PayStatusEnum;
import com.taotao.cloud.order.api.feign.OrderApi;
import com.taotao.cloud.order.api.model.page.order.StoreFlowPageQuery;
import com.taotao.cloud.sys.api.dto.DistributionSetting;
import com.taotao.cloud.sys.api.enums.SettingCategoryEnum;
import com.taotao.cloud.sys.api.model.vo.setting.SettingVO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 分销订单接口实现 */
@Service
public class DistributionOrderServiceImpl extends ServiceImpl<DistributionOrderMapper, DistributionOrder>
        implements DistributionOrderService {

    /** 订单 */
    @Autowired
    private OrderApi orderApi;
    /** 店铺流水 */
    @Autowired
    private StoreFlowService storeFlowService;
    /** 分销员 */
    @Autowired
    private DistributionService distributionService;
    /** 系统设置 */
    @Autowired
    private IFeignSettingApi feignSettingApi;

    @Override
    public IPage<DistributionOrder> getDistributionOrderPage(DistributionOrderPageQuery distributionOrderPageQuery) {
        return this.page(distributionOrderPageQuery.buildMpPage(), distributionOrderPageQuery.queryWrapper());
    }

    /**
     * 1.查看订单是否为分销订单 2.查看店铺流水计算分销总佣金 3.修改分销员的分销总金额、冻结金额
     *
     * @param orderSn 订单编号
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void calculationDistribution(String orderSn) {

        // 根据订单编号获取订单数据
        Order order = orderApi.getBySn(orderSn);

        // 判断是否为分销订单，如果为分销订单则获取分销佣金
        if (order.getDistributionId() != null) {
            // 根据订单编号获取有分销金额的店铺流水记录
            List<StoreFlow> storeFlowList = storeFlowService.listStoreFlow(StoreFlowPageQuery.builder()
                    .justDistribution(true)
                    .orderSn(orderSn)
                    .build());
            BigDecimal rebate = 0.0;
            // 循环店铺流水记录判断是否包含分销商品
            // 包含分销商品则进行记录分销订单、计算分销总额
            for (StoreFlow storeFlow : storeFlowList) {
                rebate = CurrencyUtils.add(rebate, storeFlow.getDistributionRebate());
                DistributionOrder distributionOrder = new DistributionOrder(storeFlow);
                distributionOrder.setDistributionId(order.getDistributionId());
                // 分销员信息
                Distribution distribution = distributionService.getById(order.getDistributionId());
                distributionOrder.setDistributionName(distribution.getMemberName());

                // 设置结算天数(解冻日期)
                Result<SettingVO> settingResult = feignSettingApi.get(SettingCategoryEnum.DISTRIBUTION_SETTING.name());
                DistributionSetting distributionSetting =
                        JSONUtil.toBean(settingResult.getSettingValue(), DistributionSetting.class);
                // 默认解冻1天
                if (distributionSetting.getCashDay().equals(0)) {
                    distributionOrder.setSettleCycle(new DateTime());
                } else {
                    DateTime dateTime = new DateTime();
                    dateTime = dateTime.offsetNew(DateField.DAY_OF_MONTH, distributionSetting.getCashDay());
                    distributionOrder.setSettleCycle(dateTime);
                }

                // 添加分销订单
                this.save(distributionOrder);

                // 记录会员的分销总额
                if (rebate != 0.0) {
                    distributionService.addRebate(rebate, order.getDistributionId());

                    // 如果天数写0则立即进行结算
                    if (distributionSetting.getCashDay().equals(0)) {
                        DateTime dateTime = new DateTime();
                        dateTime = dateTime.offsetNew(DateField.MINUTE, 5);
                        // 计算分销提佣
                        this.baseMapper.rebate(DistributionOrderStatusEnum.WAIT_BILL.name(), dateTime);

                        // 修改分销订单状态
                        this.update(new LambdaUpdateWrapper<DistributionOrder>()
                                .eq(
                                        DistributionOrder::getDistributionOrderStatus,
                                        DistributionOrderStatusEnum.WAIT_BILL.name())
                                .le(DistributionOrder::getSettleCycle, dateTime)
                                .set(
                                        DistributionOrder::getDistributionOrderStatus,
                                        DistributionOrderStatusEnum.WAIT_CASH.name()));
                    }
                }
            }
        }
    }

    /**
     * 1.获取订单判断是否为已付款的分销订单 2.查看店铺流水记录分销佣金 3.修改分销员的分销总金额、可提现金额
     *
     * @param orderSn 订单编号
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(String orderSn) {
        // 根据订单编号获取订单数据
        Order order = orderApi.getBySn(orderSn);

        // 判断是否为已付款的分销订单，则获取分销佣金
        if (order.getDistributionId() != null && order.getPayStatus().equals(PayStatusEnum.PAID.name())) {

            // 根据订单编号获取有分销金额的店铺流水记录
            List<DistributionOrder> distributionOrderList =
                    this.list(new LambdaQueryWrapper<DistributionOrder>().eq(DistributionOrder::getOrderSn, orderSn));

            // 如果没有分销定单，则直接返回
            if (distributionOrderList.isEmpty()) {
                return;
            }
            // 分销金额
            BigDecimal rebate = 0.0;

            // 包含分销商品则进行记录分销订单、计算分销总额
            for (DistributionOrder distributionOrder : distributionOrderList) {
                rebate = CurrencyUtils.add(rebate, distributionOrder.getRebate());
            }

            // 如果包含分销商品则记录会员的分销总额
            if (rebate != 0.0) {
                distributionService.subCanRebate(CurrencyUtils.sub(0, rebate), order.getDistributionId());
            }
        }

        // 修改分销订单的状态
        this.update(new LambdaUpdateWrapper<DistributionOrder>()
                .eq(DistributionOrder::getOrderSn, orderSn)
                .set(DistributionOrder::getDistributionOrderStatus, DistributionOrderStatusEnum.CANCEL.name()));
    }

    @Override
    public void refundOrder(String afterSaleSn) {
        // 判断是否为分销订单
        StoreFlow storeFlow = storeFlowService.queryOne(StoreFlowPageQuery.builder()
                .justDistribution(true)
                .refundSn(afterSaleSn)
                .build());
        if (storeFlow != null) {

            // 获取收款分销订单
            DistributionOrder distributionOrder = this.getOne(new LambdaQueryWrapper<DistributionOrder>()
                    .eq(DistributionOrder::getOrderItemSn, storeFlow.getOrderItemSn()));
            // 分销订单不存在，则直接返回
            if (distributionOrder == null) {
                return;
            }
            if (distributionOrder.getDistributionOrderStatus().equals(DistributionOrderStatusEnum.WAIT_BILL.name())) {
                this.update(new LambdaUpdateWrapper<DistributionOrder>()
                        .eq(DistributionOrder::getOrderItemSn, storeFlow.getOrderItemSn())
                        .set(DistributionOrder::getDistributionOrderStatus, DistributionOrderStatusEnum.CANCEL.name()));
            }
            // 如果已结算则创建退款分销订单
            else {
                // 修改分销员提成金额
                distributionService.subCanRebate(
                        CurrencyUtils.sub(0, storeFlow.getDistributionRebate()), distributionOrder.getDistributionId());
            }
        }
    }
}

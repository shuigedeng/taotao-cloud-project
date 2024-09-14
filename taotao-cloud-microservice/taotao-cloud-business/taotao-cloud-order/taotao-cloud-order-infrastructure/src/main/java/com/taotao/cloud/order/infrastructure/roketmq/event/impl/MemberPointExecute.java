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

package com.taotao.cloud.order.infrastructure.roketmq.event.impl;

import com.taotao.boot.common.utils.lang.StringUtils;
import com.taotao.boot.common.utils.number.CurrencyUtils;
import com.taotao.cloud.member.api.enums.PointTypeEnum;
import com.taotao.cloud.member.api.feign.IFeignMemberApi;
import com.taotao.cloud.order.api.enums.order.OrderPromotionTypeEnum;
import com.taotao.cloud.order.api.enums.order.PayStatusEnum;
import com.taotao.cloud.order.api.enums.trade.AfterSaleStatusEnum;
import com.taotao.cloud.order.sys.model.message.OrderMessage;
import com.taotao.cloud.order.infrastructure.model.entity.aftersale.AfterSale;
import com.taotao.cloud.order.infrastructure.model.entity.order.Order;
import com.taotao.cloud.order.infrastructure.roketmq.event.AfterSaleStatusChangeEvent;
import com.taotao.cloud.order.infrastructure.roketmq.event.OrderStatusChangeEvent;
import com.taotao.cloud.order.infrastructure.service.business.order.IOrderService;
import com.taotao.cloud.sys.api.enums.SettingCategoryEnum;
import com.taotao.cloud.sys.api.feign.IFeignSettingApi;
import com.taotao.cloud.sys.api.model.vo.setting.PointSettingVO;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 会员积分
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-05-16 17:35:57
 */
@Service
public class MemberPointExecute implements OrderStatusChangeEvent, AfterSaleStatusChangeEvent {

    /** 配置 */
    @Autowired
    private IFeignSettingApi settingApi;
    /** 会员 */
    @Autowired
    private IFeignMemberApi memberApi;
    /** 订单 */
    @Autowired
    private IOrderService orderService;

    /**
     * 非积分订单订单完成后赠送积分
     *
     * @param orderMessage 订单消息
     */
    @Override
    public void orderChange(OrderMessage orderMessage) {
        switch (orderMessage.newStatus()) {
            case CANCELLED -> {
                Order order = orderService.getBySn(orderMessage.orderSn());
                Long point = order.getPriceDetailDTO().payPoint();
                if (point <= 0) {
                    return;
                }
                // 如果未付款，则不去要退回相关代码执行
                if (order.getPayStatus().equals(PayStatusEnum.UNPAID.name())) {
                    return;
                }
                String content = "订单取消，积分返还：" + point + "分";
                // 赠送会员积分
                memberApi.updateMemberPoint(point, PointTypeEnum.INCREASE.name(), order.getMemberId(), content);
            }
            case COMPLETED -> {
                Order order = orderService.getBySn(orderMessage.orderSn());
                // 如果是积分订单 则直接返回
                if (StringUtils.isNotEmpty(order.getOrderPromotionType())
                        && order.getOrderPromotionType().equals(OrderPromotionTypeEnum.POINTS.name())) {
                    return;
                }
                // 获取积分设置
                PointSettingVO pointSetting = getPointSetting();
                if (pointSetting.getConsumer() == 0) {
                    return;
                }
                // 计算赠送积分数量
                BigDecimal point = CurrencyUtils.mul(pointSetting.getConsumer(), order.getFlowPrice(), 0);
                // 赠送会员积分
                memberApi.updateMemberPoint(
                        point.longValue(),
                        PointTypeEnum.INCREASE.name(),
                        order.getMemberId(),
                        "会员下单，赠送积分" + point + "分");
            }
            default -> {}
        }
    }

    /**
     * 提交售后后扣除积分
     *
     * @param afterSale 售后
     */
    @Override
    public void afterSaleStatusChange(AfterSale afterSale) {
        if (afterSale.getServiceStatus().equals(AfterSaleStatusEnum.COMPLETE.name())) {
            // 获取积分设置
            PointSettingVO pointSetting = getPointSetting();
            // 计算扣除积分数量
            BigDecimal point = CurrencyUtils.mul(pointSetting.getMoney(), afterSale.getActualRefundPrice(), 0);
            // 扣除会员积分
            memberApi.updateMemberPoint(
                    point.longValue(), PointTypeEnum.REDUCE.name(), afterSale.getMemberId(), "会员退款，回退积分" + point + "分");
        }
    }

    /**
     * 获取积分设置
     *
     * @return 积分设置
     */
    private PointSettingVO getPointSetting() {
        return settingApi.getPointSetting(SettingCategoryEnum.POINT_SETTING.name());
    }
}

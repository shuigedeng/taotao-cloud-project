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

import com.taotao.boot.common.utils.number.CurrencyUtils;
import com.taotao.cloud.member.api.enums.PointTypeEnum;
import com.taotao.cloud.member.api.feign.IFeignMemberApi;
import com.taotao.cloud.order.api.enums.order.OrderStatusEnum;
import com.taotao.cloud.order.sys.model.message.OrderMessage;
import com.taotao.cloud.order.infrastructure.model.entity.order.Order;
import com.taotao.cloud.order.infrastructure.roketmq.event.OrderStatusChangeEvent;
import com.taotao.cloud.order.infrastructure.service.business.order.IOrderService;
import com.taotao.cloud.sys.api.enums.SettingCategoryEnum;
import com.taotao.cloud.sys.api.feign.IFeignSettingApi;
import com.taotao.cloud.sys.api.model.vo.setting.ExperienceSettingVO;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 会员经验值
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-05-16 17:35:33
 */
@Service
public class MemberExperienceExecute implements OrderStatusChangeEvent {

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
     * 完成订单赠送经验值
     *
     * @param orderMessage 订单消息
     */
    @Override
    public void orderChange(OrderMessage orderMessage) {
        if (orderMessage.newStatus().equals(OrderStatusEnum.COMPLETED)) {
            // 获取经验值设置
            ExperienceSettingVO experienceSetting = getExperienceSetting();
            // 获取订单信息
            Order order = orderService.getBySn(orderMessage.orderSn());
            // 计算赠送经验值数量
            BigDecimal point = CurrencyUtils.mul(experienceSetting.getMoney(), order.getFlowPrice(), 0);
            // 赠送会员经验值
            memberApi.updateMemberPoint(
                    point.longValue(), PointTypeEnum.INCREASE.name(), order.getMemberId(), "会员下单，赠送经验值" + point + "分");
        }
    }

    /**
     * 获取经验值设置
     *
     * @return {@link ExperienceSettingVO }
     * @since 2022-05-16 17:35:40
     */
    private ExperienceSettingVO getExperienceSetting() {
        return settingApi.getExperienceSetting(SettingCategoryEnum.EXPERIENCE_SETTING.name());
    }
}

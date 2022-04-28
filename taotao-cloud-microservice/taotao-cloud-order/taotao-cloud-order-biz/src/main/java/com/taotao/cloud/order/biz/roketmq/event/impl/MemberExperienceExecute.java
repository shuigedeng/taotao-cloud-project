package com.taotao.cloud.order.biz.roketmq.event.impl;


import com.taotao.cloud.common.utils.number.CurrencyUtil;
import com.taotao.cloud.member.api.enums.PointTypeEnum;
import com.taotao.cloud.member.api.feign.IFeignMemberService;
import com.taotao.cloud.order.api.dto.order.OrderMessage;
import com.taotao.cloud.order.api.enums.order.OrderStatusEnum;
import com.taotao.cloud.order.biz.entity.order.Order;
import com.taotao.cloud.order.biz.roketmq.event.OrderStatusChangeEvent;
import com.taotao.cloud.order.biz.service.order.IOrderService;
import com.taotao.cloud.sys.api.enums.SettingEnum;
import com.taotao.cloud.sys.api.feign.IFeignSettingService;
import com.taotao.cloud.sys.api.vo.setting.ExperienceSettingVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * 会员经验值
 *
 */
@Service
public class MemberExperienceExecute implements OrderStatusChangeEvent {

    /**
     * 配置
     */
    @Autowired
    private IFeignSettingService settingService;
    /**
     * 会员
     */
    @Autowired
    private IFeignMemberService memberService;
    /**
     * 订单
     */
    @Autowired
    private IOrderService orderService;

    /**
     * 完成订单赠送经验值
     *
     * @param orderMessage 订单消息
     */
    @Override
    public void orderChange(OrderMessage orderMessage) {
        if (orderMessage.getNewStatus().equals(OrderStatusEnum.COMPLETED)) {
            //获取经验值设置
			ExperienceSettingVO experienceSetting = getExperienceSetting();
            //获取订单信息
            Order order = orderService.getBySn(orderMessage.getOrderSn());
            //计算赠送经验值数量
            BigDecimal point = CurrencyUtil.mul(experienceSetting.getMoney(), order.getFlowPrice(), 0);
            //赠送会员经验值
            memberService.updateMemberPoint(point.longValue(), PointTypeEnum.INCREASE.name(), order.getMemberId(), "会员下单，赠送经验值" + point + "分");
        }
    }

    /**
     * 获取经验值设置
     *
     * @return 经验值设置
     */
    private ExperienceSettingVO getExperienceSetting() {
        return settingService.getExperienceSetting(SettingEnum.EXPERIENCE_SETTING.name()).data();
    }
}

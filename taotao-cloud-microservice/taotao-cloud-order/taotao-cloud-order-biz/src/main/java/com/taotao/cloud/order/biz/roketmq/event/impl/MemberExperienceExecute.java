package com.taotao.cloud.order.biz.roketmq.event.impl;


import com.google.gson.Gson;
import com.taotao.cloud.common.utils.number.CurrencyUtil;
import com.taotao.cloud.order.api.dto.order.OrderMessage;
import com.taotao.cloud.order.api.enums.order.OrderStatusEnum;
import com.taotao.cloud.order.biz.entity.order.Order;
import com.taotao.cloud.order.biz.roketmq.event.OrderStatusChangeEvent;
import com.taotao.cloud.order.biz.service.order.OrderService;
import com.taotao.cloud.sys.api.enums.SettingEnum;
import com.taotao.cloud.sys.api.setting.ExperienceSetting;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 会员经验值
 *
 */
//@Service
public class MemberExperienceExecute implements OrderStatusChangeEvent {

    /**
     * 配置
     */
    @Autowired
    private SettingService settingService;
    /**
     * 会员
     */
    @Autowired
    private MemberService memberService;
    /**
     * 订单
     */
    @Autowired
    private OrderService orderService;

    /**
     * 完成订单赠送经验值
     *
     * @param orderMessage 订单消息
     */
    @Override
    public void orderChange(OrderMessage orderMessage) {
        if (orderMessage.getNewStatus().equals(OrderStatusEnum.COMPLETED)) {
            //获取经验值设置
            ExperienceSetting experienceSetting = getExperienceSetting();
            //获取订单信息
            Order order = orderService.getBySn(orderMessage.getOrderSn());
            //计算赠送经验值数量
            Double point = CurrencyUtil.mul(experienceSetting.getMoney(), order.getFlowPrice(), 0);
            //赠送会员经验值
            memberService.updateMemberPoint(point.longValue(), PointTypeEnum.INCREASE.name(), order.getMemberId(), "会员下单，赠送经验值" + point + "分");
        }
    }

    /**
     * 获取经验值设置
     *
     * @return 经验值设置
     */
    private ExperienceSetting getExperienceSetting() {
        Setting setting = settingService.get(SettingEnum.EXPERIENCE_SETTING.name());
        return new Gson().fromJson(setting.getSettingValue(), ExperienceSetting.class);
    }
}

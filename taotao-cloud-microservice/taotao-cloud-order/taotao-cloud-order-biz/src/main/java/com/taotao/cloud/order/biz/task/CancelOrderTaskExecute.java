package com.taotao.cloud.order.biz.task;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.taotao.cloud.common.support.lock.DistributedLock;
import com.taotao.cloud.order.api.enums.order.OrderStatusEnum;
import com.taotao.cloud.order.biz.model.entity.order.Order;
import com.taotao.cloud.order.biz.service.order.IOrderService;
import com.taotao.cloud.sys.api.enums.SettingCategoryEnum;
import com.taotao.cloud.sys.api.feign.IFeignSettingApi;
import com.taotao.cloud.sys.api.model.vo.setting.OrderSettingVO;
import com.taotao.cloud.web.timetask.EveryMinuteExecute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 订单自动取消（每分钟执行）
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:48:45
 */
@Component
public class CancelOrderTaskExecute implements EveryMinuteExecute {

	/**
	 * 订单
	 */
	@Autowired
	private IOrderService orderService;
	/**
	 * 设置
	 */
	@Autowired
	private IFeignSettingApi settingService;

	@Autowired
	private DistributedLock distributedLock;

	@Override
	public void execute() {
		OrderSettingVO orderSetting = settingService.getOrderSetting(SettingCategoryEnum.ORDER_SETTING.name()).data();
		if (orderSetting != null && orderSetting.getAutoCancel() != null) {
			//订单自动取消时间 = 当前时间 - 自动取消时间分钟数
			DateTime cancelTime = DateUtil.offsetMinute(DateUtil.date(), -orderSetting.getAutoCancel());
			LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
			queryWrapper.eq(Order::getOrderStatus, OrderStatusEnum.UNPAID.name());
			//订单创建时间 <= 订单自动取消时间
			queryWrapper.le(Order::getCreateTime, cancelTime);
			List<Order> list = orderService.list(queryWrapper);
			List<String> cancelSnList = list.stream().map(Order::getSn).toList();
			for (String sn : cancelSnList) {
				orderService.systemCancel(sn, "超时未支付自动取消");
			}
		}
	}
}

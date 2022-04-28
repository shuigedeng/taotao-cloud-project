package com.taotao.cloud.order.biz.timetask;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.taotao.cloud.order.api.enums.order.PayStatusEnum;
import com.taotao.cloud.sys.api.enums.SettingEnum;
import com.taotao.cloud.sys.api.feign.IFeignSettingService;
import com.taotao.cloud.sys.api.vo.setting.OrderSettingVO;
import com.taotao.cloud.web.timetask.EveryMinuteExecute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 充值订单自动取消（每分钟执行）
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:48:27
 */
@Component
public class RechargeOrderTaskExecute implements EveryMinuteExecute {

	/**
	 * 充值
	 */
	@Autowired
	private RechargeService rechargeService;
	/**
	 * 设置
	 */
	@Autowired
	private IFeignSettingService settingService;


	@Override
	public void execute() {
		OrderSettingVO orderSetting = settingService.getOrderSetting(SettingEnum.ORDER_SETTING.name()).data();
		if (orderSetting != null && orderSetting.getAutoCancel() != null) {
			//充值订单自动取消时间 = 当前时间 - 自动取消时间分钟数
			DateTime cancelTime = DateUtil.offsetMinute(DateUtil.date(),
				-orderSetting.getAutoCancel());
			LambdaQueryWrapper<Recharge> queryWrapper = new LambdaQueryWrapper<>();
			queryWrapper.eq(Recharge::getPayStatus, PayStatusEnum.UNPAID.name());
			//充值订单创建时间 <= 订单自动取消时间
			queryWrapper.le(Recharge::getCreateTime, cancelTime);
			List<Recharge> list = rechargeService.list(queryWrapper);
			List<String> cancelSnList = list.stream().map(Recharge::getRechargeSn)
				.collect(Collectors.toList());
			for (String sn : cancelSnList) {
				rechargeService.rechargeOrderCancel(sn);
			}
		}
	}
}

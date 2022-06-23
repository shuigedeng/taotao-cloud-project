package com.taotao.cloud.order.biz.task;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.taotao.cloud.member.api.feign.IFeignMemberRechargeService;
import com.taotao.cloud.member.api.vo.MemberRechargeVO;
import com.taotao.cloud.sys.api.enums.SettingEnum;
import com.taotao.cloud.sys.api.feign.IFeignSettingService;
import com.taotao.cloud.sys.api.web.vo.setting.OrderSettingVO;
import com.taotao.cloud.web.timetask.EveryMinuteExecute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

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
	private IFeignMemberRechargeService rechargeService;
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
			DateTime cancelTime = DateUtil.offsetMinute(DateUtil.date(), -orderSetting.getAutoCancel());
			List<MemberRechargeVO> list = rechargeService.list(cancelTime).data();
			List<String> cancelSnList = list.stream().map(MemberRechargeVO::getRechargeSn).toList();
			for (String sn : cancelSnList) {
				rechargeService.rechargeOrderCancel(sn);
			}
		}
	}
}

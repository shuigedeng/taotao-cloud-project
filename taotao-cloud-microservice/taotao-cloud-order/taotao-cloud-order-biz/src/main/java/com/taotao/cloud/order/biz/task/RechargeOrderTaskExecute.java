package com.taotao.cloud.order.biz.task;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.taotao.cloud.job.xxl.timetask.EveryMinuteExecute;
import com.taotao.cloud.member.api.feign.IFeignMemberRechargeApi;
import com.taotao.cloud.member.api.model.vo.MemberRechargeVO;
import com.taotao.cloud.sys.api.enums.SettingCategoryEnum;
import com.taotao.cloud.sys.api.feign.IFeignSettingApi;
import com.taotao.cloud.sys.api.model.vo.setting.OrderSettingVO;
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
	private IFeignMemberRechargeApi rechargeService;
	/**
	 * 设置
	 */
	@Autowired
	private IFeignSettingApi settingService;


	@Override
	public void execute() {
		OrderSettingVO orderSetting = settingService.getOrderSetting(SettingCategoryEnum.ORDER_SETTING.name()).data();
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

package com.taotao.cloud.auth.biz.uaa.processor.loginrisk;

import org.dromara.hutool.core.date.DateUtil;
import org.dromara.hutool.core.map.MapUtil;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 密码错误次数风险实现
 */
@Component
public class PasswordErrorRiskHandle extends AbstractLoginHandle {

	// 配置触发时间间隔类型是秒
	private static final Integer SEC = 1;

	// 配置触发时间间隔类型是分钟
	private static final Integer MIN = 2;

	// 配置触发时间间隔类型是小时
	private static final Integer HOU = 3;

	//@Resource
	//private LoginLogService loginLogService;

	@Override
	public void filterRisk(List<RiskRule> filter, Map<Integer, RiskRule> ruleMap, UserAccount account) {
		if (MapUtil.isNotEmpty(ruleMap)) {
			//获取密码错误的规则信息
			RiskRule passwordRisk = ruleMap.get(1);
			if (passwordRisk != null) {
				//触发次数
				Integer triggerNumber = passwordRisk.getTriggerNumber();
				//触发时间
				Integer triggerTime = passwordRisk.getTriggerTime();
				//时间类型
				Integer triggerTimeType = passwordRisk.getTriggerTimeType();

				Date endTime = new Date();

				Date startTime;

				if (triggerTimeType == SEC) {
					startTime = DateUtil.offsetSecond(endTime, -triggerTime);
				} else if (triggerTimeType == MIN) {
					startTime = DateUtil.offsetMinute(endTime, -triggerTime);
				} else {
					startTime = DateUtil.offsetHour(endTime, -triggerTime);
				}
				// 查询范围时间内密码错误的次数
				//Integer count = loginLogService.lambdaQuery().eq(LoginLog::getResult, 2)
				//	.eq(LoginLog::getAccount, account.getAccount())
				//	.between(LoginLog::getTime, startTime, endTime)
				//	.count();
				Integer count = 0;
				// 如果达到触发规则，则记录
				if (count != null && count.intValue() >= triggerNumber.intValue()) {
					filter.add(passwordRisk);
				}
			}
		}
		//是否有下一个节点 ， 如果有，继续向下执行
		if (this.nextHandle != null) {
			this.nextHandle.filterRisk(filter, ruleMap, account);
		}
	}

}

package com.taotao.cloud.auth.biz.uaa.processor.loginrisk;

import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 登录ip风险实现
 */
@Component
public class IPRiskHandle extends AbstractLoginHandle {

	@Override
	public void filterRisk(List<RiskRule> filter, Map<Integer, RiskRule> ruleMap, UserAccount account) {
		if (MapUtil.isNotEmpty(ruleMap)) {
			RiskRule ipRisk = ruleMap.get(3);
			//判断是否配置登录ip白名单
			if (null != ipRisk && StrUtil.isNotEmpty(ipRisk.getAcceptIp())) {
				List<String> acceptIpList = Arrays.asList(ipRisk.getAcceptIp().split(","));
				//当前登录ip是否在白名单内，如果不在，则添加到filter中
				if (!acceptIpList.contains(account.getIp())) {
					filter.add(ipRisk);
				}
			}
		}
		if (this.nextHandle != null) {
			this.nextHandle.filterRisk(filter, ruleMap, account);
		}
	}
}

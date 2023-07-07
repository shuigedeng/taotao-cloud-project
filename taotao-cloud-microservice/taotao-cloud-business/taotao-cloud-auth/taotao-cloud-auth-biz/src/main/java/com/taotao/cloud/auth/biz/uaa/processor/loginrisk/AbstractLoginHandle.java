package com.taotao.cloud.auth.biz.uaa.processor.loginrisk;

import java.util.List;
import java.util.Map;

/**
 * 登录风险处理抽象父类
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-07 09:24:54
 */
public abstract class AbstractLoginHandle {

	public AbstractLoginHandle nextHandle; // 下一个执行节点

	public void setNextHandle(AbstractLoginHandle nextHandle) {
		this.nextHandle = nextHandle;
	}


	/**
	 * 具体的执行方法，过滤出满足风控的规则
	 *
	 * @param filter  满足风控的规则
	 * @param ruleMap 所有规则集合
	 * @param account 登录账户
	 */
	public abstract void filterRisk(List<RiskRule> filter, Map<Integer, RiskRule> ruleMap, UserAccount account);

}

/*
 * Copyright (c) 2018-2022 the original author or authors.
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.gnu.org/licenses/lgpl-3.0.html
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.sms.loadbalancer;

import com.taotao.cloud.sms.model.NoticeData;
import com.taotao.cloud.sms.handler.SendHandler;
import java.util.List;
import org.springframework.lang.Nullable;

/**
 * weight round-robin Load Balancer
 *
 * @author shuigedeng
 */
public class WeightRoundRobinSmsLoadBalancer extends
	WeightRoundRobinLoadBalancer<SendHandler, NoticeData>
	implements SmsSenderLoadBalancer {

	public static final String TYPE_NAME = "WeightRoundRobin";

	@Nullable
	@Override
	protected SendHandler choose0(List<TargetWrapper<SendHandler>> activeTargetList,
		NoticeData chooseReferenceObject) {
		List<TargetWrapper<SendHandler>> newActiveTargetList = activeTargetList.stream()
			.filter(wrapper -> chooseFilter(wrapper, chooseReferenceObject))
			.toList();

		if (newActiveTargetList.isEmpty()) {
			return null;
		}
		return super.choose0(activeTargetList, chooseReferenceObject);
	}
}


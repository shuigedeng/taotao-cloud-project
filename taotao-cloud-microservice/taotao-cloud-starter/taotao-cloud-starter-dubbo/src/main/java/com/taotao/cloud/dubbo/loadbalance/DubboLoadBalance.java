/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.dubbo.loadbalance;

import com.taotao.cloud.common.utils.log.LogUtils;
import java.util.List;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.cluster.loadbalance.AbstractLoadBalance;

/**
 * ShortestResponseLoadBalance
 * </p>
 * Filter the number of invokers with the shortest response time of success calls and count the
 * weights and quantities of these invokers in last slide window. If there is only one invoker, use
 * the invoker directly; if there are multiple invokers and the weights are not the same, then
 * random according to the total weight; if there are multiple invokers and the same weight, then
 * randomly called.
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-08 10:19:08
 */
public class DubboLoadBalance extends AbstractLoadBalance {

	@Override
	protected <T> Invoker<T> doSelect(List<Invoker<T>> invokers, URL url, Invocation invocation) {

		LogUtils.info("DubboLoadBalance doSelect activate ------------------------------");
		LogUtils.info(String.valueOf(invokers.size()));

		return invokers.get(0);

	}
}

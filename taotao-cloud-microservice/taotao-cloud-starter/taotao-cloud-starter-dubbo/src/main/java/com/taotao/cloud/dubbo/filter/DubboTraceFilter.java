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
package com.taotao.cloud.dubbo.filter;

import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.context.TraceContextHolder;
import com.taotao.cloud.common.utils.common.IdGeneratorUtils;
import com.taotao.cloud.common.utils.lang.StringUtils;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.common.utils.servlet.TraceUtils;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcException;

/**
 * DubboTraceFilter
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-08 10:18:45
 */
@Activate(group = {CommonConstants.PROVIDER, CommonConstants.CONSUMER})
public class DubboTraceFilter implements Filter {

	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		//服务消费者：传递traceId给下游服务 服务提供者：获取traceId并赋值给MDC
		LogUtils.info("DubboTraceFilter activate ------------------------------");

		boolean isProviderSide = RpcContext.getServerContext().isProviderSide();
		if (isProviderSide) {
			//服务提供者逻辑
			String traceId = invocation.getAttachment(CommonConstant.TAOTAO_CLOUD_TRACE_ID);
			if (StringUtils.isEmpty(traceId)) {
				traceId = IdGeneratorUtils.getIdStr();
				TraceContextHolder.setTraceId(traceId);
				TraceUtils.mdcTraceId(traceId);
			} else {
				TraceContextHolder.setTraceId(traceId);
				TraceUtils.mdcTraceId(traceId);
			}
		} else {
			//服务消费者逻辑
			String traceId = TraceUtils.getTraceId();
			if (!StringUtils.isEmpty(traceId)) {
				invocation.setAttachment(CommonConstant.TAOTAO_CLOUD_TRACE_ID, traceId);
			}
		}
		
		try {
			return invoker.invoke(invocation);
		} finally {
			if (isProviderSide) {
				TraceContextHolder.clear();
			}
		}
	}
}

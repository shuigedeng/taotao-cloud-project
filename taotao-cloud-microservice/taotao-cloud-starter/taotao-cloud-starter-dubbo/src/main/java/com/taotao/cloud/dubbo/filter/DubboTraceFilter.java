package com.taotao.cloud.dubbo.filter;

import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.context.TraceContextHolder;
import com.taotao.cloud.common.utils.common.IdGeneratorUtil;
import com.taotao.cloud.common.utils.lang.StringUtil;
import com.taotao.cloud.common.utils.servlet.TraceUtil;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcException;
import org.springframework.util.StringUtils;

/**
 * DubboTraceFilter
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-10 13:51:37
 */
@Activate(group = {CommonConstants.PROVIDER, CommonConstants.CONSUMER}, value = "dubboTraceFilter", order = 10001)
public class DubboTraceFilter implements Filter {

	/**
	 * 服务消费者：传递traceId给下游服务 服务提供者：获取traceId并赋值给MDC
	 */
	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		boolean isProviderSide = RpcContext.getContext().isProviderSide();
		if (isProviderSide) {
			//服务提供者逻辑
			String traceId = invocation.getAttachment(CommonConstant.TAOTAO_CLOUD_TRACE_ID);
			if (StringUtil.isEmpty(traceId)) {
				traceId = IdGeneratorUtil.getIdStr();
				TraceContextHolder.setTraceId(traceId);
				TraceUtil.mdcTraceId(traceId);
			} else {
				TraceContextHolder.setTraceId(traceId);
				TraceUtil.mdcTraceId(traceId);
			}
		} else {
			//服务消费者逻辑
			String traceId = TraceUtil.getTraceId();
			if (!StringUtil.isEmpty(traceId)) {
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

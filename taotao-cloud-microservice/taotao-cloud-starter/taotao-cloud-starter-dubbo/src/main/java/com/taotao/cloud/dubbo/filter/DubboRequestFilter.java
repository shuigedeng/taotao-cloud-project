package com.taotao.cloud.dubbo.filter;

import com.google.common.base.Stopwatch;
import com.taotao.cloud.common.utils.common.JsonUtils;
import com.taotao.cloud.common.utils.context.ContextUtils;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.dubbo.properties.DubboProperties;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.service.GenericService;

import java.util.concurrent.TimeUnit;

/**
 * dubbo日志过滤器
 */
@Activate(group = {CommonConstants.PROVIDER, CommonConstants.CONSUMER})
public class DubboRequestFilter implements Filter {

	private boolean requestLog;

	public void DubboRequestFilter() {
		DubboProperties dubboProperties = ContextUtils.getBean(DubboProperties.class, true);
		this.requestLog = dubboProperties.isRequestLog();
	}

	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		if (!requestLog) {
			// 未开启则跳过日志逻辑
			return invoker.invoke(invocation);
		}

		String client = CommonConstants.PROVIDER;
		if (RpcContext.getServiceContext().isConsumerSide()) {
			client = CommonConstants.CONSUMER;
		}

		String baselog =
			"Client[" + client + "],InterfaceName=[" + invocation.getInvoker().getInterface()
				.getSimpleName() + "],MethodName=[" + invocation.getMethodName() + "]";
		LogUtils.info("DUBBO - 服务调用: {},Parameter={}", baselog, invocation.getArguments());

		Stopwatch stopwatch = Stopwatch.createStarted();
		// 执行接口调用逻辑
		Result result = invoker.invoke(invocation);
		
		// 如果发生异常 则打印异常日志
		if (result.hasException() && invoker.getInterface().equals(GenericService.class)) {
			LogUtils.error("DUBBO - 服务异常: {},Exception={}", baselog, result.getException());
		} else {
			LogUtils.info("DUBBO - 服务响应: {},SpendTime=[{}ms],Response={}", baselog, stopwatch.elapsed(TimeUnit.MILLISECONDS),
				JsonUtils.toJSONString(new Object[]{result.getValue()}));
		}
		return result;
	}

}

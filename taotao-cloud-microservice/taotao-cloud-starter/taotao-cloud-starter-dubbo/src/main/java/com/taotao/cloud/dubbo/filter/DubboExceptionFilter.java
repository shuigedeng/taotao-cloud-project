package com.taotao.cloud.dubbo.filter;

import com.taotao.cloud.common.exception.BaseException;
import java.lang.reflect.Method;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.common.utils.ReflectUtils;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.service.GenericService;

/**
 * Dubbo 自定义异常过滤器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Activate(group = CommonConstants.PROVIDER)
public class DubboExceptionFilter implements Filter, Filter.Listener {

	private static Logger logger = LoggerFactory.getLogger(DubboExceptionFilter.class);

	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		return invoker.invoke(invocation);
	}

	@Override
	public void onResponse(Result appResponse, Invoker<?> invoker, Invocation invocation) {
		if (appResponse.hasException() && GenericService.class != invoker.getInterface()) {
			try {
				Throwable exception = appResponse.getException();

				// directly throw if it's checked exception
				if (!(exception instanceof RuntimeException) && (exception instanceof Exception)) {
					return;
				}

				// directly throw if the exception appears in the signature
				try {
					Method method = invoker.getInterface()
						.getMethod(invocation.getMethodName(), invocation.getParameterTypes());
					Class<?>[] exceptionClasses = method.getExceptionTypes();
					for (Class<?> exceptionClass : exceptionClasses) {
						if (exception.getClass().equals(exceptionClass)) {
							return;
						}
					}
				} catch (NoSuchMethodException e) {
					return;
				}

				// for the exception not found in method's signature, print ERROR message in server's log.
				logger.error("Got unchecked and undeclared exception which called by "
					+ RpcContext.getServiceContext().getRemoteHost() + ". service: "
					+ invoker.getInterface().getName() + ", method: " + invocation.getMethodName()
					+ ", exception: " + exception.getClass().getName() + ": "
					+ exception.getMessage(), exception);

				// directly throw if exception class and interface class are in the same jar file.
				String serviceFile = ReflectUtils.getCodeBase(invoker.getInterface());
				String exceptionFile = ReflectUtils.getCodeBase(exception.getClass());
				if (serviceFile == null || exceptionFile == null || serviceFile.equals(
					exceptionFile)) {
					return;
				}

				// directly throw if it's JDK exception
				String className = exception.getClass().getName();
				if (className.startsWith("java.") || className.startsWith("javax.")) {
					return;
				}

				// directly throw if it's custom exception
				if (exception instanceof BaseException) {
					return;
				}

				// directly throw if it's dubbo exception
				if (exception instanceof RpcException) {
					return;
				}

				// otherwise, wrap with RuntimeException and throw back to the client
				appResponse.setException(new RuntimeException(StringUtils.toString(exception)));
			} catch (Throwable e) {
				logger.warn(
					"Fail to ExceptionFilter when called by " + RpcContext.getServiceContext()
						.getRemoteHost() + ". service: " + invoker.getInterface().getName()
						+ ", method: " + invocation.getMethodName() + ", exception: " + e.getClass()
						.getName() + ": " + e.getMessage(), e);
			}
		}
	}

	@Override
	public void onError(Throwable e, Invoker<?> invoker, Invocation invocation) {
		logger.error("Got unchecked and undeclared exception which called by "
			+ RpcContext.getServiceContext().getRemoteHost() + ". service: "
			+ invoker.getInterface().getName() + ", method: " + invocation.getMethodName()
			+ ", exception: " + e.getClass().getName() + ": " + e.getMessage(), e);
	}

	// For test purpose
	//public void setLogger(Logger logger) {
	//	this.logger = logger;
	//}
}

package com.taotao.cloud.schedule.core.interceptor;

import com.taotao.cloud.schedule.exception.ScheduledException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

/**
 * RunnableBaseInterceptor
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-09 17:03:27
 */
public class RunnableBaseInterceptor implements MethodInterceptor {

	protected final Log logger = LogFactory.getLog(getClass());
	/**
	 * 定时任务执行器
	 */
	private ScheduledRunnable runnable;
	/**
	 * 定时任务增强类
	 */
	private BaseStrengthen strengthen;

	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy)
		throws Throwable {
		Object result;
		List<String> methodName = Arrays.asList("invoke", "before", "after", "exception",
			"afterFinally");
		if (methodName.contains(method.getName())) {
			strengthen.before(obj, method, args, runnable.getContext());
			try {
				result = runnable.invoke();
			} catch (Exception e) {
				strengthen.exception(obj, method, args, runnable.getContext());
				throw new ScheduledException(strengthen.getClass() + "中强化执行时发生错误", e);
			} finally {
				strengthen.afterFinally(obj, method, args, runnable.getContext());
			}
			strengthen.after(obj, method, args, runnable.getContext());
		} else {
			result = methodProxy.invokeSuper(obj, args);
		}
		return result;
	}

	public RunnableBaseInterceptor(Object object, ScheduledRunnable runnable) {
		this.runnable = runnable;
		if (BaseStrengthen.class.isAssignableFrom(object.getClass())) {
			this.strengthen = (BaseStrengthen) object;
		} else {
			throw new ScheduledException(object.getClass() + "对象不是BaseStrengthen类型");
		}
	}

	public RunnableBaseInterceptor() {

	}
}

package com.taotao.cloud.job.core.worker.processor.factory;

import com.taotao.cloud.job.core.worker.processor.ProcessResult;
import com.taotao.cloud.job.core.worker.processor.task.TaskContext;
import com.taotao.cloud.job.core.worker.processor.type.BasicProcessor;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class MethodBasicProcessor implements BasicProcessor {

	private final Object bean;

	private final Method method;

	public MethodBasicProcessor(Object bean, Method method) {
		this.bean = bean;
		this.method = method;
	}

	@Override
	public ProcessResult process(TaskContext context) throws Exception {
		try {
			Object result = method.invoke(bean, context);

			//todo 这里应该返回一个ProcessResult对象，里面包含执行结果和状态信息
			//return new ProcessResult(true, JsonUtils.toJSONString(result));
			return null;
		} catch (InvocationTargetException ite) {
			ExceptionUtils.rethrow(ite.getTargetException());
		}

		return new ProcessResult(false, "IMPOSSIBLE");
	}
}

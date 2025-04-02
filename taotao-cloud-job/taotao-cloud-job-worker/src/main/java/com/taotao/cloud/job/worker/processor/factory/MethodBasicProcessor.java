package com.taotao.cloud.job.worker.processor.factory;

import com.taotao.cloud.job.common.utils.JsonUtils;
import com.taotao.cloud.job.worker.processor.ProcessResult;
import com.taotao.cloud.job.worker.processor.task.TaskContext;
import com.taotao.cloud.job.worker.processor.type.BasicProcessor;
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
			return new ProcessResult(true, JsonUtils.toJSONString(result));
		} catch (InvocationTargetException ite) {
			ExceptionUtils.rethrow(ite.getTargetException());
		}

		return new ProcessResult(false, "IMPOSSIBLE");
	}
}

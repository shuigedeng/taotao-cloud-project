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
package com.taotao.cloud.logger.logRecord.context;

import org.springframework.core.NamedThreadLocal;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * 日志记录上下文
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-26 14:43:32
 */
public class LogRecordContext {

	/**
	 * 上下文线程本地
	 */
	private static final ThreadLocal<StandardEvaluationContext> CONTEXT_THREAD_LOCAL = new NamedThreadLocal<>("ThreadLocal StandardEvaluationContext");

	/**
	 * 获得上下文
	 *
	 * @return {@link StandardEvaluationContext }
	 * @since 2022-04-26 14:43:32
	 */
	public static StandardEvaluationContext getContext() {
        return CONTEXT_THREAD_LOCAL.get() == null ? new StandardEvaluationContext(): CONTEXT_THREAD_LOCAL.get();
    }

	/**
	 * 把变量
	 *
	 * @param key   关键
	 * @param value 价值
	 * @since 2022-04-26 14:43:33
	 */
	public static void putVariables(String key, Object value) {
        StandardEvaluationContext context = CONTEXT_THREAD_LOCAL.get() == null ? new StandardEvaluationContext(): CONTEXT_THREAD_LOCAL.get();
        context.setVariable(key, value);
        CONTEXT_THREAD_LOCAL.set(context);
    }

	/**
	 * 清晰上下文
	 *
	 * @since 2022-04-26 14:43:33
	 */
	public static void clearContext() {
        CONTEXT_THREAD_LOCAL.remove();
    }

}

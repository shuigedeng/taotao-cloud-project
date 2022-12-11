package com.taotao.cloud.sys.biz.gobrs.config;

import com.gobrs.async.core.callback.AsyncTaskPostInterceptor;
import org.springframework.stereotype.Component;

/**
 * @program: m-detail
 * @ClassName AsyncTaskPreInterceptor
 * @description:
 * @author: sizegang
 * @create: 2022-03-24
 **/
@Component
public class TaskPostInterceptor implements AsyncTaskPostInterceptor {

	/**
	 * @param result   任务结果
	 * @param taskName 任务名称
	 */
	@Override
	public void postProcess(Object result, String taskName) {

	}
}


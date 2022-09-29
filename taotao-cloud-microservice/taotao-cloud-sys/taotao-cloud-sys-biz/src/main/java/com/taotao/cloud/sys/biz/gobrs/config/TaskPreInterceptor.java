package com.taotao.cloud.sys.biz.gobrs.config;

import com.gobrs.async.callback.AsyncTaskPreInterceptor;
import org.springframework.stereotype.Component;

/**
 * @program: m-detail
 * @ClassName AsyncTaskPreInterceptor
 * @description:
 * @author: sizegang
 * @create: 2022-03-24
 **/
@Component
public class TaskPreInterceptor implements AsyncTaskPreInterceptor {


	@Override
	public void preProcess(Object params, String taskName) {

	}
}



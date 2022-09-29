package com.taotao.cloud.sys.biz.gobrs.config;

import com.gobrs.async.callback.AsyncTaskExceptionInterceptor;
import com.gobrs.async.callback.ErrorCallback;
import org.springframework.stereotype.Component;

/**
 * @program: gobrs-async
 * @ClassName GobrsExceptionInter
 * @description: 主流程中断异常自定义处理
 * @author: sizegang
 * @create: 2022-02-19 22:55
 * @Version 1.0
 **/
@Component
public class GobrsExceptionInter implements AsyncTaskExceptionInterceptor {

	@Override
	public void exception(ErrorCallback errorCallback) {
		System.out.println("自定义全局异常 exceptor Interceptor 触发");
	}
}


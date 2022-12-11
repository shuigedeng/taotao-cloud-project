package com.taotao.cloud.sys.biz.gobrs.task;

import com.gobrs.async.core.TaskSupport;
import com.gobrs.async.core.anno.Task;
import com.gobrs.async.core.task.AsyncTask;
import org.springframework.stereotype.Component;

/**
 * The type B service.
 *
 * @program: gobrs -async-starter
 * @ClassName BService
 * @description:
 * @author: sizegang
 * @create: 2022 -03-20
 */
@Component
@Task
public class BService extends AsyncTask<Object, Object> {


	/**
	 * The .
	 */
	int i = 10000;

	@Override
	public void prepare(Object o) {

	}

	@Override
	public Object task(Object o, TaskSupport support) {
		System.out.println("BService Begin");
		for (int i1 = 0; i1 < i; i1++) {
			i1 += i1;
		}
		System.out.println(1 / 0);
		System.out.println("BService Finish");

		String result = getResult(support, AService.class, String.class);
		System.out.println("拿到的结果" + result);
		System.out.println("执行BService");

		return null;
	}


	@Override
	public void onSuccess(TaskSupport support) {

	}

	@Override
	public void onFailureTrace(TaskSupport support, Exception exception) {
	}

	@Override
	public boolean necessary(Object params, TaskSupport support) {
		// 假如参数是cancel 则不执行当前任务
		if ("cancel".equals(params)) {
			return false;
		}
		return true;
	}

	@Override
	public void onFail(TaskSupport support, Exception exception) {
	}

	@Override
	public void rollback(Object o) {
		super.rollback(o);
	}
}

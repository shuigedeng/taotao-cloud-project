package com.taotao.cloud.sys.biz.gobrs.task;

import com.gobrs.async.core.TaskSupport;
import com.gobrs.async.core.anno.Task;
import com.gobrs.async.core.task.AsyncTask;
import org.springframework.stereotype.Component;

/**
 * The type G service.
 *
 * @program: gobrs -async-starter
 * @ClassName EService
 * @description:
 * @author: sizegang
 * @create: 2022 -03-20
 */
@Component
@Task(callback = true)
public class GService extends AsyncTask<Object, Object> {

	/**
	 * The .
	 */
	int i = 10000;

	@Override
	public void prepare(Object o) {

	}

	@Override
	public Object task(Object o, TaskSupport support) {
		try {
			System.out.println("GService Begin");
			Thread.sleep(100);
			System.out.println("GService Finish");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for (int i1 = 0; i1 < i; i1++) {
			i1 += i1;
		}

		return null;
	}


	@Override
	public void onSuccess(TaskSupport support) {

	}

	@Override
	public void onFailureTrace(TaskSupport support, Exception exception) {
	}

	@Override
	public boolean necessary(Object o, TaskSupport support) {
		return super.necessary(o, support);
	}

	@Override
	public void onFail(TaskSupport support, Exception exception) {
	}

	@Override
	public void rollback(Object o) {
		super.rollback(o);
	}
}

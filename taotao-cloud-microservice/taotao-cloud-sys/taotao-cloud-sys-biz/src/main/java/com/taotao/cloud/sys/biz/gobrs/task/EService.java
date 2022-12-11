package com.taotao.cloud.sys.biz.gobrs.task;

import com.gobrs.async.core.TaskSupport;
import com.gobrs.async.core.task.AsyncTask;
import org.springframework.stereotype.Component;


/**
 * The type E service.
 *
 * @program: gobrs -async-starter
 * @ClassName EService
 * @description:
 * @author: sizegang
 * @create: 2022 -03-20
 */
@Component
public class EService extends AsyncTask<Object, Object> {

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
			System.out.println("EService Begin");
			Thread.sleep(600);
			for (int i1 = 0; i1 < i; i1++) {
				i1 += i1;
			}
			System.out.println("EService Finish");

		} catch (InterruptedException e) {
			e.printStackTrace();
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

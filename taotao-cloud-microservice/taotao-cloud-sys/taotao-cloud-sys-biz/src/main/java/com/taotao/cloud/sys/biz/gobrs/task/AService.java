package com.taotao.cloud.sys.biz.gobrs.task;

import com.gobrs.async.TaskSupport;
import com.gobrs.async.anno.Task;
import com.gobrs.async.domain.TaskResult;
import com.gobrs.async.task.AsyncTask;
import com.taotao.cloud.common.exception.BusinessException;
import org.springframework.stereotype.Component;

/**
 * The type A service.
 *
 * @program: gobrs -async-starter
 * @ClassName AService
 * @description:
 * @author: sizegang
 * @create: 2022 -03-20
 */
//在你需要进行事务的任务上进行回滚注解， Gobrs-Async 会找到 AService 任务链之前的所有任务 回调rollback 方法
//在执行 A->B->C 过程中，如果A 执行异常，Gobrs-Async 默认不会继续执行 B、C任务了，但是如果使用者有特殊需求，
// 想要继续执行 B、C任务， 这种情况Gobrs-Async 也提供支持, 只需要在 Task注解中声明 failSubExec 即可继续执行任务流程。
@Task(failSubExec = true, callback = true)
@Component
public class AService extends AsyncTask<Object, String> {

	/**
	 * The .
	 */
	int i = 10000;

	@Override
	public void prepare(Object o) {

	}

	//核心任务执行
	@Override
	public String task(Object o, TaskSupport support) {
		String result = getResult(support);
		TaskResult<String> taskResult = getTaskResult(support);

		try {
			System.out.println("AService Begin");
			Thread.sleep(300);
			for (int i1 = 0; i1 < i; i1++) {
				i1 += i1;
			}
			System.out.println("AService Finish");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		try {
			// todo 执行任务
		} catch (Exception e) {
			// todo  根据不同的异常 处理返回不同的 中断码
			if (e instanceof BusinessException) {
				// 中断任务流程
				stopAsync(support, 500);
				return null;
			}
			stopAsync(support, 501);
		}
		return null;

		// return "result";
	}

	//Gobrs-Async 会根据 nessary 的返回结果，判断当前task 是否需要执行 如果返回true 则需要被执行，否则返之。
	//
	// 例如： 当参数为 cancel 时， 任务不执行。
	@Override
	public boolean nessary(Object o, TaskSupport support) {
		return true;
	}


	//如果你想在任务执行完成后做一些额外的操作。例如打印日志、发送邮件、发送MQ、记录信息等。 Gobrs-Async 同样也为你考虑到了。
	// 通过实现 callback 方法。会让你轻松的拿到 任务的执行结果。
	@Override
	public void onSuccess(TaskSupport support) {

	}

	//在任务异常时发送告警信息
	@Override
	public void onFail(TaskSupport support) {

	}

	//事务回滚 具体回滚业务需要自己实现 该方法是一个默认方法 需要自己手动重写
	@Override
	public void rollback(Object o) {
		//super.rollback(dataContext);
		//todo rollback business
	}
}

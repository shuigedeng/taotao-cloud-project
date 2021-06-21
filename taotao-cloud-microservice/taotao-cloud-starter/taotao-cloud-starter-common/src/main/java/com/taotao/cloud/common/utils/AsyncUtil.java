package com.taotao.cloud.common.utils;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

/**
 * @创建人 霍钧城
 * @创建时间 2020年12月03日 17:28:00
 * @描述 异步处理异常工具类
 */
public class AsyncUtil {

	private final static String name = "异步处理异常工具";
	private static ExecutorService executorService = new ThreadPoolExecutor(1, 20,
		60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
	private static Timer taskTimer = new Timer(name, true);

	/**
	 * @描述 启动线程
	 * @参数 [task]
	 * @返回值 void
	 * @创建人 霍钧城
	 * @创建时间 2020/12/28
	 * @修改历史：
	 */
	public static void execute(Runnable task) {
		execute(task, 0, 0, null);
	}

	/**
	 * @描述 线程启动 出错后重试，最大重试maxRetryCount次，每次默认多延迟1秒执行，最大延迟5秒
	 * @参数 [task, maxRetryCount]
	 * @返回值 void
	 * @创建人 霍钧城
	 * @创建时间 2020/12/28
	 * @修改历史：
	 */
	public static void execute(Runnable task, int maxRetryCount) {
		execute(task, maxRetryCount, 0, null);
	}

	/**
	 * @描述 出错后重试，最大重试maxRetryCount次，每次默认延迟1秒执行，每次增加1秒，最大延迟5秒，返回执行结果
	 * @参数 [task, maxRetryCount, consumer]
	 * @返回值 void
	 * @创建人 霍钧城
	 * @创建时间 2020/12/28
	 * @修改历史：
	 */
	public static void execute(Runnable task, int maxRetryCount,
		BiConsumer<Boolean, Throwable> consumer) {
		execute(task, maxRetryCount, 0, consumer);
	}

	/**
	 * @描述 出错后重试，最大重试maxRetryCount次，每次延迟delaySeconds秒执行,如果delaySeconds<=0
	 * 默认延迟1秒执行，每次增加1秒，最大延迟5秒，返回执行结果
	 * @参数 [task, maxRetryCount, delaySeconds, consumer]
	 * @返回值 void
	 * @创建人 霍钧城
	 * @创建时间 2020/12/28
	 * @修改历史：
	 */
	public static void execute(Runnable task, int maxRetryCount, int delaySeconds,
		BiConsumer<Boolean, Throwable> consumer) {
		new Executor(task, maxRetryCount, delaySeconds, consumer).execute();
	}

	/**
	 * @描述 出错后重试，延迟firstDelaySeconds后开始重试，最大重试maxRetryCount次，每次延迟delaySeconds秒执行，如果delaySeconds<=0
	 * 默认延迟1秒执行，每次增加1秒，最大延迟5秒，返回执行结果
	 * @参数 [task, firstDelaySeconds, maxRetryCount, delaySeconds, consumer]
	 * @返回值 void
	 * @创建人 霍钧城
	 * @创建时间 2020/12/28
	 * @修改历史：
	 */
	public static void executeDelay(Runnable task, int firstDelaySeconds, int maxRetryCount,
		int delaySeconds, BiConsumer<Boolean, Throwable> consumer) {
		Executor executor = new Executor(task, maxRetryCount, delaySeconds, consumer);
		if (firstDelaySeconds > 0) {
			LogUtil.warn(name, "延迟执行异常，将会在[" + firstDelaySeconds + "]秒后执行重试");
			taskTimer.schedule(new Task(executor), firstDelaySeconds * 1000L);
		} else {
			executor.execute();
		}
	}

	private static class Executor implements Runnable {

		private int[] retrySeconds = {1, 2, 3, 4, 5};
		private Runnable task;
		private int maxRetryCount;
		private int delaySeconds;
		private int retryAttempts;
		private BiConsumer<Boolean, Throwable> resultConsumer;

		public Executor(Runnable task, int maxRetryCount, int delaySeconds,
			BiConsumer<Boolean, Throwable> resultConsumer) {
			this.task = task;
			this.maxRetryCount = maxRetryCount;
			this.delaySeconds = delaySeconds;
			this.resultConsumer = resultConsumer;
		}

		public void execute() {
			executorService.execute(this);
		}

		@Override
		public void run() {
			Throwable exception = null;
			try {
				task.run();
			} catch (Throwable e) {
				if (retryAttempts++ < maxRetryCount) {
					int delay = delaySeconds > 0 ? delaySeconds
						: retrySeconds[Math.min(retryAttempts, retrySeconds.length) - 1];
					LogUtil.warn(name,
						"执行异常，将会在[" + delay + "]秒后进行第[" + retryAttempts + "]次重试，异常信息：" + e
							.getMessage(), e);
					taskTimer.schedule(new Task(this), delay * 1000L);
					return;
				}
				exception = e;
			}
			if (exception != null) {
				if (maxRetryCount > 0) {
					LogUtil.error(name,
						"执行异常，重试[" + maxRetryCount + "]后仍然失败，异常信息：" + exception.getMessage(),
						exception);
				} else {
					LogUtil.error(name, "执行异常，异常信息：" + exception.getMessage(),
						exception);
				}
			}
			if (resultConsumer != null) {
				try {
					resultConsumer.accept(exception == null, exception);
				} catch (Throwable e) {
					LogUtil.error( name, "处理结果回调异常", e);
				}
			}
		}
	}

	private static class Task extends TimerTask {

		private Executor task;

		public Task(Executor task) {
			this.task = task;
		}

		@Override
		public void run() {
			task.execute();
		}
	}
}

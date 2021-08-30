package com.taotao.cloud.core.thread;


import com.taotao.cloud.core.model.Collector;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author: chejiangyi
 * @version: 2019-08-01 13:49
 **/
public class ThreadMonitor {

	private ThreadPoolExecutor threadPoolExecutor;
	private final String name;

	public ThreadMonitor(String name, ThreadPoolExecutor threadPoolExecutor) {
		this.threadPoolExecutor = threadPoolExecutor;
		this.name = name;

		Collector.DEFAULT.call(name + ".active.count").set(() ->
			threadPoolExecutor == null ? 0 : threadPoolExecutor.getActiveCount());
		Collector.DEFAULT.call(name + ".core.poolSize").set(() ->
			threadPoolExecutor == null ? 0 : threadPoolExecutor.getCorePoolSize());
		Collector.DEFAULT.call(name + ".poolSize.largest")
			.set(() -> threadPoolExecutor == null ? 0 : threadPoolExecutor.getLargestPoolSize());
		Collector.DEFAULT.call(name + ".poolSize.max").set(() ->
			threadPoolExecutor == null ? 0 : threadPoolExecutor.getMaximumPoolSize());
		Collector.DEFAULT.call(name + ".poolSize.count").set(() ->
			threadPoolExecutor == null ? 0 : threadPoolExecutor.getPoolSize());
		Collector.DEFAULT.call(name + ".queue.size").set(() ->
			threadPoolExecutor == null ? 0 : threadPoolExecutor.getQueue().size());
		Collector.DEFAULT.call(name + ".task.count").set(() ->
			threadPoolExecutor == null ? 0 : threadPoolExecutor.getTaskCount());
		Collector.DEFAULT.call(name + ".task.completed").set(() ->
			threadPoolExecutor == null ? 0 : threadPoolExecutor.getCompletedTaskCount());
	}

	public Collector.Hook hook() {
		return Collector.DEFAULT.hook(name + ".hook");
	}
}

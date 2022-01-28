package com.taotao.cloud.web.schedule;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * @author jitwxs
 * @date 2021年03月27日 21:54
 */
//@Component
public class SchedulerDSHandler extends AbstractDSHandler<SchedulerTaskInfo> implements
	ApplicationListener {

	public volatile List<SchedulerTaskInfo> taskInfoList = Collections.singletonList(
		SchedulerTaskInfo.builder()
			.id(1)
			.cron("0/10 * * * * ? ")
			.isValid(true)
			.reference("com.github.jitwxs.sample.ds.test.SchedulerTest#foo")
			.build()
	);

	public void add(SchedulerTaskInfo taskInfo){
		taskInfoList.add(taskInfo);
	}

	@Override
	protected List<SchedulerTaskInfo> listTaskInfo() {
		return taskInfoList;
	}

	@Override
	protected void doProcess(SchedulerTaskInfo taskInfo) throws Throwable {
		final String reference = taskInfo.getReference();
		final String[] split = reference.split("#");
		if (split.length != 2) {
			return;
		}

		try {
			final Class<?> clazz = Class.forName(split[0]);
			final Method method = clazz.getMethod(split[1]);
			method.invoke(clazz.newInstance());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onApplicationEvent(ApplicationEvent applicationEvent) {
		Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> {
			LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(10));

			// setting 1 seconds execute
			taskInfoList = Collections.singletonList(
				SchedulerTaskInfo.builder()
					.id(1)
					.cron("0/1 * * * * ? ")
					.isValid(true)
					.reference("com.github.jitwxs.sample.ds.test.SchedulerTest#foo")
					.build()
			);

			LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(10));

			// setting not valid
			taskInfoList = Collections.singletonList(
				SchedulerTaskInfo.builder()
					.id(1)
					.cron("0/1 * * * * ? ")
					.isValid(false)
					.reference("com.github.jitwxs.sample.ds.test.SchedulerTest#foo")
					.build()
			);

			LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(10));

			// setting valid
			taskInfoList = Collections.singletonList(
				SchedulerTaskInfo.builder()
					.id(1)
					.cron("0/1 * * * * ? ")
					.isValid(true)
					.reference("com.github.jitwxs.sample.ds.test.SchedulerTest#foo")
					.build()
			);
		}, 12, 86400, TimeUnit.SECONDS);
	}
}

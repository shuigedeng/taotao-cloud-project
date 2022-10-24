package com.taotao.cloud.job.schedule.task;

import com.taotao.cloud.common.utils.context.ContextUtils;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.job.schedule.model.entity.Task;
import com.taotao.cloud.job.schedule.utils.CronUtils;
import com.taotao.cloud.job.schedule.utils.JobInvokeUtil;
import com.taotao.cloud.lock.support.DistributedLock;
import com.taotao.cloud.lock.support.ZLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class TaskRunnable implements Runnable {
	private final Logger log = LoggerFactory.getLogger(TaskManager.class);

	/**
	 * 创建任务时，传递任任务id
	 */
	private final String id;

	/**
	 * redis锁的key
	 */
	public static final String TASK_LOCK_KEY = "task_lock_key:";

	public TaskRunnable(String id) {
		this.id = id;
	}

	/**
	 * 是否进行校验时间差，第一次执行任务时，不校验时间差
	 */
	private boolean checkTime = false;

	@Override
	public void run() {

		DistributedLock distributedLock = ContextUtils.getBean(DistributedLock.class);
		if (distributedLock == null) {
			LogUtils.warn("分布式锁为空, 不支持分布式, 请配置taotao.cloud.lock.type=redis");
		}

		// redis 锁验证的value
		String value = UUID.randomUUID().toString();
		;

		//获取当前执行时间戳
		long currentTime = System.currentTimeMillis();

		// 获取task
		Task currentTask = ContextUtils.getBean(TaskMapper.class).selectTaskById(id);

		ZLock zLock = null;
		try {
			//判断任务执行时间和实际时间差
			Date nextRunTime = currentTask.getNextRunTime();
			log.info("任务：{}，当前：{}，下次：{}", id, new Date(), nextRunTime);
			// 时间差
			long diffTime = Math.abs(currentTime - nextRunTime.getTime());

			// 进行redis + lua 分布式锁判断任务是  否已经执行


			String lockKey = TASK_LOCK_KEY + id;
			try {
				if (diffTime <= 1000) {
					zLock = distributedLock.lock(lockKey, 500, TimeUnit.MILLISECONDS);
				} else {
					zLock = distributedLock.lock(lockKey, 1, TimeUnit.SECONDS);
				}
			} catch (Exception e) {
				LogUtils.error(e);
			}

			if (zLock == null) {
				// 如果已经被锁，则不继续执行，将停止任务的执行
				log.info("任务:{} 已执行，暂停当前执行!", id);
			} else {
				//执行时，允许200ms误差，为了防止服务器时间钟摆出现误差
				if (diffTime > 200 && checkTime) {
					String msg = "任务执行异常，时间节点错误！";
					//开发中出现了错误情况，可以采用发生邮箱提醒给开发者
					log.error(msg);
					// 抛出异常记录错误日志
					throw new RuntimeException(msg);
				}

				//通过表达式找到需要执行的方法
				String invokeTarget = currentTask.getInvokeTarget();
				//获取bean
				String beanName = JobInvokeUtil.getBeanName(invokeTarget);
				// 获取调用方法
				String methodName = JobInvokeUtil.getMethodName(invokeTarget);
				// 获取参数
				List<Object[]> methodParams = JobInvokeUtil.getMethodParams(invokeTarget);
				// 默认第一个参数 加上 id 参数
				methodParams.add(0, new Object[]{id, String.class});

				// 通过反射找到对应执行方法
				Object bean = ContextUtils.getBean(beanName);
				Method method = bean.getClass().getDeclaredMethod(methodName, JobInvokeUtil.getMethodParamsType(methodParams));
				// 执行任务
				long startTime = System.currentTimeMillis();
				method.invoke(bean, JobInvokeUtil.getMethodParamsValue(methodParams));

				// 更新任务
				updateTask(currentTask);

				// 记录日志
				TaskLogRecord.recordTaskLog(id, startTime, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 更新任务
			updateTask(currentTask);
			// 出现异常记录异常日志，并且可以发生邮箱给开发者
			TaskLogRecord.recordTaskLog(id, 0, e);
		} finally {
			// 当任务执行完成后，后续开启时间校验
			checkTime = true;

			try {
				distributedLock.unlock(zLock);
			} catch (Exception e) {
				LogUtils.error("每分钟任务异常", e);
			}
		}
	}


	private void updateTask(Task currentTask) {
		String taskId = currentTask.getId();

		TaskManager taskManager = ContextUtils.getBean(TaskManager.class);
		if (taskManager.getTaskMap().get(taskId) != null) {
			String cron = currentTask.getCronExpression();
			String invokeTarget = currentTask.getInvokeTarget();
			Date nextRunTime = currentTask.getNextRunTime();

			// 查询执行周期
			Date nextTime = CronUtils.nextCurrentTime(cron);

			//修改任务状况为执行中
			Task task = new Task();
			task.setId(taskId);
			task.setCronExpression(cron);
			task.setInvokeTarget(invokeTarget);
			//上次执行时间为，本次的下次执行时间
			task.setLastRunTime(nextRunTime);
			task.setNextRunTime(nextTime);
			//执行中
			task.setSituation(1);

			ContextUtils.getBean(TaskMapper.class).update(task);

			log.info("更新任务执行情况!");
		}
	}
}

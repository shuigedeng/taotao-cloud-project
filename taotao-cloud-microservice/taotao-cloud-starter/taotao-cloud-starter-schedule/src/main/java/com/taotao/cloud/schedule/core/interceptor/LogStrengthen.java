package com.taotao.cloud.schedule.core.interceptor;


import com.taotao.cloud.common.constant.RedisConstant;
import com.taotao.cloud.common.utils.context.ContextUtils;
import com.taotao.cloud.redis.repository.RedisRepository;
import com.taotao.cloud.schedule.common.annotation.ScheduledInterceptorOrder;
import com.taotao.cloud.schedule.common.utils.proxy.Point;
import com.taotao.cloud.schedule.model.ScheduledLogModel;
import com.taotao.cloud.schedule.model.ScheduledRunningContext;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * LogStrengthen 
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-25 15:18:36
 */
@ScheduledInterceptorOrder(-1)
public class LogStrengthen implements BaseStrengthen {

	private ScheduledLogModel scheduledLogModel;

	/**
	 * 前置强化方法
	 *
	 * @param bean    bean实例（或者是被代理的bean）
	 * @param method  执行的方法对象
	 * @param args    方法参数
	 * @param context 任务线程运行时的上下文
	 */
	@Override
	public void before(Object bean, Method method, Object[] args, ScheduledRunningContext context) {
		Point point = (Point) bean;
		scheduledLogModel = new ScheduledLogModel();
		scheduledLogModel.setScheduledSource(point.getScheduledSource());
		scheduledLogModel.setStatrDate(new Date());
		scheduledLogModel.setScheduledName(point.getSuperScheduledName());
	}

	/**
	 * 后置强化方法
	 *
	 * @param bean    bean实例（或者是被代理的bean）
	 * @param method  执行的方法对象
	 * @param args    方法参数
	 * @param context 任务线程运行时的上下文
	 */
	@Override
	public void after(Object bean, Method method, Object[] args, ScheduledRunningContext context) {
		scheduledLogModel.setEndDate(new Date());
		scheduledLogModel.setSuccess(Boolean.TRUE);
		scheduledLogModel.computingTime();

		// 发送日志到redis  sys模块消费存入数据库
		RedisRepository redisRepository = ContextUtils.getBean(RedisRepository.class, true);
		if(redisRepository != null) {
			redisRepository.send(RedisConstant.SCHEDULED_JOB_LOG_ADD_TOPIC, scheduledLogModel);
		}

		//SerializableUtils.toIncFile(scheduledLogModel, logPath, scheduledLogModel.getFileName());
	}

	/**
	 * 异常强化方法
	 *
	 * @param bean    bean实例（或者是被代理的bean）
	 * @param method  执行的方法对象
	 * @param args    方法参数
	 * @param context 任务线程运行时的上下文
	 */
	@Override
	public void exception(Object bean, Method method, Object[] args,
		ScheduledRunningContext context) {
		scheduledLogModel.setSuccess(Boolean.FALSE);
	}

	/**
	 * Finally强化方法，出现异常也会执行
	 *
	 * @param bean    bean实例（或者是被代理的bean）
	 * @param method  执行的方法对象
	 * @param args    方法参数
	 * @param context 任务线程运行时的上下文
	 */
	@Override
	public void afterFinally(Object bean, Method method, Object[] args,
		ScheduledRunningContext context) {
		scheduledLogModel.setEndDate(new Date());
		scheduledLogModel.computingTime();
		scheduledLogModel.generateFileName();

		// 发送日志到redis  sys模块消费存入数据库
		RedisRepository redisRepository = ContextUtils.getBean(RedisRepository.class, true);
		if(redisRepository != null) {
			redisRepository.send(RedisConstant.SCHEDULED_JOB_LOG_ADD_TOPIC, scheduledLogModel);
		}

		//if (scheduledLogModel.getSuccess() != null && !scheduledLogModel.getSuccess()) {
		//	SerializableUtils.toIncFile(scheduledLogModel, logPath, scheduledLogModel.getFileName());
		//}
	}
}

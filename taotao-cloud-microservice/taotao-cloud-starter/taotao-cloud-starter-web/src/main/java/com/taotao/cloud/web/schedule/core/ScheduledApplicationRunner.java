package com.taotao.cloud.web.schedule.core;

import com.taotao.cloud.web.schedule.ScheduledException;
import com.taotao.cloud.web.schedule.common.annotation.ScheduledInterceptorOrder;
import com.taotao.cloud.web.schedule.common.annotation.ScheduledOrder;
import com.taotao.cloud.web.schedule.common.utils.AnnotationUtils;
import com.taotao.cloud.web.schedule.common.utils.proxy.Chain;
import com.taotao.cloud.web.schedule.common.utils.proxy.Point;
import com.taotao.cloud.web.schedule.common.utils.proxy.ProxyUtils;
import com.taotao.cloud.web.schedule.core.interceptor.BaseStrengthen;
import com.taotao.cloud.web.schedule.core.interceptor.RunnableBaseInterceptor;
import com.taotao.cloud.web.schedule.core.interceptor.ScheduledRunnable;
import com.taotao.cloud.web.schedule.model.ScheduledJobModel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * SuperScheduledApplicationRunner
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-09 17:00:05
 */
public class ScheduledApplicationRunner implements ApplicationRunner, ApplicationContextAware {

	protected final Log logger = LogFactory.getLog(getClass());
	private final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	private ApplicationContext applicationContext;

	/**
	 * 定时任务配置管理器
	 */
	@Autowired
	private ScheduledConfig scheduledConfig;
	/**
	 * 定时任务执行线程
	 */
	@Autowired
	private ThreadPoolTaskScheduler threadPoolTaskScheduler;

	@Override
	public void run(ApplicationArguments args) {
		scheduledConfig.setTaskScheduler(threadPoolTaskScheduler);
		Map<String, ScheduledJobModel> nameToScheduledSource = scheduledConfig.getNameToScheduledSource();
		for (String name : nameToScheduledSource.keySet()) {
			//获取定时任务源数据
			ScheduledJobModel scheduledJobModel = nameToScheduledSource.get(name);
			//获取所有增强类
			String[] baseStrengthenBeanNames = applicationContext.getBeanNamesForType(
				BaseStrengthen.class);
			//创建执行控制器
			ScheduledRunnable runnable = new ScheduledRunnable();
			runnable.setMethod(scheduledJobModel.getMethod());
			runnable.setBean(scheduledJobModel.getBean());
			//将增强器代理成point
			List<Point> points = new ArrayList<>(baseStrengthenBeanNames.length);
			for (String baseStrengthenBeanName : baseStrengthenBeanNames) {
				Object baseStrengthenBean = applicationContext.getBean(baseStrengthenBeanName);
				//获取顺序
				ScheduledOrder orderAnnotation = baseStrengthenBean.getClass()
					.getAnnotation(ScheduledOrder.class);
				ScheduledInterceptorOrder interiorOrderAnnotation = baseStrengthenBean.getClass()
					.getAnnotation(ScheduledInterceptorOrder.class);

				//创建代理
				Point proxy = ProxyUtils.getInstance(Point.class,
					new RunnableBaseInterceptor(baseStrengthenBean, runnable));
				proxy.setOrder(orderAnnotation == null ? 0 : orderAnnotation.value());
				proxy.setInteriorOrder(
					interiorOrderAnnotation == null ? null : interiorOrderAnnotation.value());
				proxy.setSuperScheduledName(name);
				proxy.setScheduledSource(scheduledJobModel);
				//所有的points连接起来
				points.add(proxy);
			}
			//按照执行顺序排序
			AnnotationUtils.scheduledOrderSort(points);
			runnable.setChain(new Chain(points));
			//添加缓存中
			scheduledConfig.addRunnable(name, runnable::invoke);
			//执行方法
			try {
				ScheduledFuture<?> schedule = ScheduledFutureFactory.create(threadPoolTaskScheduler
					, scheduledJobModel, runnable::invoke);
				scheduledConfig.addScheduledFuture(name, schedule);
				logger.info(df.format(LocalDateTime.now()) + "任务" + name + "已经启动...");
			} catch (Exception e) {
				throw new ScheduledException(
					"任务" + name + "启动失败，错误信息：" + e.getLocalizedMessage());
			}
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}

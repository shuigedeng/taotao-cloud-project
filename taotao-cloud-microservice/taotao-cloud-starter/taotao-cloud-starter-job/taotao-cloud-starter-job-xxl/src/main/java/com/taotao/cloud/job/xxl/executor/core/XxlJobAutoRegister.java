package com.taotao.cloud.job.xxl.executor.core;

import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.job.xxl.executor.annotation.XxlRegister;
import com.taotao.cloud.job.xxl.executor.model.XxlJobGroup;
import com.taotao.cloud.job.xxl.executor.model.XxlJobInfo;
import com.taotao.cloud.job.xxl.executor.service.JobGroupService;
import com.taotao.cloud.job.xxl.executor.service.JobInfoService;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * xxl汽车登记工作
 *
 * @author shuigedeng
 * @version 2022.09
 * @since 2022-10-25 09:44:17
 */
@Component
public class XxlJobAutoRegister implements ApplicationListener<ApplicationReadyEvent>,
	ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Autowired
	private JobGroupService jobGroupService;

	@Autowired
	private JobInfoService jobInfoService;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		//注册执行器
		try {
			addJobGroup();
		} catch (Exception e) {
			LogUtils.info("get xxl-job cookie error!");
			return;
		}

		//注册任务
		addJobInfo();
	}

	//自动注册执行器
	private void addJobGroup() {
		if (jobGroupService.preciselyCheck()) {
			return;
		}

		if (jobGroupService.autoRegisterGroup()) {
			LogUtils.info("auto register xxl-job group success!");
		}
	}

	private void addJobInfo() {
		List<XxlJobGroup> jobGroups = jobGroupService.getJobGroup();
		XxlJobGroup xxlJobGroup = jobGroups.get(0);

		String[] beanDefinitionNames = applicationContext.getBeanNamesForType(Object.class, false, true);
		for (String beanDefinitionName : beanDefinitionNames) {
			Object bean = applicationContext.getBean(beanDefinitionName);

			Map<Method, XxlJob> annotatedMethods = MethodIntrospector.selectMethods(bean.getClass(),
				(MethodIntrospector.MetadataLookup<XxlJob>) method -> AnnotatedElementUtils.findMergedAnnotation(method, XxlJob.class));

			for (Map.Entry<Method, XxlJob> methodXxlJobEntry : annotatedMethods.entrySet()) {
				Method executeMethod = methodXxlJobEntry.getKey();
				XxlJob xxlJob = methodXxlJobEntry.getValue();

				//自动注册
				if (executeMethod.isAnnotationPresent(XxlRegister.class)) {
					XxlRegister xxlRegister = executeMethod.getAnnotation(XxlRegister.class);
					List<XxlJobInfo> jobInfo = jobInfoService.getJobInfo(xxlJobGroup.getId(), xxlJob.value());
					if (!jobInfo.isEmpty()) {
						//因为是模糊查询，需要再判断一次
						Optional<XxlJobInfo> first = jobInfo.stream()
							.filter(xxlJobInfo -> xxlJobInfo.getExecutorHandler().equals(xxlJob.value()))
							.findFirst();
						if (first.isPresent()) {
							continue;
						}
					}

					XxlJobInfo xxlJobInfo = createXxlJobInfo(xxlJobGroup, xxlJob, xxlRegister);
					Integer jobInfoId = jobInfoService.addJobInfo(xxlJobInfo);
					LogUtils.info("xxljob 自动注册成功 XxlJobInfo: {}, jobInfoId: {}", xxlJobInfo, jobInfoId);
				}
			}
		}
	}

	private XxlJobInfo createXxlJobInfo(XxlJobGroup xxlJobGroup, XxlJob xxlJob, XxlRegister xxlRegister) {
		XxlJobInfo xxlJobInfo = new XxlJobInfo();
		xxlJobInfo.setJobGroup(xxlJobGroup.getId());
		xxlJobInfo.setJobDesc(xxlRegister.jobDesc());
		xxlJobInfo.setAuthor(xxlRegister.author());
		xxlJobInfo.setScheduleType("CRON");
		xxlJobInfo.setScheduleConf(xxlRegister.cron());
		xxlJobInfo.setGlueType("BEAN");
		xxlJobInfo.setExecutorHandler(xxlJob.value());
		xxlJobInfo.setExecutorRouteStrategy(xxlRegister.executorRouteStrategy());
		xxlJobInfo.setMisfireStrategy("DO_NOTHING");
		xxlJobInfo.setExecutorBlockStrategy("SERIAL_EXECUTION");
		xxlJobInfo.setExecutorTimeout(0);
		xxlJobInfo.setExecutorFailRetryCount(0);
		xxlJobInfo.setGlueRemark("GLUE代码初始化");
		xxlJobInfo.setTriggerStatus(xxlRegister.triggerStatus());

		return xxlJobInfo;
	}

}

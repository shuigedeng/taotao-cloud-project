package com.taotao.cloud.job.xxl.configuration;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.job.xxl.timetask.TimedTaskJobHandler;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Import;

/**
 * 自动任务类自动注入配置
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 22:13:57
 */
@AutoConfiguration
@ConditionalOnBean(XxlJobSpringExecutor.class)
@Import(value = TimedTaskJobHandler.class)
public class TimeTaskAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(TimeTaskAutoConfiguration.class, StarterName.WEB_STARTER);
	}

}

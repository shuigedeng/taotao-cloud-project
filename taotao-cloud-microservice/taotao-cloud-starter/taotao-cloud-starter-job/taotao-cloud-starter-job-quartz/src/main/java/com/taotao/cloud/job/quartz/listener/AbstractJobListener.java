package com.taotao.cloud.job.quartz.listener;

import org.quartz.JobListener;
import org.quartz.Matcher;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.EverythingMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>自定义JobListener，可匹配任务。定义并配置之后，系统可自动注册
 *
 * @author luas
 * @since 1.0
 */
public abstract class AbstractJobListener implements JobListener {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 返回匹配某一、某些Job的匹配策略
	 */
	public Matcher<TriggerKey> matcher() {
		return EverythingMatcher.allTriggers();
	}

}

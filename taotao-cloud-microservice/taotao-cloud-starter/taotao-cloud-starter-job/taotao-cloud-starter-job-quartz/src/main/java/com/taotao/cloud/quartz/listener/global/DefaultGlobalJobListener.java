package com.taotao.cloud.quartz.listener.global;

import com.taotao.cloud.quartz.listener.AbstractJobListener;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Matcher;
import org.quartz.TriggerKey;

import java.time.LocalDateTime;

/**
 * 默认全局JobListener
 *
 * @author luas
 * @since 4.3
 */
public class DefaultGlobalJobListener extends AbstractJobListener {

	@Override
	public String getName() {
		return "defaultGlobalJobListener";
	}

	@Override
	public Matcher<TriggerKey> matcher() {
		return super.matcher();
	}

	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
		if (this.logger.isDebugEnabled()) {
			this.logger.debug("Job {} 即将执行", context.getJobDetail());
		}
	}

	@Override
	public void jobExecutionVetoed(JobExecutionContext context) {
		if (this.logger.isDebugEnabled()) {
			this.logger.debug("Job {} 于 {} 被否决，不再执行", context.getJobDetail(), LocalDateTime.now());
		}
	}

	@Override
	public void jobWasExecuted(JobExecutionContext context,
			JobExecutionException jobException) {
		if (this.logger.isDebugEnabled()) {
			this.logger.debug("Job {} 于 {} 执行，", context.getJobDetail(), LocalDateTime.now());

			if (jobException != null) {
				this.logger.debug("Job {} 执行中发生异常，异常信息为 {}，", context.getJobDetail(), jobException);
			}
		}

	}

}

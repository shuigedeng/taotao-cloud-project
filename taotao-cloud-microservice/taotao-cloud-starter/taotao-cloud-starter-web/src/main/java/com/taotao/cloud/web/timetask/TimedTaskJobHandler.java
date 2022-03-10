package com.taotao.cloud.web.timetask;

import com.taotao.cloud.common.utils.log.LogUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 定时器任务
 */
public class TimedTaskJobHandler {

	@Autowired(required = false)
	private List<EveryMinuteExecute> everyMinuteExecutes;

	@Autowired(required = false)
	private List<EveryHourExecute> everyHourExecutes;

	@Autowired(required = false)
	private List<EveryDayExecute> everyDayExecutes;

	/**
	 * 每分钟任务
	 */
	@XxlJob("everyMinuteExecute")
	public ReturnT<String> everyMinuteExecute(String param) {
		LogUtil.info("每分钟任务执行");
		if (everyMinuteExecutes == null || everyMinuteExecutes.size() == 0) {
			return ReturnT.SUCCESS;
		}

		for (EveryMinuteExecute everyMinuteExecute : everyMinuteExecutes) {
			try {
				everyMinuteExecute.execute();
			} catch (Exception e) {
				LogUtil.error("每分钟任务异常", e);
			}
		}
		return ReturnT.SUCCESS;
	}

	/**
	 * 每小时任务
	 */
	@XxlJob("everyHourExecuteJobHandler")
	public ReturnT<String> everyHourExecuteJobHandler(String param) {
		LogUtil.info("每小时任务执行");
		if (everyHourExecutes == null || everyHourExecutes.size() == 0) {
			return ReturnT.SUCCESS;
		}

		for (EveryHourExecute everyHourExecute : everyHourExecutes) {
			try {
				everyHourExecute.execute();
			} catch (Exception e) {
				LogUtil.error("每分钟任务异常", e);
			}
		}
		return ReturnT.SUCCESS;
	}

	/**
	 * 每日任务
	 */
	@XxlJob("everyDayExecuteJobHandler")
	public ReturnT<String> everyDayExecuteJobHandler(String param) {

		LogUtil.info("每日任务执行");
		if (everyDayExecutes == null || everyDayExecutes.size() == 0) {
			return ReturnT.SUCCESS;
		}

		for (EveryDayExecute everyDayExecute : everyDayExecutes) {
			try {
				everyDayExecute.execute();
			} catch (Exception e) {
				LogUtil.error("每分钟任务异常", e);
			}
		}
		return ReturnT.SUCCESS;
	}


}

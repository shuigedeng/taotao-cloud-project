package com.taotao.cloud.job.xxl.timetask;

import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.lock.support.DistributedLock;
import com.taotao.cloud.lock.support.ZLock;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 定时器任务
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-04 09:15:24
 */
public class TimedTaskJobHandler {

	@Autowired(required = false)
	private List<EveryMinuteExecute> everyMinuteExecutes;

	@Autowired(required = false)
	private List<EveryHourExecute> everyHourExecutes;

	@Autowired(required = false)
	private List<EveryDayExecute> everyDayExecutes;

	@Autowired
	private DistributedLock distributedLock;

	/**
	 * 每分钟任务
	 */
	@XxlJob("everyMinuteExecute")
	public ReturnT<String> everyMinuteExecute(String param) {
		LogUtils.info("每分钟任务执行");
		if (everyMinuteExecutes == null || everyMinuteExecutes.size() == 0) {
			return ReturnT.SUCCESS;
		}

		for (EveryMinuteExecute everyMinuteExecute : everyMinuteExecutes) {
			ZLock result = null;
			try {
				result = distributedLock.tryLock(
					"timetask-everyMinuteExecute-" + everyMinuteExecute.getClass().getName(), 30,
					TimeUnit.MILLISECONDS);
				everyMinuteExecute.execute();
			} catch (Exception e) {
				LogUtils.error("每分钟任务异常", e);
				return ReturnT.FAIL;
			} finally {
				try {
					distributedLock.unlock(result);
				} catch (Exception e) {
					LogUtils.error("每分钟任务异常", e);
				}
			}
		}
		return ReturnT.SUCCESS;
	}

	/**
	 * 每小时任务
	 */
	@XxlJob("everyHourExecuteJobHandler")
	public ReturnT<String> everyHourExecuteJobHandler(String param) {
		LogUtils.info("每小时任务执行");
		if (everyHourExecutes == null || everyHourExecutes.size() == 0) {
			return ReturnT.SUCCESS;
		}

		for (EveryHourExecute everyHourExecute : everyHourExecutes) {
			ZLock result = null;
			try {
				result = distributedLock.tryLock(
					"timetask-everyHourExecute-" + everyHourExecute.getClass().getName(), 30,
					TimeUnit.MILLISECONDS);
				everyHourExecute.execute();
			} catch (Exception e) {
				LogUtils.error("每小时任务异常", e);
				return ReturnT.FAIL;
			} finally {
				try {
					distributedLock.unlock(result);
				} catch (Exception e) {
					LogUtils.error("每小时任务异常", e);
				}
			}
		}
		return ReturnT.SUCCESS;
	}

	/**
	 * 每日任务
	 */
	@XxlJob("everyDayExecuteJobHandler")
	public ReturnT<String> everyDayExecuteJobHandler(String param) {

		LogUtils.info("每日任务执行");
		if (everyDayExecutes == null || everyDayExecutes.size() == 0) {
			return ReturnT.SUCCESS;
		}

		for (EveryDayExecute everyDayExecute : everyDayExecutes) {
			ZLock result = null;
			try {
				result = distributedLock.tryLock(
					"timetask-everyDayExecute-" + everyDayExecute.getClass().getName(),
					30,
					TimeUnit.MILLISECONDS);
				everyDayExecute.execute();
			} catch (Exception e) {
				LogUtils.error("每天任务异常", e);
				return ReturnT.FAIL;
			} finally {
				try {
					distributedLock.unlock(result);
				} catch (Exception e) {
					LogUtils.error("每天任务异常", e);
				}
			}
		}
		return ReturnT.SUCCESS;
	}


}

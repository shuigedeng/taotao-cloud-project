package com.taotao.cloud.job.biz.quartz.record;

import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.job.quartz.entity.QuartzJobLog;
import com.taotao.cloud.job.quartz.utils.QuartzLogRecord;
import org.springframework.stereotype.Component;

@Component
public class QuartzLogRecordImpl implements QuartzLogRecord {

	@Override
	public void addLog(QuartzJobLog quartzJobLog) {
		LogUtils.info("quartz1  QuartzLogRecordImpl : {}", quartzJobLog);
	}
}

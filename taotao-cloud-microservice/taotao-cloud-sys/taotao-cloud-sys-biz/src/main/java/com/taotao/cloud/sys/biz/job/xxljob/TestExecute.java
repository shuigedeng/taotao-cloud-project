package com.taotao.cloud.sys.biz.job.xxljob;

import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.job.xxl.timetask.EveryMinuteExecute;
import org.springframework.stereotype.Component;

/**
 * 测试（每分钟执行）
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 11:54:41
 */
@Component
public class TestExecute implements EveryMinuteExecute {

	@Override
	public void execute() {
		LogUtils.info(Thread.currentThread().getName() + "============ 每分钟执行");
	}
}

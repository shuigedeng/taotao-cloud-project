package com.taotao.cloud.sys.biz.task.xxljob;

import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.web.timetask.EveryMinuteExecute;
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
		LogUtil.info(Thread.currentThread().getName() + "============ 每分钟执行");
	}
}

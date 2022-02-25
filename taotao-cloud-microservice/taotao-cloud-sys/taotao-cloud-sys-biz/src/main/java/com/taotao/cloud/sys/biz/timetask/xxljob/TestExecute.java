package com.taotao.cloud.sys.biz.timetask.xxljob;

import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.web.timetask.EveryMinuteExecute;
import org.springframework.stereotype.Component;

/**
 * 测试（每分钟执行）
 **/
@Component
public class TestExecute implements EveryMinuteExecute {


	@Override
	public void execute() {

		LogUtil.info(Thread.currentThread().getName() + "============ 每分钟执行");
	}
}

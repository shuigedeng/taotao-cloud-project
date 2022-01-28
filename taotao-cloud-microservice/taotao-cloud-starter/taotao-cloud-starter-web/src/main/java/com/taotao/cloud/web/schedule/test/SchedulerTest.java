package com.taotao.cloud.web.schedule.test;

import com.taotao.cloud.common.utils.LogUtil;
import java.time.LocalTime;

/**
 * 模拟动态调整任务执行时间
 *
 * @author jitwxs
 * @date 2021年03月27日 21:52
 */
public class SchedulerTest {

	public void foo() {
		LogUtil.info("{} Execute com.github.jitwxs.sample.ds.test.SchedulerTest#foo",
			LocalTime.now());
	}
}

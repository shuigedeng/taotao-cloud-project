package com.taotao.cloud.job.biz.quartz.task;

import com.taotao.cloud.common.utils.log.LogUtils;
import org.springframework.stereotype.Component;

@Component("TestDemo")
public class TestDemo {

	public void doExecute(String params) throws Exception {
		LogUtils.info("quartz1 TestDemo,方法参数:{}", params);
	}
}

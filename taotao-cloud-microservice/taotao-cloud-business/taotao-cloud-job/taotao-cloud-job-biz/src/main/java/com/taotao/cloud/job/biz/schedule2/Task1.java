package com.taotao.cloud.job.biz.schedule2;

import com.taotao.cloud.common.utils.log.LogUtils;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.stereotype.Component;

/**
 * 定时任务示例类
 */
@Component("taskDemo")
public class Task1 {

	public void taskByParams(String params) {
		LogUtils.info("taskByParams执行时间:{}",
			new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		LogUtils.info("taskByParams执行有参示例任务：{}", params);
	}

	public void taskNoParams() {
		LogUtils.info("taskByParams执行时间:{}",
			new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		LogUtils.info("taskNoParams执行无参示例任务");
	}

	public void test(String params) {
		LogUtils.info("test执行时间:{}",
			new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		LogUtils.info("test执行有参示例任务：{}", params);
	}
} 

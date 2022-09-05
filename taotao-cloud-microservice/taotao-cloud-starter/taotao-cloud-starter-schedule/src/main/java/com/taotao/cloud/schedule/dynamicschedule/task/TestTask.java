package com.taotao.cloud.schedule.dynamicschedule.task;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

/**
 * 测试定时任务(演示Demo，可删除)
 *
 * testTask为spring bean的名称
 *
 */
@Component("testTask")
@Slf4j
public class TestTask {


	public void test(String params){
		log.info("我是带参数的test方法，正在被执行，参数为：" + params);

		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}


	public void test2(){
		log.info("我是不带参数的test2方法，正在被执行");
	}
}

package com.taotao.cloud.log.biz.web.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * spring框架的监听事件 想要启动的时候做一些事 可以放这
 *
 * @since 2022/05/01
 */
@Slf4j
@Component
public class SpringListener implements ApplicationRunner {

	/**
	 * 启动后要做的一些初始化动作可以放这里
	 *
	 * @param args incoming application arguments
	 * @throws Exception on error
	 */
	@Override
	public void run(ApplicationArguments args) throws Exception {

	}

}

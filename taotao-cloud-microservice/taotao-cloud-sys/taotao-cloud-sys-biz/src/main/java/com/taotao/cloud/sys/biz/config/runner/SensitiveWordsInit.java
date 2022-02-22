package com.taotao.cloud.sys.biz.config.runner;

import com.taotao.cloud.sys.biz.service.ISensitiveWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class SensitiveWordsInit implements ApplicationRunner {

	@Autowired
	private ISensitiveWordService sensitiveWordsService;

	/**
	 * consumer 启动时，实时更新一下过滤词
	 */
	@Override
	public void run(ApplicationArguments args) {
		//sensitiveWordsService.resetCache();
	}

}

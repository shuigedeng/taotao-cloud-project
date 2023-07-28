package com.taotao.cloud.auth.biz.uaa;

import com.taotao.cloud.common.utils.log.LogUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

public class MyApplicationRunListener implements SpringApplicationRunListener {

  @Override
  public void failed(ConfigurableApplicationContext context, Throwable exception) {
	  exception.printStackTrace();

	  LogUtils.error(exception, "启动失败");
  }

}

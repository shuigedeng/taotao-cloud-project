package com.taotao.cloud.sys.biz.event.application;

import com.taotao.cloud.common.utils.log.LogUtils;
import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class WebSevletEventListener {

	@Component
	public static class ServletWebServerInitializedEventListener implements ApplicationListener<ServletWebServerInitializedEvent> {
		@Override
		public void onApplicationEvent(ServletWebServerInitializedEvent event) {
			LogUtils.info("ServletWebServerInitializedEventListener ----- ServletWebServerInitializedEvent onApplicationEvent {}", event);

		}
	}
}

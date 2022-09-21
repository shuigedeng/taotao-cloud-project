package com.taotao.cloud.sys.biz.event.application;

import com.taotao.cloud.common.utils.log.LogUtils;
import org.springframework.cloud.stream.binder.BindingCreatedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class StreamEventListener {
	@Component
	public static class BindingCreatedEventListener implements ApplicationListener<BindingCreatedEvent> {
		@Override
		public void onApplicationEvent(BindingCreatedEvent event) {
			LogUtils.info("StreamEventListener ----- BindingCreatedEvent onApplicationEvent {}", event);

		}
	}


}

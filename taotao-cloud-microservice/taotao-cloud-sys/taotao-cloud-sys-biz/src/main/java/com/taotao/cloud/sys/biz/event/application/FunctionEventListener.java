package com.taotao.cloud.sys.biz.event.application;

import com.taotao.cloud.common.utils.log.LogUtils;
import org.springframework.cloud.function.context.catalog.FunctionCatalogEvent;
import org.springframework.cloud.function.context.catalog.FunctionRegistrationEvent;
import org.springframework.cloud.function.context.catalog.FunctionUnregistrationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class FunctionEventListener {
	@Component
	public static class FunctionCatalogEventListener implements ApplicationListener<FunctionCatalogEvent> {
		@Override
		public void onApplicationEvent(FunctionCatalogEvent event) {
			LogUtils.info("FunctionEventListener ----- FunctionCatalogEvent onApplicationEvent {}", event);

		}
	}

	@Component
	public static class FunctionRegistrationEventListener implements ApplicationListener<FunctionRegistrationEvent> {
		@Override
		public void onApplicationEvent(FunctionRegistrationEvent event) {
			LogUtils.info("FunctionEventListener ----- FunctionRegistrationEvent onApplicationEvent {}", event);

		}
	}

	@Component
	public static class FunctionUnregistrationEventListener implements ApplicationListener<FunctionUnregistrationEvent> {
		@Override
		public void onApplicationEvent(FunctionUnregistrationEvent event) {
			LogUtils.info("FunctionEventListener ----- FunctionUnregistrationEvent onApplicationEvent {}", event);

		}
	}
}

package com.taotao.cloud.sys.biz.event.application;

import com.taotao.cloud.common.utils.log.LogUtils;
import org.springframework.cloud.client.discovery.event.HeartbeatEvent;
import org.springframework.cloud.client.discovery.event.InstancePreRegisteredEvent;
import org.springframework.cloud.client.discovery.event.InstanceRegisteredEvent;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class CloudEventListener {
	@Component
	public static class RefreshScopeRefreshedEventListener implements ApplicationListener<RefreshScopeRefreshedEvent> {
		@Override
		public void onApplicationEvent(RefreshScopeRefreshedEvent event) {
			LogUtils.info("CloudEventListener ----- RefreshScopeRefreshedEvent onApplicationEvent {}", event);

		}
	}


	@Component
	public static class EnvironmentChangeEventListener implements ApplicationListener<EnvironmentChangeEvent> {
		@Override
		public void onApplicationEvent(EnvironmentChangeEvent event) {
			LogUtils.info("CloudEventListener ----- EnvironmentChangeEvent onApplicationEvent {}", event);

		}
	}

	@Component
	public static class HeartbeatEventListener implements ApplicationListener<HeartbeatEvent> {
		@Override
		public void onApplicationEvent(HeartbeatEvent event) {
			LogUtils.info("CloudEventListener ----- HeartbeatEvent onApplicationEvent {}", event);

		}
	}

	@Component
	public static class InstanceRegisteredEventListener implements ApplicationListener<InstanceRegisteredEvent> {
		@Override
		public void onApplicationEvent(InstanceRegisteredEvent event) {
			LogUtils.info("CloudEventListener ----- InstanceRegisteredEvent onApplicationEvent {}", event);

		}
	}

	@Component
	public static class InstancePreRegisteredEventEventListener implements ApplicationListener<InstancePreRegisteredEvent> {
		@Override
		public void onApplicationEvent(InstancePreRegisteredEvent event) {
			LogUtils.info("CloudEventListener ----- InstancePreRegisteredEvent onApplicationEvent {}", event);

		}
	}
}

package com.taotao.cloud.sys.biz.event.application;

import com.taotao.cloud.common.utils.log.LogUtils;
import org.springframework.boot.context.event.ApplicationContextInitializedEvent;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class SpringApplicationEventListener {

	/**
	 * SpringApplication的生命周期事件一共有七个，它们发生的顺序如下：
	 * <pre class="code">
	 * ApplicationStartingEvent 这个事件是在一个SpringApplication对象的初始化和监听器的注册之后，抛出的。
	 * ApplicationEnvironmentPreparedEvent 这个事件在Environment对象创建之后，Context对象创建之前，抛出。
	 * ApplicationContextInitializedEvent 这个事件在ApplicationContext对象被初始化后抛出，抛出的时候，所有bean的定义还没被加载。
	 * ApplicationPreparedEvent 这个事件在bean定义被加载之后，Context对象刷新之前抛出。
	 * ApplicationStartedEvent 这个事件在Context对象刷新之后，应用启动器被调用之前抛出。
	 * ApplicationReadyEvent 这个事件在应用启动器被调用之后抛出，这个代表着应用已经被正常的启动，可以接收请求了。
	 * ApplicationFailedEvent 这个事件只有在应用启动出现异常，无法正常启动时才会抛出。
	 * </pre>
	 */
	@Component
	public static class ApplicationStartingEventListener implements ApplicationListener<ApplicationStartingEvent> {
		@Override
		public void onApplicationEvent(ApplicationStartingEvent event) {
			LogUtils.info("SpringApplicationEventListener ----- ApplicationStartingEvent onApplicationEvent {}", event);

		}
	}

	@Component
	public static class ApplicationEnvironmentPreparedEventListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {
		@Override
		public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
			LogUtils.info("SpringApplicationEventListener ----- ApplicationEnvironmentPreparedEvent onApplicationEvent {}", event);

		}
	}

	@Component
	public static class ApplicationContextInitializedEventListener implements ApplicationListener<ApplicationContextInitializedEvent> {
		@Override
		public void onApplicationEvent(ApplicationContextInitializedEvent event) {
			LogUtils.info("SpringApplicationEventListener ----- ApplicationContextInitializedEvent onApplicationEvent {}", event);

		}
	}

	@Component
	public static class ApplicationPreparedEventListener implements ApplicationListener<ApplicationPreparedEvent> {
		@Override
		public void onApplicationEvent(ApplicationPreparedEvent event) {
			LogUtils.info("SpringApplicationEventListener ----- ApplicationPreparedEvent onApplicationEvent {}", event);

		}
	}

	@Component
	public static class ApplicationStartedEventListener implements ApplicationListener<ApplicationStartedEvent> {
		@Override
		public void onApplicationEvent(ApplicationStartedEvent event) {
			LogUtils.info("SpringApplicationEventListener ----- ApplicationStartedEvent onApplicationEvent {}", event);

		}
	}

	@Component
	public static class ApplicationReadyEventListener implements ApplicationListener<ApplicationReadyEvent> {
		@Override
		public void onApplicationEvent(ApplicationReadyEvent event) {
			LogUtils.info("SpringApplicationEventListener ----- ApplicationReadyEvent onApplicationEvent {}", event);

		}
	}

	@Component
	public static class ApplicationFailedEventListener implements ApplicationListener<ApplicationFailedEvent> {
		@Override
		public void onApplicationEvent(ApplicationFailedEvent event) {
			LogUtils.info("SpringApplicationEventListener ----- ApplicationFailedEvent onApplicationEvent {}", event);

		}
	}
}

//package com.taotao.cloud.sys.biz.event.application;
//
//import com.taotao.cloud.common.utils.log.LogUtils;
//import org.apache.dubbo.config.spring.context.event.DubboApplicationStateEvent;
//import org.apache.dubbo.config.spring.context.event.DubboConfigInitEvent;
//import org.apache.dubbo.config.spring.context.event.ServiceBeanExportedEvent;
//import org.springframework.context.ApplicationListener;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.stereotype.Component;
//
//@Configuration
//public class DubboEventListener {
//	@Component
//	public static class ServiceBeanExportedEventListener implements ApplicationListener<ServiceBeanExportedEvent> {
//		@Override
//		public void onApplicationEvent(ServiceBeanExportedEvent event) {
//			LogUtils.info("DubboEventListener ----- ServiceBeanExportedEvent onApplicationEvent {}", event);
//
//		}
//	}
//
//	@Component
//	public static class DubboApplicationStateEventListener implements ApplicationListener<DubboApplicationStateEvent> {
//		@Override
//		public void onApplicationEvent(DubboApplicationStateEvent event) {
//			LogUtils.info("DubboEventListener ----- DubboApplicationStateEvent onApplicationEvent {}", event);
//
//		}
//	}
//
//	@Component
//	public static class DubboConfigInitEventListener implements ApplicationListener<DubboConfigInitEvent> {
//		@Override
//		public void onApplicationEvent(DubboConfigInitEvent event) {
//			LogUtils.info("DubboEventListener ----- DubboConfigInitEvent onApplicationEvent {}", event);
//
//		}
//	}
//
//
//}

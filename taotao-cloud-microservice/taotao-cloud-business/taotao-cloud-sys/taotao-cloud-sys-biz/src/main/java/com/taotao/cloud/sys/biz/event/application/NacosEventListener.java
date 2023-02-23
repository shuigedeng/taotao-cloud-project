package com.taotao.cloud.sys.biz.event.application;

import com.alibaba.cloud.nacos.event.NacosDiscoveryInfoChangedEvent;
import com.taotao.cloud.common.utils.log.LogUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class NacosEventListener {

	@Component
	public static class NacosDiscoveryInfoChangedEventListener implements ApplicationListener<NacosDiscoveryInfoChangedEvent> {
		@Override
		public void onApplicationEvent(NacosDiscoveryInfoChangedEvent event) {
			LogUtils.info("NacosEventListener ----- NacosDiscoveryInfoChangedEvent onApplicationEvent {}", event);
		}
	}

	// @Autowired
	// private NacosRefresher nacosRefresher;
	//
	// @NacosConfigListener(dataId = "config")
	// private void onMessage(String msg) {
	// 	nacosRefresher.refresh(msg, ConfigFileTypeEnum.YAML);
	// 	System.out.println("配置变动" + msg);
	// }
	//
	// @Component
	// public static class NacosRefresher extends AbstractRefresher {
	// }

}

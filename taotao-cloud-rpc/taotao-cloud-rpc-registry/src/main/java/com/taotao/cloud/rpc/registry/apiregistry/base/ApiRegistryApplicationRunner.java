package com.taotao.cloud.rpc.registry.apiregistry.base;

import com.taotao.cloud.rpc.common.common.support.status.enums.StatusEnum;
import com.taotao.cloud.rpc.registry.apiregistry.ApiRegistryProperties;
import com.taotao.cloud.rpc.registry.apiregistry.registry.BaseRegistry;
import org.springframework.core.annotation.Order;

//@Order(Integer.MAX_VALUE - 1)
//public class ApiRegistryApplicationRunner implements StatusListener {
//	@Override
//	public void onApplicationEvent(StatusEnum statusEnum) {
//		if (statusEnum == StatusEnum.RUNNING) {
//			BaseRegistry registry = ContextUtils.getBean(BaseRegistry.class, false);
//			if (registry != null) {
//				LogUtils.info(ApiRegistryProperties.Project, "ApiRegistry注册开启中.....");
//				registry.register();
//			}
//		}
//	}
//}

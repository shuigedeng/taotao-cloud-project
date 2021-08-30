package com.taotao.cloud.canal.configuration;


import com.taotao.cloud.canal.core.SimpleCanalClient;
import com.taotao.cloud.canal.interfaces.CanalClient;
import com.taotao.cloud.canal.properties.CanalProperties;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.LogUtil;
import org.springframework.context.annotation.Bean;

public class CanalClientConfiguration {

	/**
	 * 返回 canal 的客户端
	 */
	@Bean
	private CanalClient canalClient(CanalProperties canalProperties) {
		LogUtil.info(CanalClientConfiguration.class, StarterName.CANAL_STARTER,
			" CanalClient 正在尝试连接 canal 客户端....");

		//连接 canal 客户端
		// CanalClient canalClient = new SimpleCanalClient(canalConfig, MessageTransponders.defaultMessageTransponder());
		CanalClient canalClient = new SimpleCanalClient(canalProperties);
		LogUtil.info(CanalClientConfiguration.class, StarterName.CANAL_STARTER,
			" CanalClient 正在尝试开启 canal 客户端....");

		//开启 canal 客户端
		canalClient.start();

		LogUtil.info(CanalClientConfiguration.class, StarterName.CANAL_STARTER,
			" CanalClient 启动 canal 客户端成功....");
		//返回结果
		return canalClient;
	}
}

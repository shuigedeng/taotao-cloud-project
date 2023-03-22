package com.taotao.cloud.log.biz.shortlink.config;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;

/**
 * This is Description
 *
 * @since 2022/05/03
 */
@Configuration
public class SpringShutdownHook {

	private static final Logger logger = LoggerFactory.getLogger(SpringShutdownHook.class);

	@Resource
	private ConfigurableApplicationContext configurableApplicationContext;

	@Value("${dubbo.service.shutdown.wait:10000}")
	private static int SHUTDOWN_TIMEOUT_WAIT;

	public SpringShutdownHook() {
	}

	@PostConstruct
	public void registerShutdownHook() {
		logger.info("[SpringShutdownHook] Register ShutdownHook....");

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				int timeOut = SHUTDOWN_TIMEOUT_WAIT;
				SpringShutdownHook.logger
					.info(
						"[SpringShutdownHook] Application need sleep {} seconds to wait Dubbo shutdown",
						timeOut / 1000.0D);
				Thread.sleep((long) timeOut);
				SpringShutdownHook.this.configurableApplicationContext.close();
				SpringShutdownHook.logger
					.info("[SpringShutdownHook] ApplicationContext closed, Application shutdown");
			} catch (InterruptedException var2) {
				SpringShutdownHook.logger.error(var2.getMessage(), var2);
			}
		}));
	}

}

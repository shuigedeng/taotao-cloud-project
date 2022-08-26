package com.taotao.cloud.monitor.kuding;

/**
 * org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
 * com.config.PromethuesConfig,\
 * com.config.PromethuesDingDingNoticeConfig,\
 * com.config.PromethuesEmailNoticeConfig,\
 * com.config.exceptionnotice.ExceptionNoticeConfig,\
 * com.config.exceptionnotice.ExceptionNoticeCommonTypeConfig,\
 * com.config.exceptionnotice.ExceptionNoticeDingdingSendingConfig,\
 * com.config.exceptionnotice.ExceptionNoticeEmailSendingConfig,\
 * com.config.exceptionnotice.ExceptionNoticeSendConfig,\
 * com.config.exceptionnotice.ExceptionNoticeWebListenConfig,\
 * com.config.servicemonitor.ServiceMonitorConfig,\
 * com.config.servicemonitor.ConsulHealthCheckHandlerConfig,\
 * com.config.servicemonitor.ExceptionNoticeEmailSendingConfig,\
 * com.config.servicemonitor.ServiceMonitorControlConfig,\
 * com.config.servicemonitor.ServiceMonitorDingdingSendingConfig,\
 * com.config.servicemonitor.ServiceMonitorListenerConfig
 */
/**
 * 此目录下的文件需要修改
 */
public class ExceptionNoticeException extends RuntimeException {

	public ExceptionNoticeException(String message) {
		super(message);
	}

	private static final long serialVersionUID = 9006489175368117913L;

}

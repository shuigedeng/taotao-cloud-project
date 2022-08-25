package com.taotao.cloud.monitor.kuding.exceptions;

public class PrometheusException extends RuntimeException {

	private static final long serialVersionUID = 5505450495558938108L;

	public PrometheusException() {
		super();
	}

	public PrometheusException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public PrometheusException(String message, Throwable cause) {
		super(message, cause);
	}

	public PrometheusException(String message) {
		super(message);
	}

	public PrometheusException(Throwable cause) {
		super(cause);
	}

}

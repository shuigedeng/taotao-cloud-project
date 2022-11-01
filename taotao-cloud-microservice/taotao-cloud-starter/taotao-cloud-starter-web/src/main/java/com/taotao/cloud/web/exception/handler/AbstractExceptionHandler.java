package com.taotao.cloud.web.exception.handler;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.exceptions.ExceptionUtil;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.common.utils.servlet.RequestUtils;
import com.taotao.cloud.web.exception.domain.ExceptionMessage;
import com.taotao.cloud.web.exception.domain.ExceptionNoticeResponse;
import com.taotao.cloud.web.exception.properties.ExceptionHandleProperties;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.context.request.NativeWebRequest;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public abstract class AbstractExceptionHandler extends Thread
	implements ExceptionHandler, InitializingBean, DisposableBean {

	private final BlockingQueue<QueueMessage> queue = new LinkedBlockingQueue<>();

	private static final String NULL_MESSAGE_KEY = "";

	protected final ExceptionHandleProperties config;

	private volatile boolean flag = true;

	/**
	 * 通知消息存放 e.message 堆栈信息
	 */
	private final Map<String, ExceptionMessage> messages;

	/**
	 * 本地物理地址
	 */
	private String mac;

	/**
	 * 本地hostname
	 */
	private String hostname;

	/**
	 * 本地ip
	 */
	private String ip;

	private final String applicationName;

	protected AbstractExceptionHandler(ExceptionHandleProperties config,
									   String applicationName) {
		this.config = config;
		messages = new ConcurrentHashMap<>(config.getMax() * 2);
		this.applicationName = applicationName;
		try {
			InetAddress ia = InetAddress.getLocalHost();
			hostname = ia.getHostName();
			ip = ia.getHostAddress();

			byte[] macByte = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < macByte.length; i++) {
				sb.append(String.format("%02X%s", macByte[i], (i < macByte.length - 1) ? "-" : ""));
			}
			this.mac = sb.toString();
		} catch (Exception e) {
			mac = "获取失败!";
		}
	}

	@Override
	@SuppressWarnings("all")
	public void run() {
		String key;
		TimeInterval interval = new TimeInterval();
		long threadId = Thread.currentThread().getId();
		// 未被中断则一直运行
		while (flag) {
			int i = 0;
			while (i < config.getMax() && interval.intervalSecond() < config.getTime()) {
				QueueMessage queueMessage = null;
				try {
					// 如果 i=0,即 当前未处理异常，则等待超时时间为 1 小时， 否则为 10 秒
					queueMessage = queue.poll(i == 0 ? TimeUnit.HOURS.toSeconds(1) : 10, TimeUnit.SECONDS);
				} catch (InterruptedException e) {
					interrupt();
				}

				if (queueMessage != null) {
					// key = t.getMessage() == null ? NULL_MESSAGE_KEY : t.getMessage();
					key = queueMessage.getTraceId();
					// i++
					if (i++ == 0) {
						// 第一次收到数据, 重置计时
						interval.restart();
						ExceptionMessage message = toMessage(queueMessage);
						message.setThreadId(threadId);
						messages.put(key, message);
					} else {
						if (messages.containsKey(key)) {
							messages.put(key, messages.get(key).increment());
						} else {
							ExceptionMessage message = toMessage(queueMessage);
							message.setThreadId(threadId);
							messages.put(key, message);
						}
					}
				}
			}

			// 一次处理结束
			if (messages.size() > 0) {
				// 如果需要发送的消息不为空
				messages.forEach((k, v) -> {
					try {
						ExceptionNoticeResponse response = send(v);
						if (!response.isSuccess()) {
							LogUtils.error("消息通知发送失败! msg: {}", response.getErrMsg());
						}
					} catch (Exception e) {
						LogUtils.error("消息通知时发生异常", e);
					}
				});
				messages.clear();
			}
			interval.restart();
		}
	}

	public ExceptionMessage toMessage(QueueMessage queueMessage) {
		ExceptionMessage message = new ExceptionMessage();
		message.setTraceId(queueMessage.getTraceId());
		message.setNumber(1);
		message.setMac(mac);
		message.setApplicationName(applicationName);
		message.setHostname(hostname);
		message.setIp(ip);
		message.setRequestUri(queueMessage.getRequestUri());
		message.setTime(DateUtil.now());
		message.setStack(
			ExceptionUtil.stacktraceToString(queueMessage.getThrowable(), config.getLength()).replace("\\r", ""));
		return message;
	}

	/**
	 * 发送通知
	 *
	 * @param sendMessage 发送的消息
	 * @return 返回消息发送状态，如果发送失败需要设置失败信息
	 */
	public abstract ExceptionNoticeResponse send(ExceptionMessage sendMessage);

	@Override
	public void handle(NativeWebRequest req, Throwable throwable, String traceId) {
		try {
			String requestUri = RequestUtils.getRequest() == null ? "uri not found" : RequestUtils.getRequest().getRequestURI();

			// 是否忽略该异常
			boolean ignore = false;

			// 只有不是忽略的异常类才会插入异常消息队列
			if (Boolean.FALSE.equals(config.getIgnoreChild())) {
				// 不忽略子类
				ignore = config.getIgnoreExceptions().contains(throwable.getClass());
			} else {
				// 忽略子类
				for (Class<? extends Throwable> ignoreException : config.getIgnoreExceptions()) {
					// 属于子类
					if (ignoreException.isAssignableFrom(throwable.getClass())) {
						ignore = true;
						break;
					}
				}
			}

			QueueMessage message = new QueueMessage(throwable, traceId, requestUri);
			// 不忽略则插入队列
			if (!ignore) {
				queue.put(message);
			}
		} catch (InterruptedException e) {
			interrupt();
		} catch (Exception e) {
			LogUtils.error("往异常消息队列插入新异常时出错", e);
		}
	}

	@Override
	public void afterPropertiesSet() {
		initThread();
	}

	protected void initThread() {
		this.setName("exception-notice");
		this.start();
	}

	@Override
	public void destroy() throws Exception {
		this.flag = false;
		this.interrupt();
	}


	private static class QueueMessage {
		private Throwable throwable;
		private String traceId;
		private String requestUri;

		public QueueMessage(Throwable throwable, String traceId, String requestUri) {
			this.throwable = throwable;
			this.traceId = traceId;
			this.requestUri = requestUri;
		}

		public Throwable getThrowable() {
			return throwable;
		}

		public void setThrowable(Throwable throwable) {
			this.throwable = throwable;
		}

		public String getTraceId() {
			return traceId;
		}

		public void setTraceId(String traceId) {
			this.traceId = traceId;
		}

		public String getRequestUri() {
			return requestUri;
		}

		public void setRequestUri(String requestUri) {
			this.requestUri = requestUri;
		}
	}

}

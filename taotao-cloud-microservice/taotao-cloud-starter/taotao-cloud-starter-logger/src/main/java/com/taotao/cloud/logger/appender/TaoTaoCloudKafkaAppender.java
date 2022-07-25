/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.logger.appender;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.spi.AppenderAttachableImpl;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.danielwegener.logback.kafka.KafkaAppenderConfig;
import com.github.danielwegener.logback.kafka.delivery.FailedDeliveryCallback;
import com.google.common.base.Stopwatch;
import com.taotao.cloud.common.utils.log.LogUtil;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.serialization.ByteArraySerializer;

/**
 * TaotaoKafkaAppender
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/6/12 16:28
 */
public class TaoTaoCloudKafkaAppender<E> extends KafkaAppenderConfig<E> {

	/**
	 * Kafka clients uses this prefix for its slf4j logging. This appender defers appends of any
	 * Kafka logs since it could cause harmful infinite recursion/self feeding effects.
	 */
	private static final String KAFKA_LOGGER_PREFIX = "org.apache.kafka.clients";
	private static final int THRESHOLD = 1000;

	private final Stopwatch  currentStopwatch = Stopwatch.createStarted();
	private final Stopwatch  lastSuccessStopwatch = Stopwatch.createStarted();
	private final Stopwatch  lastErrorStopwatch = Stopwatch.createStarted();

	private final AtomicLong sendErrorNum = new AtomicLong(0L);
	private final AtomicLong msgErrorNum = new AtomicLong(0L);
	private final AtomicLong sendSuccessNum = new AtomicLong(0L);

	private LazyProducer lazyProducer = null;
	private final AppenderAttachableImpl<E> aai = new AppenderAttachableImpl<E>();
	private final ConcurrentLinkedQueue<E> queue = new ConcurrentLinkedQueue<E>();
	private final FailedDeliveryCallback<E> failedDeliveryCallback = (evt, throwable) -> {
		aai.appendLoopOnAppenders(evt);

		long andIncrement = sendErrorNum.getAndIncrement();
		if (andIncrement > 0 && andIncrement % THRESHOLD == 0) {
			errorLog(andIncrement, "系统日志消息发送失败");
		}
	};

	public TaoTaoCloudKafkaAppender() {
		// setting these as config values sidesteps an unnecessary warning (minor bug in KafkaProducer)
		addProducerConfigValue(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
			ByteArraySerializer.class.getName());
		addProducerConfigValue(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
			ByteArraySerializer.class.getName());
	}

	@Override
	public void doAppend(E e) {
		ensureDeferredAppends();
		if (e instanceof ILoggingEvent && ((ILoggingEvent) e).getLoggerName()
			.startsWith(KAFKA_LOGGER_PREFIX)) {
			deferAppend(e);
		} else {
			super.doAppend(e);
		}
	}

	@Override
	public void start() {
		// only error free appenders should be activated
		if (!checkPrerequisites()) {
			return;
		}

		if (partition != null && partition < 0) {
			partition = null;
		}

		lazyProducer = new LazyProducer();

		super.start();
	}

	@Override
	public void stop() {
		super.stop();
		if (lazyProducer != null && lazyProducer.isInitialized()) {
			try {
				lazyProducer.get().close();
			} catch (KafkaException e) {
				this.addWarn("Failed to shut down kafka producer: " + e.getMessage(), e);
			}
			lazyProducer = null;
		}
	}

	@Override
	public void addAppender(Appender<E> newAppender) {
		aai.addAppender(newAppender);
	}

	@Override
	public Iterator<Appender<E>> iteratorForAppenders() {
		return aai.iteratorForAppenders();
	}

	@Override
	public Appender<E> getAppender(String name) {
		return aai.getAppender(name);
	}

	@Override
	public boolean isAttached(Appender<E> appender) {
		return aai.isAttached(appender);
	}

	@Override
	public void detachAndStopAllAppenders() {
		aai.detachAndStopAllAppenders();
	}

	@Override
	public boolean detachAppender(Appender<E> appender) {
		return aai.detachAppender(appender);
	}

	@Override
	public boolean detachAppender(String name) {
		return aai.detachAppender(name);
	}

	@Override
	protected void append(E e) {
		final byte[] payload = encoder.encode(e);
		String s = new String(payload);

		JSONObject jsonObject;
		try {
			jsonObject = JSONUtil.parseObj(s
				.replace("[", "#")
				.replace("]", "#")
				.replace("\n", "")
			);
		} catch (Exception exception) {
			long andIncrement = msgErrorNum.getAndIncrement();
			if (andIncrement > 0 && andIncrement % THRESHOLD == 0) {
				errorLog(andIncrement, "系统日志消息处理失败");
			}

			return;
		}

		jsonObject.set("ctime",
			String.valueOf(LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli()));
		final byte[] key = keyingStrategy.createKey(e);

		final Long timestamp = isAppendTimestamp() ? getTimestamp(e) : null;
		final ProducerRecord<byte[], String> record = new ProducerRecord<>(topic, partition,
			timestamp, key,
			JSONUtil.toJsonStr(jsonObject));

		final Producer<byte[], String> producer = lazyProducer.get();
		if (producer != null) {
			try {
				deliveryStrategy.send(producer, record, e, failedDeliveryCallback);

				long andIncrement = sendSuccessNum.getAndIncrement();
				if (andIncrement > 0 && andIncrement % THRESHOLD == 0) {
					successLog(andIncrement);
				}
			} catch (Exception ex) {
				long andIncrement = sendErrorNum.getAndIncrement();
				if (andIncrement > 0 && andIncrement % THRESHOLD == 0) {
					errorLog(andIncrement, "系统日志消息发送失败");
				}
			}
		} else {
			sendErrorNum.getAndIncrement();
			LogUtil.error("kafka producer not init");
			failedDeliveryCallback.onFailedDelivery(e, null);
		}
	}

	protected void errorLog(long num, String msg) {
		long hour = currentStopwatch.elapsed(TimeUnit.HOURS);
		long minute = currentStopwatch.elapsed(TimeUnit.MINUTES);
		long seconds = currentStopwatch.elapsed(TimeUnit.SECONDS);

		long lastSeconds = lastErrorStopwatch.elapsed(TimeUnit.SECONDS);
		long lastMinute =lastErrorStopwatch.elapsed(TimeUnit.MINUTES);
		long lastHour = lastErrorStopwatch.elapsed(TimeUnit.HOURS);

		LogUtil.error("KafkaAppender [{}已达 {}条 共用时{}秒 {}分 {}小时, 最近一次用时{}秒 {}分 {}小时]", msg, num, seconds, minute, hour, lastSeconds, lastMinute, lastHour);
		lastErrorStopwatch.reset().start();
	}

	protected void successLog(long num) {
		long hour = currentStopwatch.elapsed(TimeUnit.HOURS);
		long minute = currentStopwatch.elapsed(TimeUnit.MINUTES);
		long seconds = currentStopwatch.elapsed(TimeUnit.SECONDS);

		long lastSeconds = lastSuccessStopwatch.elapsed(TimeUnit.SECONDS);
		long lastMinute =lastSuccessStopwatch.elapsed(TimeUnit.MINUTES);
		long lastHour = lastSuccessStopwatch.elapsed(TimeUnit.HOURS);

		LogUtil.info("KafkaAppender [{}已达 {}条 共用时{}秒 {}分 {}小时, 最近一次用时{}秒 {}分 {}小时]", "系统日志消息发送成功", num, seconds, minute, hour, lastSeconds, lastMinute, lastHour);
		lastSuccessStopwatch.reset().start();
	}

	protected Long getTimestamp(E e) {
		if (e instanceof ILoggingEvent) {
			return ((ILoggingEvent) e).getTimeStamp();
		} else {
			return System.currentTimeMillis();
		}
	}

	protected Producer<byte[], String> createProducer() {
		return new KafkaProducer<>(new HashMap<>(producerConfig));
	}

	private void deferAppend(E event) {
		queue.add(event);
	}

	// drains queue events to super
	private void ensureDeferredAppends() {
		E event;

		while ((event = queue.poll()) != null) {
			super.doAppend(event);
		}
	}

	/**
	 * Lazy initializer for producer, patterned after commons-lang.
	 *
	 * @see <a href="https://commons.apache.org/proper/commons-lang/javadocs/api-3.4/org/apache/commons/lang3/concurrent/LazyInitializer.html">LazyInitializer</a>
	 */
	private class LazyProducer {

		private volatile Producer<byte[], String> producer;

		public Producer<byte[], String> get() {
			Producer<byte[], String> result = this.producer;
			if (result == null) {
				synchronized (this) {
					result = this.producer;
					if (result == null) {
						this.producer = result = this.initialize();
					}
				}
			}

			return result;
		}

		protected Producer<byte[], String> initialize() {
			Producer<byte[], String> producer = null;
			try {
				producer = createProducer();
			} catch (Exception e) {
				addError("error creating producer", e);
			}
			return producer;
		}

		public boolean isInitialized() {
			return producer != null;
		}
	}
}

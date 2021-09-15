/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.health.warn;

import com.taotao.cloud.common.constant.StarterNameConstant;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.common.utils.StringUtil;
import com.taotao.cloud.core.monitor.MonitorThreadPool;
import com.taotao.cloud.core.properties.CoreProperties;
import com.taotao.cloud.core.utils.PropertyUtil;
import com.taotao.cloud.core.utils.RequestUtil;
import com.taotao.cloud.health.enums.WarnTypeEnum;
import com.taotao.cloud.health.model.Message;
import com.taotao.cloud.health.properties.WarnProperties;
import com.taotao.cloud.health.utils.ExceptionUtils;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

/**
 * WarnProvider
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-09 11:04:53
 */
public class WarnProvider extends AbstractWarn implements AutoCloseable,
	ApplicationRunner {

	private ConcurrentLinkedDeque<Message> messages = new ConcurrentLinkedDeque<>();
	private AtomicInteger atomicInteger = new AtomicInteger(0);
	private final Object lock = new Object();
	private List<AbstractWarn> warns = new ArrayList<>();
	private boolean isClose;
	private DuplicateFilter duplicateFilter;
	private AtomicBoolean atomicChannel = new AtomicBoolean(false);
	private WarnProperties warnProperties;
	private MonitorThreadPool monitorThreadPool;

	public WarnProvider(
		WarnProperties warnProperties,
		MonitorThreadPool monitorThreadPool) {
		this.warnProperties = warnProperties;
		this.monitorThreadPool = monitorThreadPool;
		this.duplicateFilter = new DuplicateFilter(warnProperties);
		this.isClose = false;

		registerWarn();

		this.monitorThreadPool.monitorSubmit("系统任务: WarnProvider 实时报警任务", () -> {
			while (!this.monitorThreadPool.monitorIsShutdown() && !isClose) {
				try {
					notifyRunning();
				} catch (Exception exp) {
					LogUtil.warn(StarterNameConstant.HEALTH_STARTER, "WarnProvider 消息循环异常");
				}

				try {
					Thread.sleep(warnProperties.getTimeSpan() * 1000L);
				} catch (Exception e) {
					LogUtil.error(e);
				}
			}
		});
	}

	/**
	 * registerWarn
	 *
	 * @author shuigedeng
	 * @since 2021-09-10 16:18:47
	 */
	public void registerWarn() {
		if (warnProperties.isDingDingWarnEnabled()) {
			warns.add(new DingdingWarn());
		}

		if (warnProperties.isEmailWarnEnabled()) {
			warns.add(new MailWarn());
		}

		if (warnProperties.isSmsWarnEnabled()) {
			warns.add(new SmsWarn());
		}
	}

	/**
	 * clearWarn
	 *
	 * @author shuigedeng
	 * @since 2021-09-10 16:19:01
	 */
	public void clearWarn() {
		warns.clear();
	}

	/**
	 * notifyRunning
	 *
	 * @author shuigedeng
	 * @since 2021-09-10 16:19:04
	 */
	private void notifyRunning() {
		Message msg = new Message();
		msg.setWarnType(WarnTypeEnum.WARN);

		List<Message> msgs = getAllMessage();
		int msgCount = atomicInteger.getAndSet(0);
		if (msgCount > 0) {
			StringBuilder content = new StringBuilder();
			content.append(String.format("最新报警累计:%s条,详情请查看日志系统,最后%s条报警内容如下:\n",
				msgCount,
				warnProperties.getCacheCount()));

			msgs.forEach(c -> {
				if (c.getWarnType().getLevel() > (msg.getWarnType()).getLevel()) {
					msg.setWarnType(c.getWarnType());
				}
				content.append(
					String.format("[%s][%s]内容%s\n",
						c.getWarnType().getDescription(),
						c.getTitle(),
						c.getContent()));
			});

			msg.setTitle(String.format("收到%s条报警", msgCount));
			msg.setContent(content.toString());
			notifyNow(msg);
		}
	}

	@Override
	public void notify(Message message) {
		addMessage(message);
	}

	/**
	 * notify
	 *
	 * @param warnType warnType
	 * @param title    title
	 * @param content  content
	 * @author shuigedeng
	 * @since 2021-09-10 16:19:41
	 */
	public void notify(String warnType, String title, String content) {
		Message message = new Message(WarnTypeEnum.valueOf(warnType), title, content);
		addMessage(message);
	}

	/**
	 * addMessage
	 *
	 * @param msg msg
	 * @author shuigedeng
	 * @since 2021-09-10 16:19:44
	 */
	private void addMessage(Message msg) {
		atomicInteger.getAndIncrement();

		//加锁
		synchronized (lock) {
			messages.add(msg);
			//清理多余
			if (messages.size() > this.warnProperties.getCacheCount()) {
				int cacheCount = this.warnProperties.getCacheCount();
				for (int i = 0; i < messages.size() - cacheCount; i++) {
					if (!messages.isEmpty()) {
						messages.removeFirst();
					}
				}
			}
		}
	}

	/**
	 * getAllMessage
	 *
	 * @return {@link java.util.List }
	 * @author shuigedeng
	 * @since 2021-09-10 16:20:17
	 */
	private List<Message> getAllMessage() {
		List<Message> msgs = new ArrayList<>(messages.size());
		synchronized (lock) {
			msgs.addAll(messages);
			messages.clear();
		}
		return msgs;
	}

	/**
	 * notifyNow
	 *
	 * @param message message
	 * @author shuigedeng
	 * @since 2021-09-10 16:20:33
	 */
	public void notifyNow(Message message) {
		notifyMessage0(message);
	}

	/**
	 * notifyNow
	 *
	 * @param warnType warnType
	 * @param title    title
	 * @param content  content
	 * @author shuigedeng
	 * @since 2021-09-10 16:20:38
	 */
	public void notifyNow(String warnType, String title, String content) {
		Message message = new Message(WarnTypeEnum.valueOf(warnType), title, content);
		notifyMessage0(message);
	}

	/**
	 * 方法私有化，避免重载方法循环调用
	 *
	 * @param message message
	 * @author shuigedeng
	 * @since 2021-09-10 16:20:48
	 */
	private void notifyMessage0(Message message) {
		if (!duplicateFilter.ifDuplicate(message.getContent()) && atomicChannel.get()) {
			if (WarnTypeEnum.ERROR == message.getWarnType()) {
				ExceptionUtils.reportException(message);
			}

			for (AbstractWarn warn : warns) {
				message.setTitle(String.format("[%s][%s][%s][%s]%s",
					new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
					RequestUtil.getIpAddress(),
					PropertyUtil.getProperty(CoreProperties.SpringApplicationName),
					PropertyUtil.getProperty(CoreProperties.SpringProfilesActive),
					StringUtil.nullToEmpty(message.getTitle())));
				warn.notify(message);
			}
		}
	}

	@Override
	public void close() {
		isClose = true;
	}

	@Override
	public void run(ApplicationArguments args) {
		atomicChannel.getAndSet(true);
		LogUtil.info(StarterNameConstant.HEALTH_STARTER, "开启消息通道");
	}

	/**
	 * 简单重复过滤算法 去除数值并hash
	 *
	 * @author shuigedeng
	 * @version 2021.9
	 * @since 2021-09-10 16:21:09
	 */
	private static class DuplicateFilter {

		private WarnProperties warnProperties;
		private int cacheMax = 100;
		private volatile List<Integer> cacheTag = new ArrayList<>(cacheMax + 5);
		private long lastClearTime = System.currentTimeMillis();

		public DuplicateFilter(WarnProperties warnProperties) {
			this.warnProperties = warnProperties;
		}

		/**
		 * ifDuplicate
		 *
		 * @param message message
		 * @return boolean
		 * @author shuigedeng
		 * @since 2021-09-10 16:21:52
		 */
		public boolean ifDuplicate(String message) {
			int hash = StringUtil.nullToEmpty(message)
				.replaceAll("\\d+", "")
				.hashCode();

			//超过1分钟清理
			if (System.currentTimeMillis() - lastClearTime > TimeUnit.MINUTES.toMillis(
				this.warnProperties.getDuplicateTimeSpan())) {
				cacheTag.clear();
				lastClearTime = System.currentTimeMillis();
			}

			//过长清理
			if (cacheTag.size() >= cacheMax) {
				cacheTag.clear();
			}

			if (!cacheTag.contains(hash)) {
				cacheTag.add(hash);
				return false;
			}

			return true;
		}
	}

}

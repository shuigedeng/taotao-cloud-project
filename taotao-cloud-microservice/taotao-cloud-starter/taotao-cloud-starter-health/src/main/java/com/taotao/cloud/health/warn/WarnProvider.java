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
import com.taotao.cloud.common.utils.ContextUtil;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.common.utils.StringUtil;
import com.taotao.cloud.core.monitor.MonitorThreadPool;
import com.taotao.cloud.core.properties.CoreProperties;
import com.taotao.cloud.core.utils.PropertyUtil;
import com.taotao.cloud.core.utils.RequestUtil;
import com.taotao.cloud.dingtalk.model.DingerRobot;
import com.taotao.cloud.health.model.EnumWarnType;
import com.taotao.cloud.health.model.Message;
import com.taotao.cloud.health.properties.WarnProperties;
import com.taotao.cloud.health.utils.ExceptionUtils;
import com.taotao.cloud.mail.template.MailTemplate;
import com.taotao.cloud.sms.service.SmsService;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
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
	private Object lock = new Object();
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

		this.monitorThreadPool.monitorSubmit("系统任务:WarnProvider 实时报警任务", () -> {
			while (!this.monitorThreadPool.monitorIsShutdown() && !isClose) {
				LogUtil.info(
					Thread.currentThread().getName() + " =========> 系统任务:WarnProvider 实时报警任务");

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

	public void registerWarn() {
		DingerRobot dingerRobot = ContextUtil.getBean(DingerRobot.class, true);
		if (this.warnProperties.isDingDingWarnEnabled() && Objects.nonNull(dingerRobot)) {
			warns.add(new DingdingWarn(this.warnProperties, dingerRobot));
		}

		MailTemplate mailTemplate = ContextUtil.getBean(MailTemplate.class, false);
		if (warnProperties.isEmailWarnEnabled() && Objects.nonNull(mailTemplate)) {
			warns.add(new MailWarn(this.warnProperties, mailTemplate));
		}

		SmsService smsService = ContextUtil.getBean(SmsService.class, false);
		if (warnProperties.isSmsWarnEnabled() && Objects.nonNull(smsService)) {
			warns.add(new SmsWarn(this.warnProperties, smsService));
		}
	}

	public void clearWarn() {
		warns.clear();
	}

	private void notifyRunning() {
		Message temp = new Message();
		List<Message> allmsgs = getAllmessage();
		int msgscount = atomicInteger.getAndSet(0);
		temp.setWarnType(EnumWarnType.WARN);

		if (msgscount > 0) {
			StringBuilder content = new StringBuilder();
			content.append(String.format("最新报警累计:%s条,详情请查看日志系统,最后%s条报警内容如下:\n", msgscount,
				this.warnProperties.getCacheCount()));
			allmsgs.forEach(c -> {
				if (c.getWarnType().getLevel() > (temp.getWarnType()).getLevel()) {
					temp.setWarnType(c.getWarnType());
				}
				content.append(
					String.format("[%s][%s]内容%s\n", c.getWarnType().getDescription(), c.getTitle(),
						c.getContent()));
			});
			temp.setTitle(String.format("收到%s条报警", msgscount));
			temp.setContent(content.toString());
			notifynow(temp);
		}
	}

	@Override
	public void notify(Message message) {
		addMessage(message);
	}

	/**
	 * 方法重载
	 */
	public void notify(String warnType, String title, String content) {
		Message message = new Message(EnumWarnType.valueOf(warnType), title, content);
		addMessage(message);
	}

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

	private List<Message> getAllmessage() {
		List<Message> msgs = new ArrayList<>();
		synchronized (lock) {
			messages.forEach(c -> {
				msgs.add(c);
			});
			messages.clear();
		}
		return msgs;
	}

	public void notifynow(Message message) {
		notifyMessage0(message);
	}

	/**
	 * 方法重载
	 */
	public void notifynow(String warnType, String title, String content) {
		Message message = new Message(EnumWarnType.valueOf(warnType), title, content);
		notifyMessage0(message);
	}

	/**
	 * 方法私有化，避免重载方法循环调用
	 */
	private void notifyMessage0(Message message) {
		if (!duplicateFilter.ifDuplicat(message.getContent()) && atomicChannel.get()) {
			if (message != null && EnumWarnType.ERROR == message.getWarnType()) {
				ExceptionUtils.reportException(message);
			}

			CoreProperties coreProperties = ContextUtil.getBean(CoreProperties.class, true);
			for (AbstractWarn warn : warns) {
				message.setTitle(String.format("[%s][%s][%s][%s]%s",
					new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
					RequestUtil.getIpAddress(),
					PropertyUtil.getPropertyCache(coreProperties.getEnv().getName(), ""),
					StringUtil.nullToEmpty(
						PropertyUtil.getPropertyCache(CoreProperties.SpringApplicationName, "")),
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
	 */
	private class DuplicateFilter {

		private WarnProperties warnProperties;
		private int cacheMax = 100;
		private volatile List<Integer> cacheTag = new ArrayList(cacheMax + 5);
		private long lastClearTime = System.currentTimeMillis();

		public DuplicateFilter(WarnProperties warnProperties) {
			this.warnProperties = warnProperties;
		}

		public boolean ifDuplicat(String message) {
			int hash = StringUtil.nullToEmpty(message).replaceAll("\\d+", "").hashCode();

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

package com.taotao.cloud.health.warn;

import com.taotao.cloud.common.constant.StarterNameConstant;
import com.taotao.cloud.common.utils.ContextUtil;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.common.utils.StringUtil;
import com.taotao.cloud.core.properties.CoreProperties;
import com.taotao.cloud.core.thread.ThreadPool;
import com.taotao.cloud.core.utils.PropertyUtil;
import com.taotao.cloud.core.utils.RequestUtil;
import com.taotao.cloud.health.model.EnumWarnType;
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

public class WarnProvider extends AbstractWarn implements AutoCloseable,
	ApplicationRunner {

	private ConcurrentLinkedDeque<Message> messages = new ConcurrentLinkedDeque<>();
	private AtomicInteger atomicInteger = new AtomicInteger(0);
	private Object lock = new Object();
	private List<AbstractWarn> warns = new ArrayList<>();
	private boolean isClose;
	private DuplicateFilter duplicateFilter = new DuplicateFilter();
	private AtomicBoolean atomicChannel = new AtomicBoolean(false);

	public WarnProvider() {
		isClose = false;

		RegisterWarn();

		ThreadPool.DEFAULT.submit("系统任务:WarnProvider 实时报警任务", () -> {
			while (!ThreadPool.DEFAULT.isShutdown() && !isClose) {
				try {
					notifyRunning();
				} catch (Exception exp) {
					LogUtil.warn(StarterNameConstant.HEALTH_STARTER, "WarnProvider 消息循环异常");
				}

				try {
					Thread.sleep(WarnProperties.Default().getTimeSpan() * 1000);
				} catch (Exception e) {
				}
			}
		});
	}

	public void RegisterWarn() {
		//if ("true".equals(
		//	PropertyUtil.getPropertyCache("bsf.message.dingding.enabled", "false"))) {
		//	warns.add(new DingdingWarn());
		//}
		//
		//if ("true".equals(PropertyUtil.getPropertyCache("bsf.message.flybook.enabled", "false"))) {
		//	warns.add(new FlyBookWarn());
		//}
	}

	public void ClearWarn() {
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
				WarnProperties.Default().getCacheCount()));
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
			if (messages.size() > WarnProperties.Default().getCacheCount()) {
				int cacheCount = WarnProperties.Default().getCacheCount();
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

		private int cacheMax = 100;
		private volatile List<Integer> cacheTag = new ArrayList(cacheMax + 5);
		private long lastClearTime = System.currentTimeMillis();

		public boolean ifDuplicat(String message) {
			int hash = StringUtil.nullToEmpty(message).replaceAll("\\d+", "").hashCode();
			/*超过1分钟清理*/
			if (System.currentTimeMillis() - lastClearTime > TimeUnit.MINUTES.toMillis(
				WarnProperties.Default().getDuplicateTimeSpan())) {
				cacheTag.clear();
				lastClearTime = System.currentTimeMillis();
			}
			/*过长清理*/
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

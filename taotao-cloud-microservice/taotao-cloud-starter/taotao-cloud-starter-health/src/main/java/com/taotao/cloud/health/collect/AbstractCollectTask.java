package com.taotao.cloud.health.collect;


import com.taotao.cloud.common.constant.StarterNameConstant;
import com.taotao.cloud.common.utils.ContextUtil;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.health.model.EnumWarnType;
import com.taotao.cloud.health.model.Message;
import com.taotao.cloud.health.model.Report;
import com.taotao.cloud.health.warn.WarnProvider;

/**
 * @author: chejiangyi
 * @version: 2019-07-23 18:47
 **/
public abstract class AbstractCollectTask implements AutoCloseable {

	protected int byteToMb = 1024 * 1024;
	/**
	 * 上次采集的信息
	 */
	private Object lastCollectInfo = null;
	/**
	 * 上次运行时间
	 */
	protected long lastRunTime = System.currentTimeMillis();

	/**
	 * 时间间隔:秒
	 */
	public abstract int getTimeSpan();

	/**
	 * 开关
	 */
	public abstract boolean getEnabled();

	/**
	 * 描述
	 */
	public abstract String getDesc();

	/**
	 * 唯一命名
	 */
	public abstract String getName();


	/**
	 * 报告
	 */
	public Report getReport() {
		if (getTimeSpan() > 0
			&& (System.currentTimeMillis() - lastRunTime) > getTimeSpan() * 1000) {
			lastRunTime = System.currentTimeMillis();
			lastCollectInfo = getData();
		}
		if (lastCollectInfo == null) {
			return null;
		}
		return new Report(lastCollectInfo);
	}

	public static void notifyMessage(EnumWarnType type, String subject, String content) {
		LogUtil.warn(StarterNameConstant.HEALTH_STARTER, "【报警】" + subject + "\r\n" + content, null);
		WarnProvider warnProvider = ContextUtil.getBean(WarnProvider.class, false);
		if (warnProvider != null) {
			Message message = new Message();
			message.setWarnType(type);
			message.setTitle(subject);
			message.setContent(content);
			if (type == EnumWarnType.ERROR) {
				warnProvider.notifynow(message);
			} else {
				warnProvider.notify(message);
			}
		}
	}

	/**
	 * @描述 增加自定义发送消息
	 * @参数 [message]
	 * @返回值 void
	 * @创建人 霍钧城
	 * @创建时间 2020/12/30
	 * @修改历史：
	 */
	public static void notifyMessage(Message message) {
		WarnProvider warnProvider = ContextUtil.getBean(WarnProvider.class, false);
		if (warnProvider != null) {
			if (message.getWarnType() == EnumWarnType.ERROR) {
				warnProvider.notifynow(message);
			} else {
				warnProvider.notify(message);
			}
		}
	}

	protected Object getData() {
		return null;
	}

	@Override
	public void close() throws Exception {

	}
}

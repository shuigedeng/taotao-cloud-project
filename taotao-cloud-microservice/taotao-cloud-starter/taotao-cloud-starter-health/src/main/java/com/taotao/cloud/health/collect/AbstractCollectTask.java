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
package com.taotao.cloud.health.collect;

import com.taotao.cloud.common.constant.StarterNameConstant;
import com.taotao.cloud.common.utils.ContextUtil;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.health.enums.WarnTypeEnum;
import com.taotao.cloud.health.model.CollectInfo;
import com.taotao.cloud.health.model.Message;
import com.taotao.cloud.health.model.Report;
import com.taotao.cloud.health.warn.WarnProvider;

/**
 * AbstractCollectTask
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-10 10:40:17
 */
public abstract class AbstractCollectTask implements AutoCloseable {

	protected long byteToMb = 1024 * 1024L;

	/**
	 * 上次采集的信息
	 */
	private CollectInfo lastCollectInfo = null;

	/**
	 * 上次运行时间
	 */
	protected long lastRunTime = System.currentTimeMillis();

	/**
	 * 时间间隔:秒
	 *
	 * @return int
	 * @author shuigedeng
	 * @since 2021-09-10 10:53:05
	 */
	public abstract int getTimeSpan();

	/**
	 * 开关
	 *
	 * @return boolean
	 * @author shuigedeng
	 * @since 2021-09-10 10:52:59
	 */
	public abstract boolean getEnabled();

	/**
	 * 描述
	 *
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-10 10:52:56
	 */
	public abstract String getDesc();

	/**
	 * 唯一命名
	 *
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-10 10:52:52
	 */
	public abstract String getName();

	/**
	 * 报告
	 *
	 * @return {@link com.taotao.cloud.health.model.Report }
	 * @author shuigedeng
	 * @since 2021-09-10 10:52:45
	 */
	public Report getReport() {
		long time = System.currentTimeMillis() - lastRunTime;
		if (getTimeSpan() > 0 && time > getTimeSpan() * 1000L) {
			lastRunTime = System.currentTimeMillis();
			lastCollectInfo = getData();
		}

		if (lastCollectInfo == null) {
			return null;
		}

		return new Report(lastCollectInfo);
	}

	/**
	 * notifyMessage
	 *
	 * @param type    type
	 * @param subject subject
	 * @param content content
	 * @author shuigedeng
	 * @since 2021-09-10 10:42:24
	 */
	public static void notifyMessage(WarnTypeEnum type, String subject, String content) {
		LogUtil.warn(StarterNameConstant.HEALTH_STARTER, "[warn]" + subject + "\r\n" + content,
			null);
		WarnProvider warnProvider = ContextUtil.getBean(WarnProvider.class, false);
		if (warnProvider != null) {
			Message message = new Message();
			message.setWarnType(type);
			message.setTitle(subject);
			message.setContent(content);
			if (type == WarnTypeEnum.ERROR) {
				warnProvider.notifyNow(message);
			} else {
				warnProvider.notify(message);
			}
		}
	}

	/**
	 * notifyMessage
	 *
	 * @param message message
	 * @author shuigedeng
	 * @since 2021-09-10 10:41:04
	 */
	public static void notifyMessage(Message message) {
		WarnProvider warnProvider = ContextUtil.getBean(WarnProvider.class, false);
		if (warnProvider != null) {
			if (message.getWarnType() == WarnTypeEnum.ERROR) {
				warnProvider.notifyNow(message);
			} else {
				warnProvider.notify(message);
			}
		}
	}

	/**
	 * getData
	 *
	 * @return {@link java.lang.Object }
	 * @author shuigedeng
	 * @since 2021-09-10 10:41:38
	 */
	protected CollectInfo getData() {
		return null;
	}

	@Override
	public void close() throws Exception {

	}
}

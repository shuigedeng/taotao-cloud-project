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
package com.taotao.cloud.health.collect;

import com.taotao.cloud.common.utils.context.ContextUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.health.enums.WarnTypeEnum;
import com.taotao.cloud.health.model.CollectInfo;
import com.taotao.cloud.health.model.Message;
import com.taotao.cloud.health.model.Report;
import com.taotao.cloud.health.warn.WarnProvider;

/**
 * AbstractCollectTask
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:27:14
 */
public abstract class AbstractCollectTask implements AutoCloseable {

	/**
	 * 字节,mb
	 */
	protected long byteToMb = 1024 * 1024L;

	/**
	 * 最后收集信息
	 * 上次采集的信息
	 */
	private CollectInfo lastCollectInfo = null;

	/**
	 * 持续运行时间
	 * 上次运行时间
	 */
	protected long lastRunTime = System.currentTimeMillis();

	/**
	 * 时间间隔:秒
	 *
	 * @return int
	 * @since 2022-04-27 17:27:14
	 */
	public abstract int getTimeSpan();

	/**
	 * 开关
	 *
	 * @return boolean
	 * @since 2022-04-27 17:27:14
	 */
	public abstract boolean getEnabled();

	/**
	 * 描述
	 *
	 * @return {@link String }
	 * @since 2022-04-27 17:27:14
	 */
	public abstract String getDesc();

	/**
	 * 唯一命名
	 *
	 * @return {@link String }
	 * @since 2022-04-27 17:27:14
	 */
	public abstract String getName();

	/**
	 * 报告
	 *
	 * @return {@link Report }
	 * @since 2022-04-27 17:27:14
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
	 * @since 2022-04-27 17:27:14
	 */
	public static void notifyMessage(WarnTypeEnum type, String subject, String content) {
		LogUtil.warn("[warn]" + subject + "\r\n" + content);
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
	 * @since 2022-04-27 17:27:14
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
	 * @return {@link CollectInfo }
	 * @since 2022-04-27 17:27:14
	 */
	protected CollectInfo getData() {
		return null;
	}

	/**
	 * 关闭
	 *
	 * @since 2022-04-27 17:27:14
	 */
	@Override
	public void close() throws Exception {

	}
}

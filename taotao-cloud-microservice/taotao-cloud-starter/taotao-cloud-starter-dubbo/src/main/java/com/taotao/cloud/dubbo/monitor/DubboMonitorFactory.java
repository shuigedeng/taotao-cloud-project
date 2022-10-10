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
package com.taotao.cloud.dubbo.monitor;

import com.taotao.cloud.common.utils.log.LogUtils;
import java.util.ArrayList;
import java.util.List;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.monitor.Monitor;
import org.apache.dubbo.monitor.support.AbstractMonitorFactory;

/**
 * 自定义监控工厂
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-08 10:19:23
 */
public class DubboMonitorFactory extends AbstractMonitorFactory {

	@Override
	protected Monitor createMonitor(URL url) {
		LogUtils.info("DubboMonitorFactory createMonitor activate ------------------------------");
		LogUtils.info(url.toFullString());
		return new DubboMonitor();
	}

	public static class DubboMonitor implements Monitor {

		@Override
		public URL getUrl() {
			return null;
		}

		@Override
		public boolean isAvailable() {
			return false;
		}

		@Override
		public void destroy() {

		}

		@Override
		public void collect(URL statistics) {
			LogUtils.info("DubboMonitor collect activate ------------------------------");
			LogUtils.info(statistics.toFullString());
		}

		@Override
		public List<URL> lookup(URL query) {
			LogUtils.info("DubboMonitor lookup activate ------------------------------");
			LogUtils.info(query.toFullString());

			return new ArrayList<>();
		}
	}
}

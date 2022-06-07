package com.taotao.cloud.dubbo.monitor;

import com.taotao.cloud.common.utils.log.LogUtil;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.config.spring.context.DubboBootstrapStartStopListenerSpringAdapter;
import org.apache.dubbo.monitor.Monitor;
import org.apache.dubbo.monitor.support.AbstractMonitorFactory;

import java.util.ArrayList;
import java.util.List;

public class CustomMonitorFactory extends AbstractMonitorFactory {
	@Override
	protected Monitor createMonitor(URL url) {
		LogUtil.info("CustomMonitorFactory getExecutor activate ------------------------------");
		LogUtil.info(url.toFullString());

		return new CustomMonitor();
	}


	public static class CustomMonitor implements Monitor {

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
			LogUtil.info("CustomMonitor collect activate ------------------------------");
			LogUtil.info(statistics.toFullString());
		}

		@Override
		public List<URL> lookup(URL query) {
			LogUtil.info("CustomMonitor lookup activate ------------------------------");
			LogUtil.info(query.toFullString());

			return new ArrayList<>();
		}
	}
}

package com.taotao.cloud.core.model;


import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.core.model.Callable.Action0;
import com.taotao.cloud.core.properties.CoreProperties;
import com.taotao.cloud.core.utils.PropertyUtil;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * @author: chejiangyi
 * @version: 2019-09-25 19:59 全局进程关闭事件定义
 **/
public class ProcessExitEvent {

	private static ArrayList<ExitCallback> callBackList = new ArrayList<>();
	private static final Object lock = new Object();

	/**
	 * @param action0
	 * @param order   越大越晚 必须大于0
	 */
	public static void register(Callable.Action0 action0, int order, Boolean asynch) {
		synchronized (lock) {
			callBackList.add(new ExitCallback(action0, Math.abs(order), asynch));
		}
	}

	static {
		//JVM 停止或重启时
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				synchronized (lock) {
					callBackList.sort(Comparator.comparingInt(c -> c.order));

					for (ExitCallback a : callBackList) {
						Callable.Action0 method = () -> {
							try {
								a.action0.invoke();
							} catch (Exception e2) {
								LogUtil.error(ProcessExitEvent.class, StarterName.CLOUD_STARTER,
									"进程关闭事件回调处理出错", e2);
							}
						};

						if (a.async) {
							new Thread(method::invoke).start();
						} else {
							method.invoke();
						}
					}
				}
				LogUtil.info(ProcessExitEvent.class, PropertyUtil.getProperty(CoreProperties.SpringApplicationName), "应用已正常退出！");
			} catch (Exception e) {
				LogUtil.error(ProcessExitEvent.class, PropertyUtil.getProperty(CoreProperties.SpringApplicationName), "进程关闭事件回调处理出错",
					e);
			}
		}));
	}

	private static class ExitCallback {

		Callable.Action0 action0;
		/**
		 * 顺序
		 */
		Integer order;
		/**
		 * 异步支持
		 */
		Boolean async;

		public ExitCallback() {
		}

		public ExitCallback(Action0 action0, Integer order, Boolean async) {
			this.action0 = action0;
			this.order = order;
			this.async = async;
		}

		public Action0 getAction0() {
			return action0;
		}

		public void setAction0(Action0 action0) {
			this.action0 = action0;
		}

		public Integer getOrder() {
			return order;
		}

		public void setOrder(Integer order) {
			this.order = order;
		}

		public Boolean getAsync() {
			return async;
		}

		public void setAsync(Boolean async) {
			this.async = async;
		}
	}
}

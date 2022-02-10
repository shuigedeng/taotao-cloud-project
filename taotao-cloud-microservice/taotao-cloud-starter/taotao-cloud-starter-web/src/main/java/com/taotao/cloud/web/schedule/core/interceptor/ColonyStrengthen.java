package com.taotao.cloud.web.schedule.core.interceptor;

import com.taotao.cloud.web.schedule.ScheduledException;
import com.taotao.cloud.web.schedule.common.annotation.ScheduledInterceptorOrder;
import com.taotao.cloud.web.schedule.model.ScheduledRunningContext;
import com.taotao.cloud.web.schedule.model.ZookeeperNodeData;
import com.taotao.cloud.zookeeper.template.ZookeeperTemplate;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

@ScheduledInterceptorOrder(Integer.MAX_VALUE)
public class ColonyStrengthen implements BaseStrengthen {

	protected final Log logger = LogFactory.getLog(getClass());
	@Autowired
	private ZookeeperTemplate zookeeperTemplate;
	@Autowired
	private ZookeeperNodeData zookeeperNodeData;

	/**
	 * 前置强化方法
	 *
	 * @param bean    bean实例（或者是被代理的bean）
	 * @param method  执行的方法对象
	 * @param args    方法参数
	 * @param context 任务线程运行时的上下文
	 */
	@Override
	public void before(Object bean, Method method, Object[] args, ScheduledRunningContext context) {
		boolean exists = zookeeperTemplate.exists(zookeeperNodeData.getZkParentNodePath());

		if (exists) {
			List<String> children = zookeeperTemplate.getChildren(
				zookeeperNodeData.getZkParentNodePath());
			if (children != null && children.size() > 0) {
				Collections.sort(children);
				if (zookeeperNodeData.getZkPath()
					.equals(zookeeperNodeData.getZkParentNodePath() + '/' + children.get(0))) {
					return;
				}
			} else {
				throw new ScheduledException("zookeeper连接出现异常");
			}
			context.setCallOff(true);
			context.setCallOffRemark("任务已交由其他服务运行");
		}
	}

	/**
	 * 后置强化方法
	 *
	 * @param bean    bean实例（或者是被代理的bean）
	 * @param method  执行的方法对象
	 * @param args    方法参数
	 * @param context 任务线程运行时的上下文
	 */
	@Override
	public void after(Object bean, Method method, Object[] args, ScheduledRunningContext context) {
	}

	/**
	 * 异常强化方法
	 *
	 * @param bean    bean实例（或者是被代理的bean）
	 * @param method  执行的方法对象
	 * @param args    方法参数
	 * @param context 任务线程运行时的上下文
	 */
	@Override
	public void exception(Object bean, Method method, Object[] args,
		ScheduledRunningContext context) {

	}

	/**
	 * Finally强化方法，出现异常也会执行
	 *
	 * @param bean    bean实例（或者是被代理的bean）
	 * @param method  执行的方法对象
	 * @param args    方法参数
	 * @param context 任务线程运行时的上下文
	 */
	@Override
	public void afterFinally(Object bean, Method method, Object[] args,
		ScheduledRunningContext context) {
	}

}


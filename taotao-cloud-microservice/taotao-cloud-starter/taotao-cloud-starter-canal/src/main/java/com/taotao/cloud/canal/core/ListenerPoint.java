package com.taotao.cloud.canal.core;


import com.taotao.cloud.canal.annotation.ListenPoint;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 监听 canal 操作
 *
 * @author 阿导
 * @CopyRight 萬物皆導
 * @created 2018/5/28 14:47
 * @Modified_By 阿导 2018/5/28 14:47
 */
public class ListenerPoint {

	/**
	 * 目标
	 */
	private Object target;

	/**
	 * 监听的方法和节点
	 */
	private Map<Method, ListenPoint> invokeMap = new HashMap<>();

	/**
	 * 构造方法，设置目标，方法以及注解类型
	 *
	 * @param target 目标
	 * @param method 方法
	 * @param anno   注解类型
	 * @return
	 * @author 阿导
	 * @time 2018/5/28 14:49
	 * @CopyRight 万物皆导
	 */
	ListenerPoint(Object target, Method method, ListenPoint anno) {
		this.target = target;
		this.invokeMap.put(method, anno);
	}

	/**
	 * 返回目标类
	 *
	 * @param
	 * @return
	 * @author 阿导
	 * @time 2018/5/28 14:50
	 * @CopyRight 万物皆导
	 */
	public Object getTarget() {
		return target;
	}

	/**
	 * 获取监听的操作方法和节点
	 *
	 * @param
	 * @return
	 * @author 阿导
	 * @time 2018/5/28 14:51
	 * @CopyRight 万物皆导
	 */
	public Map<Method, ListenPoint> getInvokeMap() {
		return invokeMap;
	}
}

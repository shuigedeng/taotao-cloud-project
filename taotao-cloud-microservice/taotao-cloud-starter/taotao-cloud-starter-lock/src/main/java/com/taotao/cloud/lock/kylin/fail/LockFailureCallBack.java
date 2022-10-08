package com.taotao.cloud.lock.kylin.fail;


import com.taotao.cloud.lock.kylin.exception.LockFailureException;
import java.lang.reflect.Method;

/**
 * 获取锁失败回调 接口
 *
 * @author wangjinkui
 */
public interface LockFailureCallBack {

	String DEFAULT_MESSAGE = "请求处理中，请稍后重试！";

	/**
	 * 锁失败事件
	 *
	 * @param method 业务方法
	 * @param args   方法参数
	 */
	default void callBack(Method method, Object[] args) {
		throw new LockFailureException(DEFAULT_MESSAGE);
	}

}

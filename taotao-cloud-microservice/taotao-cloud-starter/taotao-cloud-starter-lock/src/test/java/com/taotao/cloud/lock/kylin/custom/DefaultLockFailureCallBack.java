package com.taotao.cloud.lock.kylin.custom;

import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.lock.kylin.fail.LockFailureCallBack;
import java.lang.reflect.Method;
import org.springframework.stereotype.Component;

/**
 * 自定义默认加锁异常处理
 *
 * @author wangjinkui
 */
@Component("lockFailureCallBack")
public class DefaultLockFailureCallBack implements LockFailureCallBack {

	@Override
	public void callBack(Method method, Object[] args) {
		LogUtils.error("{}, method:{}, args:{}", DEFAULT_MESSAGE, method, args);
		// 此处可以抛出指定异常，配合全局异常拦截包装统一格式返回给调用端
		throw new BusinessException(DEFAULT_MESSAGE);
	}
}

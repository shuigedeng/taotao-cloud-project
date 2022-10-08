package com.taotao.cloud.lock.kylin.custom;

import com.taotao.cloud.common.utils.log.LogUtils;
import com.wjk.kylin.lock.fail.LockFailureCallBack;
import org.springframework.stereotype.Component;


/**
 * 自定义获取锁异常处理
 *
 * @author wangjinkui
 */
@Component
public class DemoLockFailureCallBack implements LockFailureCallBack {

	public void demoMethod12(String name) {
		LogUtils.error("demoMethod12-方法自定义失败回调,name:{}", name);
	}

	public void demoMethod13() {
		LogUtils.error("demoMethod13-方法自定义失败回调");
		throw new BusinessException("请求太快啦~");
	}

	public Integer demoMethod14(Integer num) {
		LogUtils.error("demoMethod14-方法自定义失败回调,入参num:{}", num);
		return -1;
	}
}

package com.taotao.cloud.dubbo.listener;

import com.taotao.cloud.common.utils.log.LogUtil;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.listener.InvokerListenerAdapter;

@Activate(group = {CommonConstants.PROVIDER, CommonConstants.CONSUMER}, order = 10003)
public class CustomInvokerListener extends InvokerListenerAdapter {
	@Override
	public void referred(Invoker<?> invoker) throws RpcException {
		LogUtil.info("CustomInvokerListener referred activate ------------------------------");
	}

	@Override
	public void destroyed(Invoker<?> invoker) {
		LogUtil.info("CustomInvokerListener destroyed activate ------------------------------");
	}
}

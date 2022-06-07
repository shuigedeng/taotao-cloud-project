package com.taotao.cloud.dubbo.listener;

import com.taotao.cloud.common.utils.log.LogUtil;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Exporter;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.listener.ExporterListenerAdapter;

@Activate(group = {CommonConstants.PROVIDER, CommonConstants.CONSUMER}, order = 10002)
public class CustomExporterListener extends ExporterListenerAdapter {
	@Override
	public void exported(Exporter<?> exporter) throws RpcException {
		LogUtil.info("CustomExporterListener exported activate ------------------------------");
	}

	@Override
	public void unexported(Exporter<?> exporter) {
		LogUtil.info("CustomExporterListener unexported activate ------------------------------");
	}
}

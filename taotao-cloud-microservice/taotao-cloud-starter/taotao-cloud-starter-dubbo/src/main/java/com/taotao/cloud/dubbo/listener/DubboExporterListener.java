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
package com.taotao.cloud.dubbo.listener;

import com.taotao.cloud.common.utils.log.LogUtils;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Exporter;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.listener.ExporterListenerAdapter;

/**
 * 自定义出口国侦听器
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-08 10:18:50
 */
@Activate(group = {CommonConstants.PROVIDER, CommonConstants.CONSUMER})
public class DubboExporterListener extends ExporterListenerAdapter {

	@Override
	public void exported(Exporter<?> exporter) throws RpcException {
		LogUtils.info("DubboExporterListener exported activate ------------------------------");
	}

	@Override
	public void unexported(Exporter<?> exporter) {
		LogUtils.info("DubboExporterListener unexported activate ------------------------------");
	}
}

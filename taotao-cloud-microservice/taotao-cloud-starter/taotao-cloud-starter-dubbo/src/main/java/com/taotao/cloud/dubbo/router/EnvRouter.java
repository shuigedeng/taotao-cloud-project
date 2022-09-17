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
package com.taotao.cloud.dubbo.router;

import java.util.List;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.cluster.router.AbstractRouter;

/**
 * env路由器
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-08 10:46:05
 */
public class EnvRouter extends AbstractRouter {

	public EnvRouter() {
		setPriority(Integer.MAX_VALUE);
	}

	@Override
	public <T> List<Invoker<T>> route(List<Invoker<T>> invokers, URL url, Invocation invocation)
		throws RpcException {
		//TODO 逻辑处理
		return invokers;
	}
}

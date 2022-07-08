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

import com.taotao.cloud.common.utils.log.LogUtil;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.cluster.Router;
import org.apache.dubbo.rpc.cluster.RouterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * env路由器工厂
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-08 10:46:08
 */
@Activate(order = 1)
public class EnvRouterFactory implements RouterFactory {
 
    @Override
    public Router getRouter(URL url) {
        LogUtil.info("启动dubbo特性环境路由");
        return new EnvRouter();
    }
}

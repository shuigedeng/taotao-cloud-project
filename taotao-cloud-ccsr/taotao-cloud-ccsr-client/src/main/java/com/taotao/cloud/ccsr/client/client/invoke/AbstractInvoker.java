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

package com.taotao.cloud.ccsr.client.client.invoke;

import com.taotao.cloud.ccsr.api.grpc.auto.Response;
import com.taotao.cloud.ccsr.client.client.AbstractClient;
import com.taotao.cloud.ccsr.client.context.CcsrContext;
import com.taotao.cloud.ccsr.client.lifecycle.Closeable;
import com.taotao.cloud.ccsr.client.option.RequestOption;
import com.taotao.cloud.ccsr.client.request.Payload;

/**
 * AbstractInvoker
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
public abstract class AbstractInvoker<R, OPTION extends RequestOption> implements Closeable {

    private final AbstractClient<?> client;

    public AbstractInvoker( AbstractClient<?> client ) {
        this.client = client;
    }

    public abstract Response invoke( CcsrContext context, Payload request );

    public abstract String protocol();

    public abstract R convert( CcsrContext context, OPTION option, Payload request );

    protected String getNamespace() {
        return this.client.getNamespace();
    }

    @SuppressWarnings("unchecked")
    protected OPTION getOption() {
        return (OPTION) this.client.getOption();
    }
}

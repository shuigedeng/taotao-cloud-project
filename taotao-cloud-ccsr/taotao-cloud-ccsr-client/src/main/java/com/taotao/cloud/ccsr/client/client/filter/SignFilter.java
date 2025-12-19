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

package com.taotao.cloud.ccsr.client.client.filter;

import com.taotao.cloud.ccsr.api.grpc.auto.Response;
import com.taotao.cloud.ccsr.client.client.AbstractClient;
import com.taotao.cloud.ccsr.client.context.CcsrContext;
import com.taotao.cloud.ccsr.client.option.RequestOption;
import com.taotao.cloud.ccsr.client.request.Payload;

/**
 * SignFilter
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
public class SignFilter<OPTION extends RequestOption> extends AbstractFilter<OPTION> {

    public SignFilter( AbstractClient<OPTION> client ) {
        super(client);
    }

    @Override
    protected Response doPreFilter( CcsrContext context, OPTION option, Payload request ) {
        // TODO 待后续实现客户端和服务端的签名校验
        return null;
    }

    @Override
    protected Response doPostFilter(
            CcsrContext context, OPTION option, Payload request, Response response ) {
        // TODO
        return response;
    }
}

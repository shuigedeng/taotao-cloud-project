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
import com.taotao.cloud.ccsr.api.result.ResponseHelper;
import com.taotao.cloud.ccsr.client.client.AbstractClient;
import com.taotao.cloud.ccsr.client.client.invoke.AbstractInvoker;
import com.taotao.cloud.ccsr.client.context.CcsrContext;
import com.taotao.cloud.ccsr.client.option.RequestOption;
import com.taotao.cloud.ccsr.client.request.Payload;
import com.taotao.cloud.ccsr.common.enums.ResponseCode;
import com.taotao.cloud.ccsr.common.exception.CcsrClientException;
import com.taotao.cloud.ccsr.common.log.Log;
import java.text.MessageFormat;
import java.util.concurrent.ConcurrentHashMap;

public class AbstractInvokerFilter<OPTION extends RequestOption> extends AbstractFilter<OPTION> {

    private final ConcurrentHashMap<String, AbstractInvoker<?, OPTION>> invokers =
            new ConcurrentHashMap<>();

    protected AbstractInvokerFilter(AbstractClient<OPTION> client) {
        super(client);
    }

    @Override
    protected Response doPreFilter(CcsrContext context, OPTION option, Payload request) {
        try {

            return getInvoker(option.protocol()).invoke(context, request);
        } catch (Exception ex) {
            String errMsg =
                    MessageFormat.format(
                            "[Client-Request] Invoke request fail, {0}", ex.getMessage());
            Log.error(errMsg, ex);
            return ResponseHelper.error(ResponseCode.SYSTEM_ERROR.getCode(), errMsg);
        }
    }

    @Override
    protected Response doPostFilter(
            CcsrContext context, OPTION option, Payload request, Response response) {
        return response;
    }

    protected AbstractInvoker<?, OPTION> getInvoker(String protocol) {
        AbstractInvoker<?, OPTION> invoker = this.invokers.get(protocol);
        if (invoker == null) {
            throw new CcsrClientException(
                    MessageFormat.format("Unidentified protocol {0}", protocol));
        }
        return invoker;
    }

    @SuppressWarnings("all")
    protected void registerInvoker(AbstractInvoker invoker) {
        this.invokers.put(invoker.protocol(), invoker);
    }
}

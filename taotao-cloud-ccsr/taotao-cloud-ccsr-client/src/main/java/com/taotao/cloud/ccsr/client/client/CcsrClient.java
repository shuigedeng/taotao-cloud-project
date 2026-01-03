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

package com.taotao.cloud.ccsr.client.client;

import com.taotao.cloud.ccsr.api.grpc.auto.Response;
import com.taotao.cloud.ccsr.client.client.filter.ConvertFilter;
import com.taotao.cloud.ccsr.client.client.filter.InvokerFilter;
import com.taotao.cloud.ccsr.client.client.filter.SignFilter;
import com.taotao.cloud.ccsr.client.client.filter.ValidationFilter;
import com.taotao.cloud.ccsr.client.dto.ServerAddress;
import com.taotao.cloud.ccsr.client.listener.ConfigListener;
import com.taotao.cloud.ccsr.client.option.GrpcOption;
import com.taotao.cloud.ccsr.client.option.RequestOption;
import com.taotao.cloud.ccsr.client.request.Payload;

import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.TimeUnit;

/**
 * CcsrClient
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
public class CcsrClient extends AbstractClient<RequestOption> {

    protected CcsrClient( String namespace ) {
        super(namespace);
    }

    @Override
    protected void buildChain() throws Exception {
        addNext(new ValidationFilter<>(this))
                .addNext(new SignFilter<>(this))
                .addNext(new ConvertFilter<>(this))
                .addNext(new InvokerFilter(this));
    }

    public static Builder builder( String namespace, RequestOption option ) {
        return new Builder(namespace, option);
    }

    public static class Builder extends AbstractBuilder<Builder, CcsrClient> {

        protected Builder( String namespace, RequestOption option ) {
            super(namespace, option);
        }

        @Override
        protected CcsrClient create( String namespace ) {
            return new CcsrClient(namespace);
        }
    }

    public static void main( String[] args ) throws InterruptedException {
        ServiceLoader.load(ConfigListener.class).forEach(ConfigListener::register);

        // 这个客户端全局只加载一次
        GrpcOption option = new GrpcOption();
        option.setServerAddresses(
                List.of(
                        new ServerAddress("127.0.0.1", 8000, true),
                        new ServerAddress("127.0.0.1", 8200, true),
                        new ServerAddress("127.0.0.1", 8100, true),
                        new ServerAddress("127.0.0.1", 8300, true)));
        CcsrClient mcsClient = CcsrClient.builder("default", option).build();

        Payload payload = Payload.builder().build();
        payload.setConfigData(new ServerAddress("127.0.0.3", 8000, true));
        payload.setNamespace("default");
        payload.setGroup("default_group");
        payload.setDataId("default_data_id");
        //        for (int i = 0; i < 10; i++) {
        Response response = mcsClient.request(payload);
        System.out.println(response);
        //        }

        mcsClient.destroy(15, TimeUnit.SECONDS);
    }
}

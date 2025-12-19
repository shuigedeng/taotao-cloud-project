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

package com.taotao.cloud.ccsr.client.starter;

import com.taotao.cloud.ccsr.api.event.EventType;
import com.taotao.cloud.ccsr.api.grpc.auto.Response;
import com.taotao.cloud.ccsr.client.client.CcsrClient;
import com.taotao.cloud.ccsr.client.listener.ConfigListener;
import com.taotao.cloud.ccsr.client.request.Payload;
import com.taotao.cloud.ccsr.common.log.Log;
import com.taotao.cloud.ccsr.spi.SpiExtensionFactory;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.DisposableBean;

/**
 * CcsrService
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
public class CcsrService implements DisposableBean {

    private final CcsrClient ccsrClient;

    public CcsrService( CcsrClient ccsrClient ) {
        SpiExtensionFactory.getExtensions(ConfigListener.class).forEach(ConfigListener::register);
        this.ccsrClient = ccsrClient;
    }

    public Response request( Payload request, EventType eventType ) {
        request.setEventType(eventType);
        return ccsrClient.request(request);
    }

    @Override
    public void destroy() {
        if (ccsrClient != null) {
            Log.print("销毁CcsrClient...");
            ccsrClient.destroy(3, TimeUnit.SECONDS);
        }
    }
}

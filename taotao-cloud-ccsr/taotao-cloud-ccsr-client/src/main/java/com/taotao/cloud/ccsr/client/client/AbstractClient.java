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
import com.taotao.cloud.ccsr.client.client.filter.Filter;
import com.taotao.cloud.ccsr.client.context.CcsrContext;
import com.taotao.cloud.ccsr.client.lifecycle.LeftCycle;
import com.taotao.cloud.ccsr.client.option.RequestOption;
import com.taotao.cloud.ccsr.client.request.Payload;
import com.taotao.cloud.ccsr.common.exception.DestroyException;
import com.taotao.cloud.ccsr.common.exception.InitializationException;
import com.taotao.cloud.ccsr.common.log.Log;
import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractClient<OPTION extends RequestOption> implements LeftCycle {

    private final AtomicBoolean started = new AtomicBoolean(false);

    private final Object initLock = new Object();

    private Filter<Response, OPTION> filterChain;

    private Filter<Response, OPTION> tailFilter;

    /**
     * namespace
     */
    private String namespace;

    /**
     * Client option.
     */
    private OPTION option;

    protected AbstractClient(String namespace) {
        this.namespace = namespace;
        this.filterChain = null;
        this.tailFilter = null;
        buildFilterChain();
    }

    public Response request(Payload request) {
        setOption(option);
        return doRequest(request, option);
    }

    @Override
    public void init() {
        Log.info("[Client-Main-Start] Initialize LinkClient...");

        if (this.started.get()) {
            Log.warn("[Client-Main-Start] LinkClient already started.");
            return;
        }

        synchronized (this.initLock) {
            if (this.started.get()) {
                Log.warn("[Client-Main-Start] LinkClient already started.");
                return;
            }

            try {
                doInit();
                this.filterChain.init();
            } catch (Exception ex) {
                String errLog =
                        MessageFormat.format(
                                "[Client-Main-Start] Initialize LinkClient fail, {0}",
                                ex.getMessage());
                Log.error(errLog, ex);
                throw new InitializationException(errLog, ex);
            }

            this.started.set(true);
        }

        Log.info("[Client-Main-Start] LinkClient started.");
    }

    @Override
    public void destroy(Integer timeout, TimeUnit unit) {
        Log.info("[Client-Main-Destroy] Destroy LinkClient...");

        if (!this.started.get()) {
            Log.warn("[Client-Main-Destroy] LinkClient already stopped.");
            return;
        }

        synchronized (this.initLock) {
            if (!this.started.get()) {
                Log.warn("[Client-Main-Destroy] LinkClient already stopped.");
                return;
            }

            try {
                doDestroy(timeout, unit);
                this.filterChain.destroy(timeout, unit);
            } catch (Exception ex) {
                String errLog =
                        MessageFormat.format(
                                "[Client-Main-Destroy] LinkClient destroy fail, {0}",
                                ex.getMessage());
                Log.error(errLog, ex);
                throw new DestroyException(errLog, ex);
            }

            this.started.set(false);
        }

        Log.info("[Client-Main-Destroy] LinkClient destroyed.");
    }

    public Response doRequest(Payload request, OPTION option) {
        if (request == null) {
            throw new IllegalArgumentException("Request is empty.");
        }

        if (option == null) {
            throw new IllegalArgumentException("Option is empty.");
        }

        Log.info("[Client-Execute-Begin] Execute sync request.");

        Response response = this.filterChain.preFilter(new CcsrContext(), option, request);

        Log.info("[Client-Execute-End] Finished sync request.");

        return response;
    }

    protected void doInit() throws Exception {}

    protected void doDestroy(Integer timeout, TimeUnit unit) throws Exception {}

    private void buildFilterChain() throws InitializationException {
        try {
            buildChain();
        } catch (Exception e) {
            throw new InitializationException(
                    MessageFormat.format(
                            "[Client-Main-Start] Create FilterChain fail, {0}", e.getMessage()),
                    e);
        }
    }

    protected AbstractClient<OPTION> addNext(Filter<Response, OPTION> next) {
        // thread-safe
        if (this.filterChain == null) {
            this.filterChain = next;
        } else {
            this.tailFilter.next(next);
            next.pre(this.tailFilter);
        }
        this.tailFilter = next;
        return this;
    }

    protected abstract void buildChain() throws Exception;

    public abstract static class AbstractBuilder<E, C extends AbstractClient<?>> {
        private String namespace;
        private RequestOption option;

        protected AbstractBuilder(String namespace, RequestOption option) {
            this.namespace = namespace;
            this.option = option;
        }

        @SuppressWarnings("unchecked")
        public E namespace(String namespace) {
            this.namespace = namespace;
            return (E) this;
        }

        @SuppressWarnings("unchecked")
        public E option(RequestOption option) {
            this.option = option;
            return (E) this;
        }

        public C build() {
            C client = create(namespace());
            client.setOption(option);
            client.init();
            return client;
        }

        protected abstract C create(String namespace);

        public String namespace() {
            return this.namespace;
        }
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public OPTION getOption() {
        return this.option;
    }

    @SuppressWarnings("unchecked")
    public void setOption(RequestOption option) {
        if (option == null) {
            throw new IllegalArgumentException("RequestOption can not be null.");
        }
        this.option = (OPTION) option;
    }
}

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

package com.taotao.cloud.ccsr.api.event; // GlobalEventBus.java

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.taotao.cloud.ccsr.api.listener.Listener;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;

public class GlobalEventBus {

    private static final EventBus INSTANCE =
            new AsyncEventBus(
                    Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2));

    private static final Set<Listener<?>> listeners = new HashSet<>();

    private GlobalEventBus() {}

    public static EventBus getInstance() {
        return INSTANCE;
    }

    public static void post(Event event) {
        INSTANCE.post(event);
    }

    public static void register(Listener<?> listener) {
        if (listeners.contains(listener)) {
            return;
        }
        listeners.add(listener);
        INSTANCE.register(listener);
    }

    public static void unregister(Listener<?> listener) {
        if (!listeners.contains(listener)) {
            return;
        }
        listeners.remove(listener);
        INSTANCE.unregister(listener);
    }
}

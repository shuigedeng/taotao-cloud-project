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

package com.taotao.cloud.sys.biz.event.application;

import com.taotao.cloud.common.utils.log.LogUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.ServletRequestHandledEvent;

@Component
public class ContextEventListener {
    /**
     *
     *
     * <pre class="code">
     * ContextRefreshedEvent
     * 在初始化或刷新ApplicationContext时发布（例如，通过使用ConfigurableApplicationContext接口上的refresh（）方法）。在这里，“已初始化”是指所有Bean都已加载，检测到并激活了后处理器Bean，已预先实例化单例并且可以使用ApplicationContext对象。只要尚未关闭上下文，只要选定的ApplicationContext实际上支持这种“热”刷新，就可以多次触发刷新。例如，XmlWebApplicationContext支持热刷新，但GenericApplicationContext不支持。
     * ContextStartedEvent
     * 使用ConfigurableApplicationContext接口上的start（）方法启动ApplicationContext时发布。此处，“已启动”表示所有Lifecycle bean都收到一个明确的启动信号。通常，此信号用于在显式停止后重新启动Bean，但也可以用于启动尚未配置为自动启动的组件（例如，尚未在初始化时启动的组件）。
     * ContextStoppedEvent
     * 通过使用ConfigurableApplicationContext接口上的stop（）方法停止ApplicationContext时发布。在这里，“已停止”表示所有Lifecycle bean都收到一个明确的停止信号。停止的上下文可以通过start（）调用重新启动。
     * ContextClosedEvent
     * 通过使用ConfigurableApplicationContext接口上的close（）方法关闭ApplicationContext时发布。在此，“封闭”表示所有单例豆都被破坏。封闭的情境到了生命的尽头。无法刷新或重新启动。
     * RequestHandledEvent
     * 一个特定于Web的事件，告诉所有Bean HTTP请求已得到服务。请求完成后，将发布此事件。此事件仅适用于使用Spring的DispatcherServlet的Web应用程序。
     * </pre>
     */
    @EventListener(ContextRefreshedEvent.class)
    public void contextRefreshedEventListener(ContextRefreshedEvent event) {
        LogUtils.info("ApplicationContextEventListener ----- ContextRefreshedEvent onApplicationEvent {}", event);
    }

    // @Async
    // @EventListener(ContextRefreshedEvent.class)
    // public void saveRequestLog(ContextRefreshedEvent event) {
    // }

    @Component
    public static class ContextStartedEventListener implements ApplicationListener<ContextStartedEvent> {
        @Override
        public void onApplicationEvent(ContextStartedEvent event) {
            LogUtils.info(
                    "ApplicationContextEventListener ----- ContextStartedEvent onApplicationEvent" + " {}", event);
        }
    }

    @Component
    public static class ContextStoppedEventListener implements ApplicationListener<ContextStoppedEvent> {
        @Override
        public void onApplicationEvent(ContextStoppedEvent event) {
            LogUtils.info(
                    "ApplicationContextEventListener ----- ContextStoppedEvent onApplicationEvent" + " {}", event);
        }
    }

    @Component
    public static class ContextClosedEventListener implements ApplicationListener<ContextClosedEvent> {
        @Override
        public void onApplicationEvent(ContextClosedEvent event) {
            LogUtils.info("ApplicationContextEventListener ----- ContextClosedEvent onApplicationEvent" + " {}", event);
        }
    }

    @Component
    public static class ServletRequestHandledEventListener implements ApplicationListener<ServletRequestHandledEvent> {
        @Override
        public void onApplicationEvent(ServletRequestHandledEvent event) {
            LogUtils.info(
                    "ApplicationContextEventListener ----- ServletRequestHandledEvent" + " onApplicationEvent {}",
                    event);
        }
    }
}

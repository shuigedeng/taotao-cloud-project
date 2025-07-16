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

package com.taotao.cloud.rpc.server.support.hook;

import com.taotao.cloud.rpc.common.common.support.hook.AbstractShutdownHook;
import com.taotao.cloud.rpc.common.common.support.invoke.InvokeManager;
import com.taotao.cloud.rpc.common.common.support.resource.ResourceManager;
import com.taotao.cloud.rpc.common.common.support.status.enums.StatusEnum;
import com.taotao.cloud.rpc.common.common.support.status.service.StatusManager;
import com.taotao.cloud.rpc.common.common.util.Waits;
import com.taotao.cloud.rpc.server.support.register.ServerRegisterManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务端默认的关闭钩子函数
 *
 * @since 0.1.8
 */
public class DefaultServerShutdownHook extends AbstractShutdownHook {

    /**
     * DefaultShutdownHook logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(DefaultServerShutdownHook.class);

    /**
     * 状态管理类
     *
     * @since 0.1.8
     */
    private StatusManager statusManager;

    /**
     * 调用管理类
     *
     * @since 0.1.8
     */
    private InvokeManager invokeManager;

    /**
     * 资源管理类
     *
     * @since 0.1.8
     */
    private ResourceManager resourceManager;

    /**
     * 注册中心服务端实现
     *
     * @since 0.1.8
     */
    private ServerRegisterManager serverRegisterManager;

    /**
     * 为剩余的请求等待时间
     *
     * @since 0.1.8
     */
    private long waitMillsForRemainRequest = 60 * 1000;

    /**
     * 新建对象
     *
     * @return this
     * @since 0.1.8
     */
    public static DefaultServerShutdownHook newInstance() {
        return new DefaultServerShutdownHook();
    }

    public StatusManager statusManager() {
        return statusManager;
    }

    public DefaultServerShutdownHook statusManager(StatusManager statusManager) {
        this.statusManager = statusManager;
        return this;
    }

    public InvokeManager invokeManager() {
        return invokeManager;
    }

    public DefaultServerShutdownHook invokeManager(InvokeManager invokeManager) {
        this.invokeManager = invokeManager;
        return this;
    }

    public ResourceManager resourceManager() {
        return resourceManager;
    }

    public DefaultServerShutdownHook resourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
        return this;
    }

    public ServerRegisterManager serverRegisterManager() {
        return serverRegisterManager;
    }

    public DefaultServerShutdownHook serverRegisterManager(
            ServerRegisterManager serverRegisterManager) {
        this.serverRegisterManager = serverRegisterManager;
        return this;
    }

    public DefaultServerShutdownHook waitMillsForRemainRequest(long waitMillsForRemainRequest) {
        this.waitMillsForRemainRequest = waitMillsForRemainRequest;
        return this;
    }

    /**
     * （1）设置 status 状态为等待关闭 （2）查看是否 {@link InvokeManager#remainsRequest()} 是否包含请求
     * （3）超时检测-可以不添加，如果难以关闭成功，直接强制关闭即可。 （4）关闭所有线程池资源信息 （5）设置状态为成功关闭
     */
    @Override
    protected void doHook() {
        // 设置状态为等待关闭
        statusManager.status(StatusEnum.WAIT_SHUTDOWN.code());
        LOG.info("[Server Shutdown] set status to wait for shutdown.");

        // 取消注册（通知注册中心）
        LOG.info(
                "[Server Shutdown] serverRegisterManager start destroy all link to registerCenter.");
        serverRegisterManager.unRegisterAll();
        // 清空相关的 channel 引用
        serverRegisterManager.clearRegisterChannel();
        LOG.info(
                "[Server Shutdown] serverRegisterManager finish destroy all link to registerCenter.");

        // 循环等待当前执行的请求执行完成
        long startMills = System.currentTimeMillis();
        while (invokeManager.remainsRequest()) {
            long currentMills = System.currentTimeMillis();
            long costMills = currentMills - startMills;
            if (costMills >= waitMillsForRemainRequest) {
                LOG.warn("[Server Shutdown] still remains request, but timeout, break.");
                break;
            }

            LOG.info("[Server Shutdown] still remains request, wait for a while.");
            Waits.waits(5);
        }

        // 销毁所有资源
        LOG.info("[Server Shutdown] resourceManager start destroy all resources.");
        this.resourceManager.destroyAll();
        LOG.info("[Server Shutdown] resourceManager finish destroy all resources.");

        // 设置状态为关闭成功
        statusManager.status(StatusEnum.SHUTDOWN_SUCCESS.code());
        LOG.info("[Server Shutdown] set status to shutdown success.");
    }
}

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

package com.taotao.cloud.rpc.core.net;

import com.taotao.cloud.rpc.common.annotation.Service;
import com.taotao.cloud.rpc.common.annotation.ServiceScan;
import com.taotao.cloud.rpc.common.exception.AnnotationMissingException;
import com.taotao.cloud.rpc.common.exception.RpcException;
import com.taotao.cloud.rpc.common.util.ReflectUtil;
import com.taotao.cloud.rpc.core.provider.ServiceProvider;
import com.taotao.cloud.rpc.core.registry.ServiceRegistry;

import java.lang.annotation.Annotation;
import java.net.InetSocketAddress;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

/**
 * AbstractRpcServer
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
@Slf4j
public abstract class AbstractRpcServer implements RpcServer {

    /**
     * 修饰符 protected 才可以将 字段 继承都 子类中 在 子类中 执行 赋值操作
     */
    protected String hostName;

    protected int port;
    protected ServiceProvider serviceProvider;
    protected ServiceRegistry serviceRegistry;

    /**
     * 启动服务后，扫描所有service类，并自动发布到注册中心
     */
    public void scanServices() throws RpcException {
        // 获取调用者 start 服务时所在的主类名, 即 调用者 调用 AbstractRpcServer 的子类 类名
        String mainClassName = ReflectUtil.getStackTrace();
        log.info("mainClassName: {}", mainClassName);
        Class<?> startClass;
        try {
            startClass = Class.forName(mainClassName);
            for (Annotation annotation : startClass.getAnnotations()) {
                log.info("discover annotation: {}", annotation);
            }
            if (!startClass.isAnnotationPresent(ServiceScan.class)) {
                log.error("The startup class is missing the @ServiceScan annotation");
                throw new AnnotationMissingException(
                        "The startup class is missing the @ServiceScan annotation Exception");
            }
        } catch (ClassNotFoundException e) {
            log.error("An unknown error has occurred:{}", e.getMessage());
            throw new RpcException("An unknown error has occurred Exception");
        }

        String basePackage = startClass.getAnnotation(ServiceScan.class).value();
        if ("".equals(basePackage)) {
            // 如果前缀有 包名
            if (mainClassName.lastIndexOf(".") != -1) {
                basePackage = mainClassName.substring(0, mainClassName.lastIndexOf("."));
                // 如果没有 包名
            } else {
                basePackage = mainClassName;
            }
        }

        Set<Class<?>> classSet = ReflectUtil.getClasses(basePackage);
        for (Class<?> clazz : classSet) {
            if (clazz.isAnnotationPresent(Service.class)) {
                String serviceName = clazz.getAnnotation(Service.class).name();
                String group = clazz.getAnnotation(Service.class).group();
                Object obj;
                String simpleName = clazz.getSimpleName();
                String firstLowCaseName =
                        simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1);
                try {
                    obj =
                            newInstance(
                                    clazz.getName(),
                                    clazz.getSimpleName(),
                                    firstLowCaseName,
                                    clazz);
                } catch (InstantiationException | IllegalAccessException e) {
                    log.error("An error occurred while creating the {} : {}", clazz, e);
                    continue;
                }

                if ("".equals(serviceName)) {
                    Class<?>[] interfaces = clazz.getInterfaces();
                    if ("".equals(group)) {
                        for (Class<?> oneInterface : interfaces) {
                            publishService(obj, oneInterface.getCanonicalName());
                        }
                    } else {
                        for (Class<?> oneInterface : interfaces) {
                            publishService(obj, group, oneInterface.getCanonicalName());
                        }
                    }
                } else {
                    if ("".equals(group)) {
                        publishService(obj, serviceName);
                    } else {
                        publishService(obj, group, serviceName);
                    }
                }
            }
        }
    }

    /**
     * 默认组名为“DEFAULT_GROUP”, 向注册中心发布服务
     *
     * @param service 服务
     * @param serviceName 服务名
     */
    @Override
    public <T> void publishService( T service, String serviceName ) throws RpcException {
        serviceProvider.addServiceProvider(service, serviceName);
        serviceRegistry.register(serviceName, new InetSocketAddress(hostName, port));
    }

    /**
     * 组名下向注册中心发布服务
     *
     * @param service 服务
     * @param groupName 组名
     * @param serviceName 服务名
     */
    @Override
    public <T> void publishService( T service, String groupName, String serviceName )
            throws RpcException {
        serviceProvider.addServiceProvider(service, serviceName);
        serviceRegistry.register(serviceName, groupName, new InetSocketAddress(hostName, port));
    }

    /**
     * 保留 类名，扩展 自定义 创建实例
     *
     * @param fullName 全类名
     * @param simpleName 忽略包类名
     * @param firstLowCaseName 首字母小写类名
     * @param clazz Class 类，可用于发射
     */
    @Override
    public Object newInstance(
            String fullName, String simpleName, String firstLowCaseName, Class<?> clazz )
            throws InstantiationException, IllegalAccessException {
        return clazz.newInstance();
    }
}

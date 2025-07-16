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

package com.taotao.cloud.rpc.registry.apiregistry.scan;

import com.taotao.cloud.rpc.registry.apiregistry.anno.ApiClient;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Set;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;

/**
 * 包扫描初始化注入bean
 */
@SuppressWarnings("unchecked")
public class ApiClientPackageScan {

    public static class ApiClientAnnotationPackageScan implements ImportBeanDefinitionRegistrar {

        @Override
        public void registerBeanDefinitions(
                AnnotationMetadata annotationMetadata,
                BeanDefinitionRegistry beanDefinitionRegistry) {
            //			String[] basePackages = ApiRegistryProperties.getRpcClientBasePackages();
            //			if (basePackages.length > 0) {
            //				// 自定义的 包扫描器
            //				ApiClientPackageScanHandle scanHandle = new ApiClientPackageScanHandle(
            //					beanDefinitionRegistry, false);
            //				// 扫描指定路径下的接口
            //				scanHandle.doScan(basePackages);
            //			}
        }
    }

    public static class ApiClientPackageScanHandle extends ClassPathBeanDefinitionScanner {

        public ApiClientPackageScanHandle(
                BeanDefinitionRegistry registry, boolean useDefaultFilters) {
            super(registry, useDefaultFilters);
            //			if (ApiRegistryProperties.getRpcClientTypeBeanNameEnabled()) {
            //				this.setBeanNameGenerator(new FullyQualifiedAnnotationBeanNameGenerator());
            //			}
        }

        @Override
        protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
            // 添加过滤条件
            addIncludeFilter(new AnnotationTypeFilter(ApiClient.class));
            // 兼容@FeignClient
            //			Class feignClientCls = ApiUtils.FeignClientAnnotationClass;
            //			if (feignClientCls != null) {
            //				addIncludeFilter(new AnnotationTypeFilter(feignClientCls));
            //			}
            Set<BeanDefinitionHolder> beanDefinitionHolders = super.doScan(basePackages);
            if (!beanDefinitionHolders.isEmpty()) {
                // 给扫描出来的接口添加上代理对象
                processBeanDefinitions(beanDefinitionHolders);
            }
            return beanDefinitionHolders;
        }

        /**
         * 给扫描出来的接口添加上代理对象
         *
         * @param beanDefinitions
         */
        private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
            GenericBeanDefinition definition;
            for (BeanDefinitionHolder holder : beanDefinitions) {
                definition = (GenericBeanDefinition) holder.getBeanDefinition();
                // 拿到接口的全路径名称
                String beanClassName = definition.getBeanClassName();

                // 设置属性 即所对应的消费接口
                try {
                    definition
                            .getPropertyValues()
                            .add("interfaceClass", Class.forName(beanClassName));
                    // 设置Calss 即代理工厂
                    definition.setBeanClass(MethodProxyFactory.class);
                    // 按 照查找Bean的Class的类型
                    definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);

                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        @Override
        protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
            return beanDefinition.getMetadata().isInterface()
                    && beanDefinition.getMetadata().isIndependent();
        }
    }

    public static class MethodProxyFactory<T> implements FactoryBean<T> {

        Class<T> interfaceClass; // 所对应的消费接口

        public Class<T> getInterfaceClass() {
            return interfaceClass;
        }

        public void setInterfaceClass(Class<T> interfaceClass) {
            this.interfaceClass = interfaceClass;
        }

        @Override
        public T getObject() {
            return (T) newInstance(interfaceClass); // 通过对应的消费接口返回代理类
        }

        @Override
        public Class<?> getObjectType() {
            return interfaceClass;
        }

        @Override
        public boolean isSingleton() {
            return true;
        }

        @SuppressWarnings("unchecked")
        private static <T> T newInstance(Class<T> methodInterface) {
            final MethodProxy<T> methodProxy = new MethodProxy<>(methodInterface);
            return (T)
                    Proxy.newProxyInstance(
                            Thread.currentThread().getContextClassLoader(),
                            new Class[] {methodInterface},
                            methodProxy);
        }
    }

    public static class MethodProxy<T> implements InvocationHandler {

        Class<T> interfaceClass;

        public MethodProxy(Class<T> interfaceClass) {
            this.interfaceClass = interfaceClass;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) {
            try {
                // 支持接口default默认实现
                //				if (ReflectUtils.isDefaultMethod(method)) {
                //					return ReflectUtils.invokeDefaultMethod(proxy, method, args);
                //				}
                //				if ("toString".equals(method.getName())) {
                //					return this.toString();
                //				}
                //				LogUtils.error(ApiRegistryProperties.Project,
                //					STR."未找到方法实现:\{getFullName(method)}");
                return null;
            } catch (Exception e) {
                //				LogUtils.error(ApiRegistryProperties.Project,
                //					STR."方法调用出错:\{getFullName(method)}");
                return null;
            }
        }

        private String getFullName(Method method) {
            return method.getDeclaringClass().getName() + method.getName();
        }
    }
}

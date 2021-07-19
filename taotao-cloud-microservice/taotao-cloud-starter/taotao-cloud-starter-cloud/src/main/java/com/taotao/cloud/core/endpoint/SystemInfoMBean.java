/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.core.endpoint;

/**
 * 注册一个 JMX MBean
 * <p>
 * 名字必须要用 MBean 结尾
 *
 * @author shuigedeng
 * @version v1.0
 * @since 2021/04/08 14:50
 */
public interface SystemInfoMBean {

	int getCpuCore();

	long getTotalMemory();

	void shutdown();

}

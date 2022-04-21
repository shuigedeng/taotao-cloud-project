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
package com.taotao.cloud.disruptor.handler;


import com.taotao.cloud.disruptor.event.DisruptorEvent;

/**
 * 给Handler设置路径
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-03 20:23:23
 */
public interface PathProcessor<T extends DisruptorEvent> {

	/**
	 * processPath
	 *
	 * @param path path
	 * @return {@link com.taotao.cloud.disruptor.handler.DisruptorHandler }
	 * @author shuigedeng
	 * @since 2021-09-03 20:23:26
	 */
	DisruptorHandler<T> processPath(String path);

}

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
package com.taotao.cloud.stream.framework.trigger.delay;

/**
 * DelayQueueMachine
 *
 * @author shuigedeng
 * @version v1.0
 * @since 2022/03/17 10:38
 */
public interface DelayQueueMachine {

	public boolean addJob(String jobId, Long triggerTime);

	/**
	 * 要实现延时队列的名字
	 *
	 * @return 延时队列的名字
	 */
	public String getDelayQueueName();
}

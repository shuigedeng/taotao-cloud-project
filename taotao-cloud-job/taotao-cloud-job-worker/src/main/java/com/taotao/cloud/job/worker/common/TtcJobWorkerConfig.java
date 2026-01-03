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

package com.taotao.cloud.job.worker.common;

import com.taotao.cloud.job.common.constant.RemoteConstant;
import com.taotao.cloud.job.worker.processor.ProcessResult;
import com.taotao.cloud.job.worker.processor.ProcessorLoader;
import com.taotao.cloud.job.worker.processor.factory.ProcessorFactory;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * TtcJobWorkerConfig
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
@Getter
@Setter
public class TtcJobWorkerConfig {

	/**
	 * AppName, recommend to use the name of this project Applications should be registered by powerjob-console in
	 * advance to prevent error.
	 */
	private String appName;

	/**
	 * Worker port Random port is enabled when port is set with non-positive number.
	 */
	private int port = RemoteConstant.DEFAULT_WORKER_GRPC_PORT;

	private int serverPort = RemoteConstant.DEFAULT_SERVER_GRPC_PORT;

	private String nameServerAddress;

	/**
	 * Address of powerjob-server node(s) Do not mistake for ActorSystem port. Do not add any prefix, i.e. http://.
	 */
	private List<String> serverAddress = new ArrayList<>();

	/**
	 * Max length of response result. Result that is longer than the value will be truncated. {@link ProcessResult} max
	 * length for #msg
	 */
	private int maxResultLength = 8096;

	//    /**
	//     * Processor factory for custom logic, generally used for IOC framework processor bean
	// injection that is not officially supported by PowerJob
	//     */
	private List<ProcessorFactory> processorFactoryList;
	/**
	 * Max numbers of LightTaskTacker
	 */
	private Integer maxLightweightTaskNum = 1024;

	/**
	 * Max numbers of HeavyTaskTacker
	 */
	private Integer maxHeavyweightTaskNum = 64;

	/**
	 * Interval(s) of worker health report
	 */
	private Integer healthReportInterval = 10;

	@Getter
	@Setter
	private static ProcessorLoader processorLoader;
}

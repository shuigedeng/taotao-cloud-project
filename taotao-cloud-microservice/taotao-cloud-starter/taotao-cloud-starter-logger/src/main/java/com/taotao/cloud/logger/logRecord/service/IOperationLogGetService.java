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
package com.taotao.cloud.logger.logRecord.service;


import com.taotao.cloud.logger.logRecord.bean.LogDTO;

/**
 * ioperation日志得到服务
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-26 14:42:31
 */
public interface IOperationLogGetService {

	/**
	 * 自定义日志监听
	 *
	 * @param logDTO 日志传输实体
	 * @since 2022-04-26 14:42:31
	 */
	void createLog(LogDTO logDTO);

}

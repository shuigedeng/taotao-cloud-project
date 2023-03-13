/*
 * COPYRIGHT (C) 2022 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.job.biz.quartz.core.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/12/7 21:28
 */
@Data
//public class JobLogPageDTO extends BasePageEntity implements Serializable {
public class JobLogPageDTO implements Serializable {

	private static final long serialVersionUID = -1L;

	private Long id;

	/**
	 * 任务名称
	 */
	private String jobName;

	/**
	 * 日志信息
	 */
	private String jobMessage;

	/**
	 * 执行状态（0正常 1失败）
	 */
	private String status;

	/**
	 * 异常信息
	 */
	private String exceptionInfo;

	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;

	public long getCurrent() {
		return 1;
	}

	public long getSize() {
		return 1;
	}
}

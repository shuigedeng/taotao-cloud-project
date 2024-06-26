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

package com.taotao.cloud.sys.api.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 计划工作签证官
 *
 * @author shuigedeng
 * @version 2023.04
 * @since 2023-05-09 15:08:02
 */
@Data
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "字典查询对象")
public class ScheduledJobVO implements Serializable {

	@Serial
	private static final long serialVersionUID = -4132785717179910025L;

	@Schema(description = "任务名")
	private String taskId;

	@Schema(description = "任务名")
	private String time;

	// 执行状态（0正常 1失败）
	@Schema(description = "任务名")
	private Integer status;

	@Schema(description = "任务名")
	private String exceptionInfo;

	private List<String> next;

	private LocalDateTime createTime;
	private Long createBy;
	private LocalDateTime updateTime;
	private Long updateBy;
	private Integer version;
	private Boolean delFlag;
}

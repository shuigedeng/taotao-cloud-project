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
import java.util.Date;
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
public class ScheduledJobLogVO implements Serializable {

	@Serial
	private static final long serialVersionUID = -4132785717179910025L;

	// 任务名
	@Schema(description = "任务名")
	private String name;

	/*
	目标字符串
	格式bean.method(params)
	String字符串类型，包含'、boolean布尔类型，等于true或者false
	long长整形，包含L、double浮点类型，包含D、其他类型归类为整形
	aa.aa('String',100L,20.20D)
	*/
	@Schema(description = "目标字符串")
	private String invokeTarget;

	// 周期(month、week、day、hour、minute、secods)
	@Schema(description = "周期(month、week、day、hour、minute、secods)")
	private String cycle;

	// cron表达式
	@Schema(description = "cron表达式")
	private String cronExpression;

	// 执行策略(1手动，2-自动）
	@Schema(description = "执行策略(1手动，2-自动）")
	private Integer policy;

	// 状态（0正常 1暂停）
	@Schema(description = "状态（0正常 1暂停）")
	private Integer status;

	// 执行情况(1-执行中,2-已暂停)
	@Schema(description = "执行情况(1-执行中,2-已暂停)")
	private Integer situation;

	// 上次执行时间
	@Schema(description = "上次执行时间")
	private Date lastRunTime;

	// 下次执行时间
	@Schema(description = "下次执行时间")
	private Date nextRunTime;

	// 备注
	@Schema(description = "remark")
	private String remark;

	private List<String> next;

	private LocalDateTime createTime;
	private Long createBy;
	private LocalDateTime updateTime;
	private Long updateBy;
	private Integer version;
	private Boolean delFlag;
}

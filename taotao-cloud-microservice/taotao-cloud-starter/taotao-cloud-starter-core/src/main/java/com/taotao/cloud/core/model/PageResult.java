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
package com.taotao.cloud.core.model;

import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.enums.ResultEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import org.slf4j.MDC;
import org.springframework.data.domain.Page;

/**
 * 返回分页实体类
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/4/29 15:40
 */
@Data
@ApiModel(value = "PageResult", description = "返回结果通用对象")
public class PageResult<T> implements Serializable {

	private static final long serialVersionUID = -275582248840137389L;

	@ApiModelProperty(value = "总数量")
	private long total;
	@ApiModelProperty(value = "页面数量")
	private long pageSize;
	@ApiModelProperty(value = "当前页")
	private long currentPage;
	@ApiModelProperty(value = "状态码")
	private int code;
	@ApiModelProperty(value = "消息体")
	private String message;
	@ApiModelProperty(value = "返回数据")
	private List<T> data;
	@ApiModelProperty(value = "消息类型 success")
	private String type;
	@ApiModelProperty(value = "请求id")
	private String requestId;
	@ApiModelProperty(value = "请求结束时间")
	private LocalDateTime timestamp;

	public PageResult() {
	}

	public PageResult(int code, String message, long total, long pageSize, long currentPage,
		List<T> data, String requestId, LocalDateTime timestamp, String type) {
		this.code = code;
		this.message = message;
		this.total = total;
		this.pageSize = pageSize;
		this.currentPage = currentPage;
		this.data = data;
		this.requestId = requestId;
		this.timestamp = timestamp;
		this.type = type;
	}

	public static <T> PageResult<T> succeed(Page<T> page) {
		return of(ResultEnum.SUCCESS.getCode(),
			ResultEnum.SUCCESS.getMessage(),
			page.getTotalElements(),
			page.getSize(),
			page.getNumber(),
			page.getContent()
		);
	}

	public static <T> PageResult<T> succeed(Page<T> page, ResultEnum resultEnum) {
		return of(resultEnum.getCode(),
			resultEnum.getMessage(),
			page.getTotalElements(),
			page.getSize(),
			page.getNumber(),
			page.getContent());
	}

	public static <T> PageResult<T> succeed(long total,
		long pageSize,
		long currentPage,
		List<T> data) {
		return of(ResultEnum.SUCCESS.getCode(),
			ResultEnum.SUCCESS.getMessage(),
			total,
			pageSize,
			currentPage,
			data);
	}

	public static <T> PageResult<T> of(Integer code,
		String msg,
		long total,
		long pageSize,
		long currentPage,
		List<T> data) {
		return new PageResult<T>(
			code,
			msg,
			total,
			pageSize,
			currentPage,
			data,
			CommonConstant.SUCCESS,
			LocalDateTime.now(),
			MDC.get(CommonConstant.TRACE_ID)
		);

	}

}

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
package com.taotao.cloud.web.base.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.log.annotation.RequestOperateLog;
import com.taotao.cloud.web.base.dto.BatchDTO;
import com.taotao.cloud.web.base.dto.BatchDTO.BatchUpdate;
import com.taotao.cloud.web.base.entity.SuperEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * DeleteController
 *
 * @param <T>         实体
 * @param <I>         id
 * @param <SaveDTO>   添加参数
 * @param <UpdateDTO> 更新参数
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 21:05:45
 */
public interface BatchController<T extends SuperEntity<T, I>, I extends Serializable, SaveDTO, UpdateDTO> extends
	BaseController<T, I> {

	/**
	 * 通用批量操作
	 *
	 * @param batchDTO batchDTO
	 * @return {@link Result&lt;java.lang.Boolean&gt; }
	 * @author shuigedeng
	 * @since 2021-10-11 15:32:22
	 */
	@Operation(summary = "通用批量操作", description = "通用批量操作")
	@PostMapping("/batch")
	@RequestOperateLog(description = "通用批量操作")
	//@PreAuthorize("hasPermission(#batchDTO, 'batch')")
	@PreAuthorize("@permissionVerifier.hasPermission('batch')")
	default Result<Boolean> batch(
		@Parameter(description = "通用批量操作", required = true)
		@RequestBody @Validated BatchDTO<SaveDTO, UpdateDTO, I> batchDTO) {
		String method = batchDTO.getMethod();
		return switch (method) {
			case "create" -> success(batchCreate(batchDTO.getBatchCreate()));
			case "update" -> success(batchUpdate(batchDTO.getBatchUpdate()));
			case "delete" -> success(batchDelete(batchDTO.getBatchDelete()));
			default -> throw new BusinessException("操作方式错误");
		};
	}

	/**
	 * batchCreate
	 *
	 * @param saveDTOList saveDTOList
	 * @return {@link Boolean }
	 * @author shuigedeng
	 * @since 2021-10-15 16:37:10
	 */
	default Boolean batchCreate(List<SaveDTO> saveDTOList) {
		if (saveDTOList.isEmpty()) {
			throw new BusinessException("添加数据不能为空");
		}
		List<T> entityList = saveDTOList.stream()
			.filter(saveDTO -> checkField(saveDTO.getClass()))
			.map(saveDTO -> BeanUtil.toBean(saveDTO, getEntityClass()))
			.collect(Collectors.toList());

		return service().saveBatch(entityList);
	}

	/**
	 * batchUpdate
	 *
	 * @param updateDTOList updateDTOList
	 * @return {@link Boolean }
	 * @author shuigedeng
	 * @since 2021-10-15 16:37:15
	 */
	default Boolean batchUpdate(List<BatchUpdate<UpdateDTO, I>> updateDTOList) {
		if (updateDTOList.isEmpty()) {
			throw new BusinessException("更新数据不能为空");
		}
		Map<I, UpdateDTO> updateDTOMap = new HashMap<>();
		updateDTOList.forEach(updateDTO -> {
			I id = updateDTO.getId();
			UpdateDTO updateDTO1 = updateDTO.getUpdateDTO();
			updateDTOMap.put(id, updateDTO1);
		});

		List<I> ids = updateDTOList.stream().map(BatchUpdate::getId).collect(Collectors.toList());
		List<T> ts = service().listByIds(ids);
		if (ts.isEmpty()) {
			throw new BusinessException("未查询到数据");
		}

		List<T> entityList = ts.stream()
			.filter(updateDTO -> checkField(updateDTO.getClass()))
			.peek(t -> {
				UpdateDTO updateDTO = updateDTOMap.get(t.getId());
				BeanUtil.copyProperties(updateDTO, t, CopyOptions.create().ignoreNullValue());
			}).collect(Collectors.toList());

		return service().updateBatchById(entityList);
	}

	/**
	 * batchDelete
	 *
	 * @param batchDelete batchDelete
	 * @return {@link Boolean }
	 * @author shuigedeng
	 * @since 2021-10-15 16:37:21
	 */
	default Boolean batchDelete(List<I> batchDelete) {
		if (batchDelete.isEmpty()) {
			throw new BusinessException("删除数据不能为空");
		}
		return service().removeByIds(batchDelete);
	}

}

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
package com.taotao.cloud.web.base.controller;

import cn.hutool.core.util.ReflectUtil;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.reflect.ReflectionUtil;
import com.taotao.cloud.logger.annotation.RequestLogger;
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
	 * @param batchDTO 批量操作对象
	 * @return 批量操作结果
	 * @since 2021-10-11 15:32:22
	 */
	@Operation(summary = "通用批量操作", description = "通用批量操作")
	@PostMapping("/batch")
	@RequestLogger("通用批量操作")
	//@PreAuthorize("hasPermission(#batchDTO, 'batch')")
	@PreAuthorize("@pms.hasPermission('batch')")
	default Result<Boolean> batch(
		@Parameter(description = "通用批量操作", required = true)
		@RequestBody @Validated BatchDTO<SaveDTO, UpdateDTO, I> batchDTO) {
		String method = batchDTO.method();
		return switch (method) {
			case "create" -> success(batchCreate(batchDTO.batchCreate()));
			case "update" -> success(batchUpdate(batchDTO.batchUpdate()));
			case "delete" -> success(batchDelete(batchDTO.batchDelete()));
			default -> throw new BusinessException("操作方式错误");
		};
	}

	/**
	 * 批量新增
	 *
	 * @param saveDTOList 批量新增对象
	 * @return 批量新增结果
	 * @since 2021-10-15 16:37:10
	 */
	default Boolean batchCreate(List<SaveDTO> saveDTOList) {
		if (saveDTOList.isEmpty()) {
			throw new BusinessException("添加数据不能为空");
		}
		List<T> entityList = saveDTOList.stream()
			.filter(saveDTO -> ReflectionUtil.checkField(saveDTO.getClass(), getEntityClass()))
			.map(saveDTO -> {
				T t = ReflectUtil.newInstanceIfPossible(getEntityClass());
				return ReflectionUtil.copyPropertiesIfRecord(t, saveDTO);
			})
			.toList();

		return service().saveBatch(entityList);
	}

	/**
	 * 批量更新
	 *
	 * @param updateDTOList 批量更新对象
	 * @return 批量更新结果
	 * @since 2021-10-15 16:37:15
	 */
	default Boolean batchUpdate(List<BatchUpdate<UpdateDTO, I>> updateDTOList) {
		if (updateDTOList.isEmpty()) {
			throw new BusinessException("更新数据不能为空");
		}
		Map<I, UpdateDTO> updateDTOMap = new HashMap<>();
		updateDTOList.forEach(updateDTO -> {
			I id = updateDTO.id();
			UpdateDTO updateDTO1 = updateDTO.updateDTO();
			updateDTOMap.put(id, updateDTO1);
		});

		List<I> ids = updateDTOList.stream().map(BatchUpdate::id).collect(Collectors.toList());
		List<T> ts = service().listByIds(ids);
		if (ts.isEmpty()) {
			throw new BusinessException("未查询到数据");
		}

		List<T> entityList = ts.stream()
			.filter(updateDTO -> ReflectionUtil.checkField(updateDTO.getClass(), getEntityClass()))
			.map(t -> {
				UpdateDTO updateDTO = updateDTOMap.get(t.getId());
				return ReflectionUtil.copyPropertiesIfRecord(t, updateDTO);
			}).toList();

		return service().updateBatchById(entityList);
	}

	/**
	 * 批量删除
	 *
	 * @param batchDelete 批量删除对象
	 * @return 批量删除结果
	 * @since 2021-10-15 16:37:21
	 */
	default Boolean batchDelete(List<I> batchDelete) {
		if (batchDelete.isEmpty()) {
			throw new BusinessException("删除数据不能为空");
		}
		return service().removeByIds(batchDelete);
	}

}

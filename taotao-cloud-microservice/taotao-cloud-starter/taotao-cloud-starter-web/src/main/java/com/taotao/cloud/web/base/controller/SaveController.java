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
import com.taotao.cloud.web.base.entity.SuperEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.io.Serializable;
import java.util.Objects;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * SaveController
 *
 * @param <T>       实体
 * @param <I>       id
 * @param <SaveDTO> 添加参数
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 21:12:22
 */
public interface SaveController<T extends SuperEntity<T, I>, I extends Serializable, SaveDTO> extends
	BaseController<T, I> {

	/**
	 * 通用单体新增
	 *
	 * @param saveDTO 保存对象
	 * @return 报错结果
	 * @since 2021-09-02 21:12:44
	 */
	@Operation(summary = "通用单体新增", description = "通用单体新增")
	@PostMapping
	@RequestLogger("通用单体新增")
	//@PreAuthorize("@pms.hasPermission('save')")
	default Result<Boolean> save(
		@Parameter(description = "新增DTO", required = true)
		@RequestBody @Validated SaveDTO saveDTO) {
		if (handlerSave(saveDTO)) {
			if (ReflectionUtil.checkField(saveDTO.getClass(), getEntityClass())) {
				T t = ReflectUtil.newInstanceIfPossible(getEntityClass());
				return success(service().save(ReflectionUtil.copyPropertiesIfRecord(t, saveDTO)));
			}
		}
		throw new BusinessException("通用单体新增失败");
	}

	/**
	 * 自定义新增
	 *
	 * @param model 新增对象
	 * @return 新增结果
	 * @since 2021-10-11 17:06:06
	 */
	default Boolean handlerSave(SaveDTO model) {
		if (Objects.isNull(model)) {
			throw new BusinessException("新增DTO不能为空");
		}
		return true;
	}

}

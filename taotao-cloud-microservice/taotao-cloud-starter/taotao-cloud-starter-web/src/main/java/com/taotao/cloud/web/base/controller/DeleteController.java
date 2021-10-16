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

import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.data.mybatis.plus.conditions.Wraps;
import com.taotao.cloud.data.mybatis.plus.conditions.query.QueryWrap;
import com.taotao.cloud.log.annotation.RequestOperateLog;
import com.taotao.cloud.web.base.entity.SuperEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * DeleteController
 *
 * @param <T> 实体
 * @param <I> id
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 21:05:45
 */
public interface DeleteController<T extends SuperEntity<I>, I extends Serializable> extends
	BaseController<T, I> {

	/**
	 * 通用单体id删除
	 *
	 * @param id id
	 * @return {@link com.taotao.cloud.common.model.Result }
	 * @author shuigedeng
	 * @since 2021-09-02 21:06:18
	 */
	@Operation(summary = "通用单体id删除", description = "通用单体id删除", method = CommonConstant.DELETE)
	@DeleteMapping("/{id:[0-9]*}")
	@RequestOperateLog(description = "通用单体id删除")
	//@PreAuthorize("@permissionVerifier.hasPermission('delete')")
	default Result<Boolean> deleteById(
		@Parameter(description = "id", required = true) @NotNull(message = "id不能为空")
		@PathVariable(value = "id") I id) {
		if (handlerDeleteById(id)) {
			return success(service().removeById(id));
		}

		throw new BusinessException(ResultEnum.ERROR);
	}

	/**
	 * 删除
	 *
	 * @param id id
	 * @return {@link com.taotao.cloud.common.model.Result } 返回SUCCESS_RESPONSE, 调用默认更新, 返回其他不调用默认更新
	 * @author shuigedeng
	 * @since 2021-09-02 21:06:27
	 */
	default Boolean handlerDeleteById(I id) {
		if (Objects.isNull(id)) {
			throw new BusinessException("id不能为空");
		}
		return true;
	}


	/**
	 * 通用单体字段删除
	 *
	 * @param filedName  字段名称
	 * @param filedValue 字段值
	 * @return {@link Result&lt;java.lang.Boolean&gt; }
	 * @author shuigedeng
	 * @since 2021-10-11 15:04:58
	 */
	@Operation(summary = "通用单体字段删除", description = "通用单体字段删除", method = CommonConstant.DELETE)
	@DeleteMapping("/{filedName}/{filedValue}")
	@RequestOperateLog(description = "通用单体字段删除")
	//@PreAuthorize("@permissionVerifier.hasPermission('delete')")
	default Result<Boolean> deleteByFiled(
		@Parameter(description = "字段名称", required = true) @NotEmpty(message = "字段名称不能为空")
		@PathVariable(value = "filedName") String filedName,
		@Parameter(description = "字段值", required = true) @NotNull(message = "字段值不能为空")
		@PathVariable(value = "filedValue") Object filedValue) {
		if (handlerDeleteByFiled(filedName, filedValue)) {
			if (checkField(filedName)) {
				QueryWrap<T> wrapper = Wraps.q();
				wrapper.eq(StrUtil.toUnderlineCase(filedName), filedValue);
				return success(service().remove(wrapper));
			}
		}

		throw new BusinessException(ResultEnum.ERROR);
	}

	/**
	 * 通用单体字段删除
	 *
	 * @param filedName  字段名称
	 * @param filedValue 字段值
	 * @return {@link Boolean }
	 * @author shuigedeng
	 * @since 2021-10-11 15:03:39
	 */
	default Boolean handlerDeleteByFiled(String filedName, Object filedValue) {
		if (Objects.isNull(filedName) || Objects.isNull(filedValue)) {
			throw new BusinessException("字段数据不能为空");
		}
		return true;
	}
}

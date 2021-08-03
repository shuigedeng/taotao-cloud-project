package com.taotao.cloud.web.base.controller;

import cn.hutool.core.bean.BeanUtil;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.data.mybatis.plus.entity.SuperEntity;
import com.taotao.cloud.log.annotation.RequestOperateLog;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 修改Controller
 *
 * @param <Entity>    实体
 * @param <UpdateDTO> 修改参数
 * @author zuihou
 * @date 2020年03月07日22:30:37
 */
public interface UpdateController<Entity, UpdateDTO> extends BaseController<Entity> {

	/**
	 * 修改
	 *
	 * @param updateDTO 修改DTO
	 * @return 修改后的实体数据
	 */
	@Operation(summary = "修改", description = "修改UpdateDTO中不为空的字段", method = CommonConstant.PUT, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@PutMapping
	@RequestOperateLog(value = "'修改:' + #updateDTO?.id", request = false)
	@PreAuthorize("hasAnyPermission('{}edit')")
	default Result<Entity> update(
		@RequestBody @Validated(SuperEntity.Update.class) UpdateDTO updateDTO) {
		Result<Entity> result = handlerUpdate(updateDTO);
		if (result.getData() != null) {
			Entity model = BeanUtil.toBean(updateDTO, getEntityClass());
			getBaseService().updateById(model);
			result.setData(model);
		}
		return result;
	}

	/**
	 * 修改所有字段
	 *
	 * @param entity 实体
	 */
	@Operation(summary = "修改所有字段", description = "修改所有字段，没有传递的字段会被置空", method = CommonConstant.PUT, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@PutMapping("/all")
	@RequestOperateLog(value = "'修改所有字段:' + #entity?.id", request = false)
	@PreAuthorize("hasAnyPermission('{}edit')")
	default Result<Entity> updateAll(
		@RequestBody @Validated(SuperEntity.Update.class) Entity entity) {
		getBaseService().updateAllById(entity);
		return Result.success(entity);
	}

	/**
	 * 自定义更新
	 *
	 * @param model 修改DTO
	 * @return 返回SUCCESS_RESPONSE, 调用默认更新, 返回其他不调用默认更新
	 */
	default Result<Entity> handlerUpdate(UpdateDTO model) {
		return Result.success();
	}
}

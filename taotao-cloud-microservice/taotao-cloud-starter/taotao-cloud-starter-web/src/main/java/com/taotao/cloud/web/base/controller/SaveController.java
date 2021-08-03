package com.taotao.cloud.web.base.controller;

import cn.hutool.core.bean.BeanUtil;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.log.annotation.RequestOperateLog;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 新增
 *
 * @param <Entity>  实体
 * @param <SaveDTO> 保存参数
 * @author zuihou
 * @date 2020年03月07日22:07:31
 */
public interface SaveController<Entity, SaveDTO> extends BaseController<Entity> {

	/**
	 * 新增
	 *
	 * @param saveDTO 保存参数
	 * @return 实体
	 */
	@Operation(summary = "新增", description = "新增", method = CommonConstant.PUT, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@PutMapping("/all")
	@RequestOperateLog(value = "新增", request = false)
	@PreAuthorize("hasAnyPermission('{}add')")
	default Result<Entity> save(@RequestBody @Validated SaveDTO saveDTO) {
		Result<Entity> result = handlerSave(saveDTO);
		if (result.getData() != null) {
			Entity model = BeanUtil.toBean(saveDTO, getEntityClass());
			getBaseService().save(model);
			result.setData(model);
		}
		return result;
	}

	/**
	 * 自定义新增
	 *
	 * @param model 保存对象
	 * @return 返回SUCCESS_RESPONSE, 调用默认更新, 返回其他不调用默认更新
	 */
	default Result<Entity> handlerSave(SaveDTO model) {
		return Result.success();
	}

}

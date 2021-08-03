package com.taotao.cloud.web.base.controller;

import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.log.annotation.RequestOperateLog;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.io.Serializable;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 删除Controller
 *
 * @param <Entity> 实体
 * @param <Id>     主键
 * @author zuihou
 * @date 2020年03月07日22:02:16
 */
public interface DeleteController<Entity, Id extends Serializable> extends BaseController<Entity> {

	/**
	 * 删除方法
	 *
	 * @param ids id
	 * @return 是否成功
	 */
	@Operation(summary = "删除", description = "删除", method = CommonConstant.DELETE, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@DeleteMapping
	@RequestOperateLog(description = "删除数据")
	@PreAuthorize("hasAnyPermission('{}delete')")
	default Result<Boolean> delete(@RequestBody List<Id> ids) {
		Result<Boolean> result = handlerDelete(ids);
		if (result.getData()) {
			getBaseService().removeByIds(ids);
		}
		return result;
	}

	/**
	 * 自定义删除
	 *
	 * @param ids id
	 * @return 返回SUCCESS_RESPONSE, 调用默认更新, 返回其他不调用默认更新
	 */
	default Result<Boolean> handlerDelete(List<Id> ids) {
		return Result.success(true);
	}

}

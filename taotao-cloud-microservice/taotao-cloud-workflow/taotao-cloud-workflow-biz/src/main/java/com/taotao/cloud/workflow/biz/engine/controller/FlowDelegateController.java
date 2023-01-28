package com.taotao.cloud.workflow.biz.engine.controller;

import com.taotao.cloud.common.model.PageResult;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.common.JsonUtils;
import com.taotao.cloud.common.utils.common.SecurityUtils;
import com.taotao.cloud.workflow.biz.common.base.Pagination;
import com.taotao.cloud.workflow.biz.common.base.vo.PaginationVO;
import com.taotao.cloud.workflow.biz.common.constant.MsgCode;
import com.taotao.cloud.workflow.biz.covert.FlowTaskConvert;
import com.taotao.cloud.workflow.biz.engine.entity.FlowDelegateEntity;
import com.taotao.cloud.workflow.biz.common.model.engine.flowdelegate.FlowDelegatListVO;
import com.taotao.cloud.workflow.biz.common.model.engine.flowdelegate.FlowDelegateCrForm;
import com.taotao.cloud.workflow.biz.common.model.engine.flowdelegate.FlowDelegateInfoVO;
import com.taotao.cloud.workflow.biz.common.model.engine.flowdelegate.FlowDelegateUpForm;
import com.taotao.cloud.workflow.biz.engine.service.FlowDelegateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import jakarta.validation.Valid;
import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 流程委托
 */
@Validated
@Tag(name = "工作流程-流程委托", description = "工作流程-流程委托")
@RestController
@RequestMapping("/api/workflow/engine/flow-delegate")
public class FlowDelegateController {

	@Autowired
	private FlowDelegateService flowDelegateService;

	@Operation(summary = "分页获取流程委托列表", description = "分页获取流程委托列表")
	@GetMapping("/page")
	public Result<PageResult<FlowDelegatListVO>> list(Pagination pagination) {
		List<FlowDelegateEntity> list = flowDelegateService.getList(pagination);
		PaginationVO paginationVO = JsonUtils.getJsonToBean(pagination, PaginationVO.class);
		List<FlowDelegatListVO> listVO = JsonUtils.getJsonToList(list, FlowDelegatListVO.class);
		return Result.page(listVO, paginationVO);
	}

	@Operation(summary = "获取流程委托信息", description = "获取流程委托信息")
	@GetMapping("/{id}")
	public Result<FlowDelegateInfoVO> info(@PathVariable("id") String id) throws DataException {
		FlowDelegateEntity entity = flowDelegateService.getInfo(id);
		return Result.success(FlowTaskConvert.INSTANCE.convert(entity));
	}

	@Operation(summary = "新建流程委托", description = "新建流程委托")
	@PostMapping
	public Result<Boolean> create(@RequestBody @Valid FlowDelegateCrForm flowDelegateCrForm) {
		FlowDelegateEntity entity = FlowTaskConvert.INSTANCE.convert(flowDelegateCrForm);
		Long userId = SecurityUtils.getUserId();
		if (userId.equals(entity.getToUserid())) {
			return Result.fail("委托人为自己，委托失败");
		}
		flowDelegateService.create(entity);
		return Result.success(true);
	}

	@Operation(summary = "更新流程委托", description = "更新流程委托")
	@PutMapping("/{id}")
	public Result<Boolean> update(@PathVariable("id") String id,
		@RequestBody @Valid FlowDelegateUpForm flowDelegateUpForm) {
		FlowDelegateEntity entity = FlowTaskConvert.INSTANCE.convert(flowDelegateUpForm);
		Long userId = SecurityUtils.getUserId();
		if (userId.equals(entity.getToUserid())) {
			return Result.fail("委托人为自己，委托失败");
		}

		boolean flag = flowDelegateService.update(id, entity);
		if (!flag) {
			return Result.success(MsgCode.FA002.get());
		}
		return Result.success(true);
	}

	@Operation(summary = "删除流程委托", description = "删除流程委托")
	@DeleteMapping("/{id}")
	public Result<Boolean> delete(@PathVariable("id") String id) {
		FlowDelegateEntity entity = flowDelegateService.getInfo(id);
		if (entity != null) {
			flowDelegateService.delete(entity);
			return Result.success(MsgCode.SU003.get());
		}
		return Result.fail(MsgCode.FA003.get());
	}
}

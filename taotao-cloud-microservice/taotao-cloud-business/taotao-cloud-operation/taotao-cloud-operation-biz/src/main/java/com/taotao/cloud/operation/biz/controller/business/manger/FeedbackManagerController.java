package com.taotao.cloud.operation.biz.controller.business.manger;

import cn.hutool.core.util.PageUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.web.request.annotation.RequestLogger;
import com.taotao.cloud.operation.biz.model.entity.Feedback;
import com.taotao.cloud.operation.biz.service.business.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端,意见反馈接口
 */
@RestController
@Tag(name = "管理端,意见反馈接口")
@RequestMapping("/manager/other/feedback")
public class FeedbackManagerController {

	/**
	 * 意见反馈
	 */
	@Autowired
	private FeedbackService feedbackService;
	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@Operation(summary = "查询意见反馈列表")
	//@ApiImplicitParam(name = "parentId", value = "父id，顶级为0", required = true, dataType = "String", paramType = "path")
	@GetMapping()
	public Result<IPage<Feedback>> page(PageVO pageVO) {
		return Result.success(feedbackService.page(PageUtil.initPage(pageVO)));
	}
	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@Operation(summary = "查看意见反馈")
	@GetMapping(value = "/{id}")
	public Result<Feedback> getFeedback(
		@Parameter(description = "意见反馈ID") @PathVariable String id) {
		return Result.success(this.feedbackService.getById(id));
	}

}

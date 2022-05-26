package com.taotao.cloud.message.biz.controller.manager;

import cn.hutool.core.util.PageUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.logger.annotation.RequestLogger;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端,会员消息接口
 */
@Validated
@RestController
@Tag(name = "平台管理端-会员消息管理API", description = "平台管理端-会员消息管理API")
@RequestMapping("/message/manager/memberNoticeSenter")
public class MemberNoticeSenterManagerController {

	@Autowired
	private MemberNoticeSenterService memberNoticeSenterService;

	@Operation(summary = "通过id获取", description = "通过id获取")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/{id}")
	public Result<MemberNoticeSenter> get(@PathVariable String id) {
		MemberNoticeSenter memberNoticeSenter = memberNoticeSenterService.getById(id);
		return Result.success(memberNoticeSenter);
	}

	@Operation(summary = "获取全部数据", description = "获取全部数据")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/all")
	public Result<List<MemberNoticeSenter>> getAll() {
		List<MemberNoticeSenter> list = memberNoticeSenterService.list();
		return Result.success(list);
	}

	@Operation(summary = "分页获取", description = "分页获取")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/page")
	public Result<IPage<MemberNoticeSenter>> getByPage(MemberNoticeSenter entity,
		SearchVO searchVo, PageVO page) {
		IPage<MemberNoticeSenter> data = memberNoticeSenterService.page(PageUtil.initPage(page),
			PageUtil.initWrapper(entity, searchVo));
		return Result.success(data);
	}

	@Operation(summary = "编辑或更新数据", description = "编辑或更新数据")
	@RequestLogger("编辑或更新数据")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PutMapping
	public Result<MemberNoticeSenter> saveOrUpdate(MemberNoticeSenter memberNoticeSenter) {
		memberNoticeSenterService.customSave(memberNoticeSenter);
		return Result.successResult.success(memberNoticeSenter);
	}

	@Operation(summary = "批量删除", description = "批量删除")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@DeleteMapping(value = "/{ids}")
	public Result<Object> delAllByIds(@PathVariable List ids) {
		memberNoticeSenterService.removeByIds(ids);
		return Result.success();
	}
}

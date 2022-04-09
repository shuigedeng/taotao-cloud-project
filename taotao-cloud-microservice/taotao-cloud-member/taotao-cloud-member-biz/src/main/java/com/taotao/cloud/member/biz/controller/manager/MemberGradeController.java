package com.taotao.cloud.member.biz.controller.manager;

import cn.hutool.core.util.PageUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.member.biz.entity.MemberGrade;
import com.taotao.cloud.member.biz.service.MemberGradeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
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
 * 管理端,会员等级API
 *
 * @since 2021/5/16 11:29 下午
 */
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("/member/manager/member/grade")
@Tag(name = "管理端-会员等级管理API", description = "管理端-会员等级管理API")
public class MemberGradeController {

	private final MemberGradeService memberGradeService;

	@Operation(summary = "通过id获取会员等级", description = "通过id获取会员等级", method = CommonConstant.GET)
	@RequestLogger("通过id获取会员等级")
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping(value = "/{id}")
	public Result<MemberGrade> get(@PathVariable Long id) {
		return Result.success(memberGradeService.getById(id));
	}

	@Operation(summary = "获取会员等级分页", description = "获取会员等级分页", method = CommonConstant.GET)
	@RequestLogger("获取会员等级分页")
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping(value = "/page")
	public Result<IPage<MemberGrade>> getByPage(PageVO page) {
		return Result.success(memberGradeService.page(PageUtil.initPage(page)));
	}

	@Operation(summary = "添加会员等级", description = "添加会员等级", method = CommonConstant.POST)
	@RequestLogger("添加会员等级")
	@PreAuthorize("@el.check('admin','timing:list')")
	@PostMapping(value = "")
	public Result<Object> daa(@Validated MemberGrade memberGrade) {
		if (memberGradeService.save(memberGrade)) {
			return Result.success(ResultEnum.SUCCESS);
		}
		throw new BusinessException(ResultEnum.ERROR);
	}

	@Operation(summary = "修改会员等级", description = "修改会员等级", method = CommonConstant.PUT)
	@RequestLogger("修改会员等级")
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping
	@PutMapping(value = "/{id}")
	public Result<Boolean> update(@PathVariable Long id, MemberGrade memberGrade) {
		return Result.success(memberGradeService.updateById(memberGrade));
	}

	@Operation(summary = "删除会员等级", description = "删除会员等级", method = CommonConstant.DELETE)
	@RequestLogger("删除会员等级")
	@PreAuthorize("@el.check('admin','timing:list')")
	@DeleteMapping(value = "/{id}")
	public Result<IPage<Object>> delete(@PathVariable Long id) {
		if (memberGradeService.getById(id).getIsDefault()) {
			throw new BusinessException(ResultEnum.USER_GRADE_IS_DEFAULT);
		}
		return Result.success(memberGradeService.removeById(id));
	}
}

package com.taotao.cloud.member.biz.controller.business.buyer;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.member.api.model.vo.MemberSignVO;
import com.taotao.cloud.member.biz.service.MemberSignService;
import com.taotao.cloud.netty.annotation.RequestParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 买家端-会员签到API
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-11 15:58:40
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "买家端-会员签到API", description = "买家端-会员签到API")
@RequestMapping("/member/buyer/member/sign")
public class MemberSignController {

	private final MemberSignService memberSignService;

	@Operation(summary = "会员签到", description = "会员签到")
	@RequestLogger
	@PreAuthorize("@el.check('admin','timing:list')")
	@PostMapping
	public Result<Boolean> memberSign() {
		return Result.success(memberSignService.memberSign());
	}

	@Operation(summary = "根据时间查询会员签到表，类型是YYYYmm", description = "根据时间查询会员签到表，类型是YYYYmm")
	@RequestLogger
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping
	public Result<List<MemberSignVO>> memberSign(@RequestParam String time) {
		return Result.success(memberSignService.getMonthSignDay(time));
	}

}

package com.taotao.cloud.member.biz.controller.buyer;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.member.biz.entity.MemberAddress;
import com.taotao.cloud.member.biz.service.IMemberAddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import org.apache.shardingsphere.distsql.parser.autogen.CommonDistSQLStatementParser.UserContext;
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
 * 买家端-会员地址接口
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-11 15:54:53
 */
@Validated
@RestController
@RequestMapping("/member/buyer/member-address")
@Tag(name = "买家端-会员地址API", description = "买家端-会员地址API")
public class MemberAddressController {

	@Autowired
	private IMemberAddressService IMemberAddressService;

	@Operation(summary = "获取会员收件地址分页列表", description = "获取会员收件地址分页列表", method = CommonConstant.GET)
	@RequestLogger(description = "获取会员收件地址分页列表")
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping
	public Result<IPage<MemberAddress>> page(PageVO page) {
		return Result.success(
			IMemberAddressService.getAddressByMember(page, UserContext.getCurrentUser().getId()));
	}

	@Operation(summary = "根据ID获取会员收件地址", description = "根据ID获取会员收件地址", method = CommonConstant.GET)
	@RequestLogger(description = "根据ID获取会员收件地址")
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping(value = "/{id}")
	public Result<MemberAddress> getShippingAddress(
		@Parameter(description = "会员地址ID", required = true) @NotBlank(message = "id不能为空")
		@PathVariable(value = "id") String id) {
		return Result.success(IMemberAddressService.getMemberAddress(id));
	}

	@Operation(summary = "获取当前会员默认收件地址", description = "获取当前会员默认收件地址", method = CommonConstant.GET)
	@RequestLogger(description = "获取当前会员默认收件地址")
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping(value = "/current/default")
	public Result<MemberAddress> getDefaultShippingAddress() {
		return Result.success(IMemberAddressService.getDefaultMemberAddress());
	}

	@Operation(summary = "新增会员收件地址", description = "新增会员收件地址", method = CommonConstant.POST)
	@RequestLogger(description = "新增会员收件地址")
	@PreAuthorize("@el.check('admin','timing:list')")
	@PostMapping
	public Result<MemberAddress> addShippingAddress(@Valid MemberAddress shippingAddress) {
		//添加会员地址
		shippingAddress.setMemberId(Objects.requireNonNull(UserContext.getCurrentUser()).getId());
		if (shippingAddress.getIsDefault() == null) {
			shippingAddress.setIsDefault(false);
		}
		return Result.success(IMemberAddressService.saveMemberAddress(shippingAddress));
	}

	@Operation(summary = "修改会员收件地址", description = "修改会员收件地址", method = CommonConstant.PUT)
	@RequestLogger(description = "修改会员收件地址")
	@PreAuthorize("@el.check('admin','timing:list')")
	@PutMapping
	public Result<Boolean> editShippingAddress(@Valid MemberAddress shippingAddress) {
		IMemberAddressService.updateMemberAddress(shippingAddress);
		return Result.success(true);
	}

	@Operation(summary = "删除会员收件地址", description = "删除会员收件地址", method = CommonConstant.DELETE)
	@RequestLogger(description = "删除会员收件地址")
	@PreAuthorize("@el.check('admin','timing:list')")
	@DeleteMapping(value = "/{id}")
	public Result<Boolean> delShippingAddressById(
		@Parameter(description = "会员地址ID", required = true) @NotBlank(message = "id不能为空")
		@PathVariable(value = "id") String id) {
		OperationalJudgment.judgment(IMemberAddressService.getById(id));
		IMemberAddressService.removeMemberAddress(id);
		return Result.success(true);
	}

}

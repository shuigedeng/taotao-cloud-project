package com.taotao.cloud.member.biz.controller.buyer;

import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.PageModel;
import com.taotao.cloud.common.model.PageParam;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.bean.BeanUtil;
import com.taotao.cloud.common.utils.common.SecurityUtil;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.member.api.vo.MemberAddressVO;
import com.taotao.cloud.member.biz.entity.MemberAddress;
import com.taotao.cloud.member.biz.service.IMemberAddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
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
 * 买家端-会员地址接口
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-11 15:54:53
 */
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("/member/buyer/member-address")
@Tag(name = "买家端-会员地址API", description = "买家端-会员地址API")
public class MemberAddressController {

	private final IMemberAddressService memberAddressService;

	@Operation(summary = "分页获取当前会员收件地址列表", description = "分页获取当前会员收件地址列表", method = CommonConstant.GET)
	@RequestLogger(description = "分页获取当前会员收件地址列表")
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping
	public Result<PageModel<MemberAddressVO>> page(@Validated PageParam page) {
		return Result.success(
			memberAddressService.getAddressByMember(page, SecurityUtil.getUserId()));
	}

	@Operation(summary = "根据ID获取会员收件地址", description = "根据ID获取会员收件地址", method = CommonConstant.GET)
	@RequestLogger(description = "根据ID获取会员收件地址")
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping(value = "/{id}")
	public Result<MemberAddressVO> getShippingAddress(
		@Parameter(description = "会员地址ID", required = true) @NotBlank(message = "id不能为空")
		@PathVariable(value = "id") String id) {
		MemberAddress memberAddress = memberAddressService.getMemberAddress(id);
		return Result.success(BeanUtil.copyProperties(memberAddress, MemberAddressVO.class));
	}

	@Operation(summary = "获取当前会员默认收件地址", description = "获取当前会员默认收件地址", method = CommonConstant.GET)
	@RequestLogger(description = "获取当前会员默认收件地址")
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping(value = "/current/default")
	public Result<MemberAddressVO> getDefaultShippingAddress() {
		MemberAddress memberAddress = memberAddressService.getDefaultMemberAddress();
		return Result.success(BeanUtil.copyProperties(memberAddress, MemberAddressVO.class));
	}

	@Operation(summary = "新增会员收件地址", description = "新增会员收件地址", method = CommonConstant.POST)
	@RequestLogger(description = "新增会员收件地址")
	@PreAuthorize("@el.check('admin','timing:list')")
	@PostMapping
	public Result<Boolean> addShippingAddress(@Valid MemberAddress shippingAddress) {
		//添加会员地址
		shippingAddress.setMemberId(Objects.requireNonNull(SecurityUtil.getUserId()));
		if (shippingAddress.getDefaulted() == null) {
			shippingAddress.setDefaulted(false);
		}
		return Result.success(memberAddressService.saveMemberAddress(shippingAddress));
	}

	@Operation(summary = "修改会员收件地址", description = "修改会员收件地址", method = CommonConstant.PUT)
	@RequestLogger(description = "修改会员收件地址")
	@PreAuthorize("@el.check('admin','timing:list')")
	@PutMapping
	public Result<Boolean> editShippingAddress(@Valid MemberAddress shippingAddress) {
		return Result.success(memberAddressService.updateMemberAddress(shippingAddress));
	}

	@Operation(summary = "删除会员收件地址", description = "删除会员收件地址", method = CommonConstant.DELETE)
	@RequestLogger(description = "删除会员收件地址")
	@PreAuthorize("@el.check('admin','timing:list')")
	@DeleteMapping(value = "/{id}")
	public Result<Boolean> delShippingAddressById(
		@Parameter(description = "会员地址ID", required = true) @NotBlank(message = "id不能为空")
		@PathVariable(value = "id") String id) {
		return Result.success(memberAddressService.removeMemberAddress(id));
	}

}

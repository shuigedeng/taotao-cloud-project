package com.taotao.cloud.member.biz.controller.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.member.biz.entity.MemberAddress;
import com.taotao.cloud.member.biz.service.IMemberAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 管理端,会员地址API
 *
 * 
 * @since 2020-02-25 14:10:16
 */
@Validated
@RestController
@RequestMapping("/member/manager/member-address")
@Tag(name = "管理端-会员地址API", description = "管理端-会员地址API")
public class MemberAddressController {

    @Autowired
    private IMemberAddressService IMemberAddressService;

	@Operation(summary = "会员地址分页列表", description = "会员地址分页列表", method = CommonConstant.GET)
	@RequestLogger(description = "会员地址分页列表")
	@PreAuthorize("@el.check('admin','timing:list')")
    @GetMapping("/{memberId}")
    public Result<IPage<MemberAddress>> getByPage(PageVO page,
		@Parameter(description = "会员地址ID", required = true) @PathVariable("memberId") String memberId) {
        return Result.success(IMemberAddressService.getAddressByMember(page, memberId));
    }

	@Operation(summary = "删除会员收件地址", description = "删除会员收件地址", method = CommonConstant.DELETE)
	@RequestLogger(description = "删除会员收件地址")
	@PreAuthorize("@el.check('admin','timing:list')")
    @DeleteMapping(value = "/{id}")
    public Result<Object> delShippingAddressById(
		@Parameter(description = "会员地址ID", required = true)@PathVariable String id) {
        IMemberAddressService.removeMemberAddress(id);
        return Result.success();
    }

	@Operation(summary = "修改会员收件地址", description = "修改会员收件地址", method = CommonConstant.PUT)
	@RequestLogger(description = "修改会员收件地址")
	@PreAuthorize("@el.check('admin','timing:list')")
    @PutMapping
    public Result<MemberAddress> editShippingAddress(@Valid MemberAddress shippingAddress) {
        //修改会员地址
        return Result.success(IMemberAddressService.updateMemberAddress(shippingAddress));
    }

	@Operation(summary = "新增会员收件地址", description = "新增会员收件地址", method = CommonConstant.POST)
	@RequestLogger(description = "新增会员收件地址")
	@PreAuthorize("@el.check('admin','timing:list')")
    @PostMapping
    public Result<MemberAddress> addShippingAddress(@Valid MemberAddress shippingAddress) {
        //添加会员地址
        return Result.success(IMemberAddressService.saveMemberAddress(shippingAddress));
    }


}

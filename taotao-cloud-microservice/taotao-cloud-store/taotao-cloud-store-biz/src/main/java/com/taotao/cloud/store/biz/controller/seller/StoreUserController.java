package com.taotao.cloud.store.biz.controller.seller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 店铺端,管理员接口
 *
 *
 * @since 2020/11/16 10:57
 */
@RestController
@Api(tags = "店铺端,管理员接口")
@RequestMapping("/store/user")
public class StoreUserController {
    @Autowired
    private MemberService memberService;


    @GetMapping(value = "/info")
    @ApiOperation(value = "获取当前登录用户接口")
    public Result<Member> getUserInfo() {
        AuthUser tokenUser = UserContext.getCurrentUser();
        if (tokenUser != null) {
            Member member = memberService.findByUsername(tokenUser.getUsername());
            member.setPassword(null);
            return Result.success(member);
        }
        throw new BusinessException(ResultEnum.USER_NOT_LOGIN);
    }


}

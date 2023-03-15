package com.taotao.cloud.sa.just.biz.just.justauth.feign;

import com.gitegg.platform.base.result.Result;
import com.gitegg.service.extension.client.dto.JustAuthSocialInfoDTO;
import com.gitegg.service.extension.justauth.entity.JustAuthSocial;
import com.gitegg.service.extension.justauth.entity.JustAuthSocialUser;
import com.gitegg.service.extension.justauth.service.IJustAuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @ClassName: JustAuthFeign
 * @Description: JustAuthFeign前端控制器
 * @author gitegg
 * @date 2019年5月18日 下午4:03:58
 */
@RestController
@RequestMapping(value = "/feign/justauth")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "JustAuthFeign|提供微服务调用接口")
@RefreshScope
public class JustAuthFeign {
    
    private final IJustAuthService justAuthService;

    @GetMapping(value = "/user/bind/id")
    @ApiOperation(value = "查询第三方用户绑定关系", notes = "查询第三方用户绑定关系")
    Result<Object> userBindId(@NotBlank @RequestParam("uuid") String uuid, @NotBlank @RequestParam("source") String source) {
        Long userId = justAuthService.userBindId(uuid, source);
        return Result.data(userId);
    }

    @PostMapping(value = "/user/create/or/update")
    @ApiOperation(value = "创建或更新第三方用户信息", notes = "创建或更新第三方用户信息")
    Result<Object> userCreateOrUpdate(@NotNull @RequestBody JustAuthSocialInfoDTO justAuthSocialInfoDTO) {
        Long socialId = justAuthService.userCreateOrUpdate(justAuthSocialInfoDTO);
        return Result.data(socialId);
    }
    
    @GetMapping(value = "/user/bind/query")
    @ApiOperation(value = "查询绑定第三方用户信息", notes = "查询绑定第三方用户信息")
    Result<Object> userBindQuery(@NotNull @RequestParam("socialId") Long socialId) {
        return justAuthService.userBindQuery(socialId);
    }
    
    /**
     * 查询第三方用户信息
     * @param socialId
     * @return
     */
    @GetMapping(value = "/social/info/query")
    Result<Object> querySocialInfo(@NotNull @RequestParam("socialId") Long socialId)
    {
        JustAuthSocial justAuthSocial = justAuthService.querySocialInfo(socialId);
        return Result.data(justAuthSocial);
    }

    @GetMapping(value = "/user/bind")
    @ApiOperation(value = "绑定第三方用户信息", notes = "绑定第三方用户信息")
    Result<JustAuthSocialUser> userBind(@NotNull @RequestParam("socialId") Long socialId, @NotNull @RequestParam("userId") Long userId) {
        JustAuthSocialUser justAuthSocialUser = justAuthService.userBind(socialId, userId);
        return Result.data(justAuthSocialUser);
    }
    
    @GetMapping(value = "/user/unbind")
    @ApiOperation(value = "解绑第三方用户信息", notes = "解绑第三方用户信息")
    Result<JustAuthSocialUser> userUnbind(@NotNull @RequestParam("socialId") Long socialId, @NotNull @RequestParam("userId") Long userId) {
        return justAuthService.userUnbind(socialId, userId);
    }
}

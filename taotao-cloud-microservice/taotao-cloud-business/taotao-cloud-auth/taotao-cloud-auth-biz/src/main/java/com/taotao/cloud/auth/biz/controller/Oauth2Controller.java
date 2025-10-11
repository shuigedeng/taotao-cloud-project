/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.auth.biz.controller;

import com.taotao.boot.cache.redis.repository.RedisRepository;
import com.taotao.boot.common.constant.RedisConstant;
import com.taotao.boot.common.exception.BaseException;
import com.taotao.boot.common.model.result.Result;
import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.boot.security.spring.utils.SecurityUtils;
import com.taotao.cloud.auth.biz.authentication.federation.Oauth2UserinfoResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Oath2Controller
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-12-22 15:50:56
 */
@Validated
@Tag(name = "Oauth2API", description = "Oauth2API")
@RestController
@RequestMapping("/auth/oauth2")
public class Oauth2Controller {

    @Autowired private RedisRepository redisRepository;

    /**
     * 获取当前认证的OAuth2用户信息，默认是保存在{@link jakarta.servlet.http.HttpSession}中的
     *
     * @param user OAuth2用户信息
     * @return OAuth2用户信息
     */
    @Operation(summary = "获取当前认证的OAuth2用户信息", description = "获取当前认证的OAuth2用户信息")
    // @RequestLogger
    @PreAuthorize("hasAuthority('express:company:info:id')")
    @GetMapping("/user")
    public Result<OAuth2User> user(@AuthenticationPrincipal OAuth2User user) {
        return Result.success(user);
    }

    /**
     * 获取当前认证的OAuth2客户端信息，默认是保存在{@link jakarta.servlet.http.HttpSession}中的
     *
     * @param oAuth2AuthorizedClient OAuth2客户端信息
     * @return OAuth2客户端信息
     */
    //    @Operation(summary = "获取当前认证的OAuth2客户端信息", description = "v")
    //    // @RequestLogger
    //    @PreAuthorize("hasAuthority('express:company:info:id')")
    //    @GetMapping("/client")
    //    public Result<OAuth2AuthorizedClient> user(
    //            @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient oAuth2AuthorizedClient) {
    //        return Result.success(oAuth2AuthorizedClient);
    //    }
    @Operation(summary = "退出系统", description = "退出系统")
    // @RequestLogger
    @PostMapping("/logout")
    public Result<Boolean> logout() {
        Authentication authentication = SecurityUtils.getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {
            Jwt jwt = jwtAuthenticationToken.getToken();
            String kid = (String) jwt.getHeaders().get("kid");
            try {
                long epochSecond = jwt.getExpiresAt().getEpochSecond();
                long nowTime = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).getEpochSecond();

                // 标识jwt令牌失效
                redisRepository.setEx(
                        RedisConstant.LOGOUT_JWT_KEY_PREFIX + kid, "", epochSecond - nowTime);

                // 添加用户退出日志

                // 删除用户在线信息

                return Result.success(true);
            } catch (Exception e) {
                LogUtils.error(e);
            }
        }

        throw new BaseException("退出失败");
    }

    @ResponseBody
    @GetMapping("/userInfo")
    public Oauth2UserinfoResult user(Principal principal) {
        Oauth2UserinfoResult result = new Oauth2UserinfoResult();
        //		if (!(principal instanceof JwtAuthenticationToken jwtAuthenticationToken)) {
        //			return result;
        //		}
        //		// 获取jwt解析内容
        //		Jwt token = jwtAuthenticationToken.getToken();
        //		// 获取当前用户的账号
        //		String account = token.getClaim(JwtClaimNames.SUB);
        //		// 获取scope
        //		List<String> scopes = token.getClaimAsStringList("scope");
        //		List<String> claimAsStringList = token.getClaimAsStringList(AUTHORITIES_KEY);
        //		if (!ObjUtil.isEmpty(claimAsStringList)) {
        //			scopes = null;
        //		}
        //		LambdaQueryWrapper<Oauth2BasicUser> accountWrapper =
        // Wrappers.lambdaQuery(Oauth2BasicUser.class)
        //			.eq(Oauth2BasicUser::getAccount, account);
        //		Oauth2BasicUser basicUser = basicUserService.getOne(accountWrapper);
        //		if (basicUser != null) {
        //			// 填充用户的权限信息
        //			this.fillUserAuthority(claimAsStringList, basicUser, scopes);
        //			BeanUtils.copyProperties(basicUser, result);
        //			// 根据用户信息查询三方登录信息
        //			LambdaQueryWrapper<Oauth2ThirdAccount> userIdWrapper =
        //				Wrappers.lambdaQuery(Oauth2ThirdAccount.class)
        //					.eq(Oauth2ThirdAccount::getUserId, basicUser.getId());
        //			Oauth2ThirdAccount oauth2ThirdAccount = thirdAccountService.getOne(userIdWrapper);
        //			if (oauth2ThirdAccount == null) {
        //				return result;
        //			}
        //			result.setCredentials(oauth2ThirdAccount.getCredentials());
        //			result.setThirdUsername(oauth2ThirdAccount.getThirdUsername());
        //			result.setCredentialsExpiresAt(oauth2ThirdAccount.getCredentialsExpiresAt());
        //			return result;
        //		}
        //		// 根据当前sub去三方登录表去查
        //		LambdaQueryWrapper<Oauth2ThirdAccount> wrapper =
        // Wrappers.lambdaQuery(Oauth2ThirdAccount.class)
        //			.eq(Oauth2ThirdAccount::getThirdUsername, account)
        //			.eq(Oauth2ThirdAccount::getType, token.getClaim("loginType"));
        //		Oauth2ThirdAccount oauth2ThirdAccount = thirdAccountService.getOne(wrapper);
        //		if (oauth2ThirdAccount == null) {
        //			return result;
        //		}
        //		// 查到之后反查基础用户表
        //		Oauth2BasicUser oauth2BasicUser =
        // basicUserService.getById(oauth2ThirdAccount.getUserId());
        //		BeanUtils.copyProperties(oauth2BasicUser, result);
        //		// 填充用户的权限信息
        //		this.fillUserAuthority(claimAsStringList, oauth2BasicUser, scopes);
        //		// 复制基础用户信息
        //		BeanUtils.copyProperties(oauth2BasicUser, result);
        // 设置三方用户信息
        //		result.setLocation(oauth2ThirdAccount.getLocation());
        //		result.setCredentials(oauth2ThirdAccount.getCredentials());
        //		result.setThirdUsername(oauth2ThirdAccount.getThirdUsername());
        //		result.setCredentialsExpiresAt(oauth2ThirdAccount.getCredentialsExpiresAt());
        return result;
    }
}

/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.oauth2.api.server.controller;

import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.auth.api.vo.TokenVO;
import com.taotao.cloud.oauth2.api.server.service.ITokensService;
import com.taotao.cloud.core.model.PageResult;
import com.taotao.cloud.core.model.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Token管理API
 *
 * @author dengtao
 * @since 2020/4/29 16:01
 * @version 1.0.0
 */
@RestController
@RequestMapping("/oauth/token")
@Api(value = "Token管理API", tags = {"Token管理API"})
public class TokenController {

    @Autowired
    private ITokensService tokensService;

    @ApiOperation(value = "token列表")
    @GetMapping("/list")
    public PageResult<TokenVO> list(@RequestParam Map<String, Object> params, String tenantId) {
        return tokensService.listTokens(params, tenantId);
    }

	/**
	 * 退出并删除token
	 * @param authHeader Authorization
	 */
	@DeleteMapping("/logout")
	public Result<Boolean> logout(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String authHeader) {
		if (StrUtil.isBlank(authHeader)) {
			throw new IllegalArgumentException("参数错误");
		}

		String tokenValue = authHeader.replace(OAuth2AccessToken.BEARER_TYPE, StrUtil.EMPTY).trim();
		return removeToken(tokenValue);
	}

	/**
	 * 令牌管理调用
	 * @param token token
	 */
	@DeleteMapping("/{token}")
	public Result<Boolean> removeToken(@PathVariable("token") String token) {
		Boolean result  = tokensService.removeToken(token);
		return Result.succeed(result);
	}

//    @ApiOperation(value = "用户名密码获取token")
//    @GetMapping("/user")
//    public Result<OAuth2AccessToken> getUserTokenInfo(@RequestParam(value = "username") String username,
//                                                      @RequestParam(value = "password") String password,
//                                                      HttpServletRequest request, HttpServletResponse response) {
//        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
//        OAuth2AccessToken oAuth2AccessToken = tokensService.getToken(request, response, token);
//        if (Objects.nonNull(oAuth2AccessToken)) {
//            return Result.succeed(oAuth2AccessToken);
//        }
//        return Result.failed("用户名或密码错误");
//    }

//    @ApiOperation(value = "openId获取token")
//    @PostMapping("/oauth/openId/token")
//    public Result<OAuth2AccessToken> getTokenByOpenId(@RequestBody OpenIdTokenDTO openIdTokenDTO,
//            HttpServletRequest request, HttpServletResponse response) {
//        String openId = openIdTokenDTO.getOpenId();
//        Assert.notNull(openId, "openId must be set");
//        OpenIdAuthenticationToken token = new OpenIdAuthenticationToken(openId);
//        OAuth2AccessToken oAuth2AccessToken = tokensService.getToken(request, response, token);
//        if (Objects.nonNull(oAuth2AccessToken)) {
//            return Result.succeed(oAuth2AccessToken);
//        }
//        return Result.authenticationFailed("openId错误");
//    }
//
//    @ApiOperation(value = "mobile获取token")
//    @PostMapping("/oauth/mobile/token")
//    public Result<OAuth2AccessToken> getTokenByMobile(@RequestBody MobileTokenDTO mobileTokenDTO,
//            HttpServletRequest request, HttpServletResponse response) {
//        String mobile = mobileTokenDTO.getMobile();
//        String password = mobileTokenDTO.getPassword();
//        Assert.notNull(mobile, "mobile must be set");
//        Assert.notNull(password, "password must be set");
//        MobileAuthenticationToken token = new MobileAuthenticationToken(mobile, password);
//        OAuth2AccessToken oAuth2AccessToken = tokensService.getToken(request, response, token);
//        if (Objects.nonNull(oAuth2AccessToken)) {
//            return Result.succeed(oAuth2AccessToken);
//        }
//        return Result.authenticationFailed("手机号或密码错误");
//    }

}

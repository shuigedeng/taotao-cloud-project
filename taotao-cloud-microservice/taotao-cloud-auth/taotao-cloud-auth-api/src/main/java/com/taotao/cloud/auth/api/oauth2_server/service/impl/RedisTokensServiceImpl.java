///*
// * Copyright 2002-2021 the original author or authors.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      https://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package com.taotao.cloud.oauth2.api.oauth2_server.service.impl;
//
//import cn.hutool.core.util.PageUtil;
//import cn.hutool.core.util.StrUtil;
//import com.taotao.cloud.auth.api.vo.TokenVO;
//import com.taotao.cloud.common.constant.SecurityConstant;
//import com.taotao.cloud.common.exception.BusinessException;
//import com.taotao.cloud.common.utils.LogUtil;
//import com.taotao.cloud.core.model.PageResult;
//import com.taotao.cloud.core.utils.AuthUtil;
//import com.taotao.cloud.core.utils.WebUtil;
//import com.taotao.cloud.oauth2.api.oauth2_server.service.ITokensService;
//import com.taotao.cloud.redis.repository.RedisRepository;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import org.apache.commons.collections4.MapUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AbstractAuthenticationToken;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.InternalAuthenticationServiceException;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
//import org.springframework.security.oauth2.common.OAuth2AccessToken;
//import org.springframework.security.oauth2.common.OAuth2RefreshToken;
//import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
//import org.springframework.security.oauth2.provider.ClientDetails;
//import org.springframework.security.oauth2.provider.ClientDetailsService;
//import org.springframework.security.oauth2.provider.OAuth2Authentication;
//import org.springframework.security.oauth2.provider.OAuth2Request;
//import org.springframework.security.oauth2.provider.TokenRequest;
//import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
//import org.springframework.security.oauth2.provider.token.TokenStore;
//import org.springframework.stereotype.Service;
//
///**
// * RedisTokensServiceImpl
// *
// * @author shuigedeng
// * @since 2020/4/29 16:03
// * @version 1.0.0
// */
//@Service
//public class RedisTokensServiceImpl implements ITokensService {
//
//	@Autowired
//	private RedisRepository redisRepository;
//
//	@Resource
//	private AuthorizationServerTokenServices authorizationServerTokenServices;
//
//	@Autowired
//	private AuthenticationManager authenticationManager;
//
//	@Autowired
//	private PasswordEncoder passwordEncoder;
//
//	@Autowired
//	private ClientDetailsService clientDetailsService;
//
//	@Autowired
//	private TokenStore tokenStore;
//
//	@Override
//	public PageResult<TokenVO> listTokens(Map<String, Object> params, String clientId) {
//		Integer page = MapUtils.getInteger(params, "page");
//		Integer limit = MapUtils.getInteger(params, "limit");
//		int[] startEnds = PageUtil.transToStartEnd(page, limit);
//		//根据请求参数生成redis的key
//		String redisKey = getRedisKey(params, clientId);
//		long size = redisRepository.length(redisKey);
//		List<TokenVO> result = new ArrayList<>(limit);
//		//查询token集合
//		List<Object> tokenObjs = redisRepository.getList(redisKey, startEnds[0], startEnds[1] - 1);
//		if (tokenObjs != null) {
//			for (Object obj : tokenObjs) {
//				DefaultOAuth2AccessToken accessToken = (DefaultOAuth2AccessToken) obj;
//				//构造token对象
//				TokenVO tokenVo = new TokenVO();
//				tokenVo.setTokenValue(accessToken.getValue());
//				tokenVo.setExpiration(accessToken.getExpiration());
//
//				//获取用户信息
//				Object authObj = redisRepository.get(SecurityConstant.REDIS_TOKEN_AUTH + accessToken.getValue());
//				OAuth2Authentication authentication = (OAuth2Authentication) authObj;
//				if (authentication != null) {
//					OAuth2Request request = authentication.getOAuth2Request();
//					tokenVo.setUsername(authentication.getName());
//					tokenVo.setClientId(request.getClientId());
//					tokenVo.setGrantType(request.getGrantType());
//				}
//
//				result.add(tokenVo);
//			}
//		}
//		return PageResult.succeed(size, limit, page, result);
//	}
//
//	@Override
//	public OAuth2AccessToken getToken(HttpServletRequest request, HttpServletResponse response, AbstractAuthenticationToken token) {
//		try {
//			final String[] clientInfos = AuthUtil.extractClient(request);
//			String clientId = clientInfos[0];
//			String clientSecret = clientInfos[1];
//
//			ClientDetails clientDetails = getClient(clientId, clientSecret);
//
//			TokenRequest tokenRequest = new TokenRequest(WebUtil.getAllRequestParam(request), clientId,
//				clientDetails.getScope(), "password");
//
//			OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);
//			Authentication authentication = authenticationManager.authenticate(token);
//
//			SecurityContextHolder.getContext().setAuthentication(authentication);
//
//			OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);
//			oAuth2Authentication.setAuthenticated(true);
//			return authorizationServerTokenServices.createAccessToken(oAuth2Authentication);
//		} catch (BadCredentialsException | InternalAuthenticationServiceException e) {
//			LogUtil.error("获取token失败， {0}", e.getMessage());
//			return null;
//		}
//	}
//
//	@Override
//	public Boolean removeToken(String token) {
//		OAuth2AccessToken accessToken = tokenStore.readAccessToken(token);
//		if (accessToken == null || StrUtil.isBlank(accessToken.getValue())) {
//			throw new BusinessException("未查询到token信息");
//		}
//
//		// 清空access token
//		tokenStore.removeAccessToken(accessToken);
//
//		// 清空 refresh token
//		OAuth2RefreshToken refreshToken = accessToken.getRefreshToken();
//		tokenStore.removeRefreshToken(refreshToken);
//		return true;
//	}
//
//	private ClientDetails getClient(String clientId, String clientSecret) {
//		ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);
//
//		if (clientDetails == null) {
//			throw new UnapprovedClientAuthenticationException("clientId对应的信息不存在");
//		} else if (!passwordEncoder.matches(clientSecret, clientDetails.getClientSecret())) {
//			throw new UnapprovedClientAuthenticationException("clientSecret不匹配");
//		}
//		return clientDetails;
//	}
//
//	/**
//	 * 根据请求参数生成redis的key
//	 *
//	 * @param params   params
//	 * @param clientId clientId
//	 * @return java.lang.String
//	 * @author shuigedeng
//	 * @since 2020/4/29 16:03
//	 */
//	private String getRedisKey(Map<String, Object> params, String clientId) {
//		String result;
//		String username = MapUtils.getString(params, "username");
//		if (StrUtil.isNotEmpty(username)) {
//			result = SecurityConstant.REDIS_UNAME_TO_ACCESS + clientId + ":" + username;
//		} else {
//			result = SecurityConstant.REDIS_CLIENT_ID_TO_ACCESS + clientId;
//		}
//		return result;
//	}
//}

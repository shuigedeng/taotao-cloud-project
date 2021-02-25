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
package com.taotao.cloud.auth.biz.service;

import com.taotao.cloud.auth.api.vo.TokenVO;
import com.taotao.cloud.core.model.PageResult;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * ITokensService
 *
 * @author dengtao
 * @date 2020/4/29 16:02
 * @since v1.0
 */
public interface ITokensService {

    /**
     * 查询token列表
     *
     * @param params   请求参数
     * @param clientId 应用id
     * @author dengtao
     * @date 2020/4/29 16:02
     */
    PageResult<TokenVO> listTokens(Map<String, Object> params, String clientId);

    /**
     * 获取token
     *
     * @param request  request
     * @param response response
     * @param token    token
     * @author dengtao
     * @date 2020/4/29 17:07
     */
    OAuth2AccessToken getToken(HttpServletRequest request, HttpServletResponse response, AbstractAuthenticationToken token);

	Boolean removeToken(String token);
}

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

package com.taotao.cloud.auth.infrastructure.authentication.oidc;

import com.taotao.boot.security.spring.constants.BaseConstants;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationContext;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

/**
 * <p>TODO </p>
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-10 17:24:44
 */
public class TtcOidcUserInfoMapper implements Function<OidcUserInfoAuthenticationContext, OidcUserInfo> {

    /**
     * 电子邮件索赔
     */
    private static final List<String> EMAIL_CLAIMS =
            Arrays.asList(StandardClaimNames.EMAIL, StandardClaimNames.EMAIL_VERIFIED);
    /**
     * 电话索赔
     */
    private static final List<String> PHONE_CLAIMS =
            Arrays.asList(StandardClaimNames.PHONE_NUMBER, StandardClaimNames.PHONE_NUMBER_VERIFIED);
    /**
     * 个人资料索赔
     */
    private static final List<String> PROFILE_CLAIMS = Arrays.asList(
            StandardClaimNames.NAME,
            StandardClaimNames.FAMILY_NAME,
            StandardClaimNames.GIVEN_NAME,
            StandardClaimNames.MIDDLE_NAME,
            StandardClaimNames.NICKNAME,
            StandardClaimNames.PREFERRED_USERNAME,
            StandardClaimNames.PROFILE,
            StandardClaimNames.PICTURE,
            StandardClaimNames.WEBSITE,
            StandardClaimNames.GENDER,
            StandardClaimNames.BIRTHDATE,
            StandardClaimNames.ZONEINFO,
            StandardClaimNames.LOCALE,
            StandardClaimNames.UPDATED_AT);

    /**
     * 应用
     *
     * @param authenticationContext 身份验证上下文
     * @return {@link OidcUserInfo }
     * @since 2023-07-10 17:24:44
     */
    @Override
    public OidcUserInfo apply(OidcUserInfoAuthenticationContext authenticationContext) {
        OidcUserInfoAuthenticationToken authentication = authenticationContext.getAuthentication();
        if (authentication.getPrincipal() instanceof BearerTokenAuthentication principal) {
					return new OidcUserInfo(getClaims(principal.getTokenAttributes()));
        } else {
            JwtAuthenticationToken principal = (JwtAuthenticationToken) authentication.getPrincipal();
            return new OidcUserInfo(getClaims(principal.getToken().getClaims()));
        }
    }

    /**
     * 获得索赔
     *
     * @param claims 索赔
     * @return {@link Map }<{@link String }, {@link Object }>
     * @since 2023-07-10 17:24:45
     */
    private static Map<String, Object> getClaims(Map<String, Object> claims) {
        Set<String> needRemovedClaims = new HashSet<>(32);
        needRemovedClaims.add(BaseConstants.AUTHORITIES);

        Map<String, Object> requestedClaims = new HashMap<>(claims);
        requestedClaims.keySet().removeIf(needRemovedClaims::contains);

        return requestedClaims;
    }
}

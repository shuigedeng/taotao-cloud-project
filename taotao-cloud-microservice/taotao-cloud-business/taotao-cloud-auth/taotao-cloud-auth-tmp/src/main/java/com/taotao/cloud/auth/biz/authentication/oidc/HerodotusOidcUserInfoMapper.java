

package com.taotao.cloud.auth.biz.authentication.oidc;

import com.taotao.cloud.security.springsecurity.core.constants.BaseConstants;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationContext;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.*;
import java.util.function.Function;

/**
 * <p>Description: TODO </p>
 *
 * 
 * @date : 2022/10/15 11:58
 */
public class HerodotusOidcUserInfoMapper implements Function<OidcUserInfoAuthenticationContext, OidcUserInfo> {

    private static final List<String> EMAIL_CLAIMS = Arrays.asList(
            StandardClaimNames.EMAIL,
            StandardClaimNames.EMAIL_VERIFIED
    );
    private static final List<String> PHONE_CLAIMS = Arrays.asList(
            StandardClaimNames.PHONE_NUMBER,
            StandardClaimNames.PHONE_NUMBER_VERIFIED
    );
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
            StandardClaimNames.UPDATED_AT
    );

    @Override
    public OidcUserInfo apply(OidcUserInfoAuthenticationContext authenticationContext) {
        OidcUserInfoAuthenticationToken authentication = authenticationContext.getAuthentication();
        if (authentication.getPrincipal() instanceof BearerTokenAuthentication) {
            BearerTokenAuthentication principal = (BearerTokenAuthentication) authentication.getPrincipal();
            return new OidcUserInfo(getClaims(principal.getTokenAttributes()));
        } else {
            JwtAuthenticationToken principal = (JwtAuthenticationToken) authentication.getPrincipal();
            return new OidcUserInfo(getClaims(principal.getToken().getClaims()));
        }
    }

    private static Map<String, Object> getClaims(Map<String, Object> claims) {
        Set<String> needRemovedClaims = new HashSet<>(32);
        needRemovedClaims.add(BaseConstants.AUTHORITIES);

        Map<String, Object> requestedClaims = new HashMap<>(claims);
        requestedClaims.keySet().removeIf(needRemovedClaims::contains);

        return requestedClaims;
    }
}

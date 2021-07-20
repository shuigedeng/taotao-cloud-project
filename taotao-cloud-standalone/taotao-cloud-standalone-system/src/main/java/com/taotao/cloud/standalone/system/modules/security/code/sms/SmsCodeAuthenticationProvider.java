package com.taotao.cloud.standalone.system.modules.security.code.sms;

import com.taotao.cloud.standalone.system.modules.security.UserDetailsServiceImpl;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @Classname SmsCodeAuthenticationProvider
 * @Description TODO
 * @Author shuigedeng
 * @since 2019-07-08 11:49
 * @Version 1.0
 */
public class SmsCodeAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsServiceImpl userDetailService;

    @Override
    public Authentication authenticate(Authentication authentication) {
        SmsCodeAuthenticationToken authenticationToken = (SmsCodeAuthenticationToken) authentication;

        String mobile = (String) authenticationToken.getPrincipal();

        //调用自定义的userDetailsService认证
        UserDetails userDetails = userDetailService.loadUserByMobile(mobile);
        //如果user不为空重新构建SmsCodeAuthenticationToken（已认证）
        SmsCodeAuthenticationToken authenticationResult = new SmsCodeAuthenticationToken(userDetails, userDetails.getAuthorities());
        authenticationResult.setDetails(authenticationToken.getDetails());

        return authenticationResult;
    }

    /**
     * 只有Authentication为SmsCodeAuthenticationToken使用此Provider认证
     * @param aClass
     * @return
     */
    @Override
    public boolean supports(Class<?> aClass) {
        return SmsCodeAuthenticationToken.class.isAssignableFrom(aClass);
    }

    public UserDetailsServiceImpl getUserDetailService() {
        return userDetailService;
    }

    public void setUserDetailService(UserDetailsServiceImpl userDetailService) {
        this.userDetailService = userDetailService;
    }

}
